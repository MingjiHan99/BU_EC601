from kafka import KafkaProducer
import os
import sys
producer = KafkaProducer(bootstrap_servers='localhost:9092')

with open(sys.argv[1], 'r') as f:
    lines = f.readlines()
    for l in lines:
        producer.send(sys.argv[2],l.encode('utf-8'))
        producer.flush()

