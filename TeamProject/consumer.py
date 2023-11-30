from kafka import KafkaConsumer
import sys

consumer = KafkaConsumer(sys.argv[1], bootstrap_servers='localhost:9092', auto_offset_reset='earliest')

for msg in consumer:
    print(msg.value.decode('utf-8'))