package tese1;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class frund2 {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("fraud_detection_2")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> rawTransactions = env.fromSource(source, WatermarkStrategy.<String>forMonotonousTimestamps()
                .withTimestampAssigner((event, timestamp) -> {
            long l = Long.parseLong(event.replace("\n", "").split(" ")[3]);
            return l;
        }), "kafka source");

        DataStream<Transaction> transactions = rawTransactions.map(new MapFunction<String, Transaction>() {
            @Override
            public Transaction map(String value) throws Exception {
                System.out.println(value);
                String[] parts = value.replace("\n","").split(" ");
                if (parts.length < 4) {
                    System.err.println("Invalid transaction format: " + value);
                    return null;
                }
                return new Transaction(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Long.parseLong(parts[3])
                );
            }
        }).filter((FilterFunction<Transaction>) transaction -> transaction != null);

        transactions
                .keyBy(Transaction::getPayer)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .process(new FraudDetector())
                .print();

        env.execute("Fraud Detection Job with Time Window");
    }

    public static class FraudDetector extends ProcessWindowFunction<Transaction, String, String, TimeWindow> {

        @Override
        public void process(String key, Context context, Iterable<Transaction> elements, Collector<String> out) throws Exception {
            int count = 0;
            System.out.println("Processing");
            for (Transaction transaction : elements) {
                System.out.println("Processing transaction: " + transaction);

                count++;
                if (transaction.getAmount() > 20000) {
                    out.collect("Suspicious transaction detected! Transaction exceeding 1000: " + transaction);
                }
            }

            System.out.println("Total transactions in this window: " + count);

            if (count > 5) {
                out.collect("Suspicious transaction detected! More than 5 transactions in one hour by payer: " + key);
                for (Transaction transaction : elements) {
                    out.collect(transaction.toString());
                }
            }
        }
    }
}
