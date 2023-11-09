from kafka import KafkaConsumer
import sys

consumer = KafkaConsumer(sys.argv[1])
for msg in consumer:
    print(msg.value.decode('utf-8'))