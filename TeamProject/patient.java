package com.tese1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class patient {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> rawHealthData = env.socketTextStream("localhost", 9999);

        DataStream<PatientData> healthData = rawHealthData.map(new MapFunction<String, PatientData>() {
            @Override
            public PatientData map(String value) {
                String[] parts = value.split(",");
                return new PatientData(
                        Integer.parseInt(parts[0]),
                        Float.parseFloat(parts[1]),
                        Float.parseFloat(parts[2]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]),
                        Float.parseFloat(parts[5]),
                        Float.parseFloat(parts[6])
                );
            }
        });

        healthData.map(new HealthChecker()).print();

        env.execute("ICU Health Monitor");
    }

    public static class HealthChecker implements MapFunction<PatientData, HealthAlert> {
        @Override
        public HealthAlert map(PatientData data) {
            StringBuilder alertMessage = new StringBuilder();

            if ((data.sex == 0 && (data.got > 37 || data.gpt > 41)) ||
                    (data.sex == 1 && (data.got > 31 || data.gpt > 31))) {
                alertMessage.append("Abnormal liver function detected! ");
            }

            if (data.glucose < 100) {
                alertMessage.append("Low glucose level detected! ");
            }

            if (data.oxygen < 95) {
                alertMessage.append("Low oxygen level detected! ");
            }

            if (data.bpSystolic < 90 || data.bpSystolic > 140 || data.bpDiastolic < 60 || data.bpDiastolic > 90) {
                alertMessage.append("Abnormal blood pressure detected! ");
            }

            return new HealthAlert(data.patientId, alertMessage.toString());
        }
    }
}

class PatientData {
    int patientId;
    float got;
    float gpt;
    int sex;
    int glucose;
    int oxygen;
    int bpSystolic;
    int bpDiastolic;

    public PatientData(int patientId, float got, float gpt, int sex, int glucose, int oxygen, int bpSystolic, int bpDiastolic) {
        this.patientId = patientId;
        this.got = got;
        this.gpt = gpt;
        this.sex = sex;
        this.glucose = glucose;
        this.oxygen = oxygen;
        this.bpSystolic = bpSystolic;
        this.bpDiastolic = bpDiastolic;
    }

    public PatientData(int patientId, float got, float gpt, int sex, int glucose, float v, float v1) {
    }
}

class HealthAlert {
    int patientId;
    String alertMessage;

    public HealthAlert(int patientId, String alertMessage) {
        this.patientId = patientId;
        this.alertMessage = alertMessage;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId + ", Alert: " + alertMessage;
    }
}
