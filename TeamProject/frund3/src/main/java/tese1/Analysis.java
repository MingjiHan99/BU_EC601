package tese1;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;


public class Analysis {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("analysis")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> rawSales = env.fromSource(source, WatermarkStrategy.<String>forMonotonousTimestamps()
                .withTimestampAssigner((event, timestamp) -> {
            long l = Long.parseLong(event.replace("\n", "").split(" ")[2]);
            return l;
        }), "kafka source");

        DataStream<Sale> sales = rawSales.map(new MapFunction<String, Sale>() {
            @Override
            public Sale map(String value) throws Exception {
                System.out.println(value);
                String[] parts = value.replace("\n","").split(" ");
                if (parts.length < 3) {
                    System.err.println("Invalid transaction format: " + value);
                    return null;
                }
                return new Sale(
                        parts[0],
                        Double.parseDouble(parts[1]),
                        Long.parseLong(parts[2])
                );
            }
        });

        sales
                .keyBy(Sale::getCategory)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .aggregate(new AggregateFunction<Sale, Double, Double>() {

                    @Override
                    public Double createAccumulator() {
                        return new Double(0.0);
                    }

                    @Override
                    public Double add(Sale sale, Double aDouble) {
                        return sale.getAmount() + aDouble;
                    }

                    @Override
                    public Double getResult(Double aDouble) {
                        return aDouble;
                    }

                    @Override
                    public Double merge(Double aDouble, Double acc1) {
                        return aDouble + acc1;
                    }


                })
                .print();

        env.execute("Real-Time Business Analysis");
    }


}
