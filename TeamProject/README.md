# Team Project: Real-Time Data Analysis Using Streaming Systems


## Team Members

Mingji Han, Yongxin Jiang, and Huaizhen Chen

## Production Mission
In this project, we are going to build Real-Time Data Analysis pipeline using streaming systems
and provide illustrative real-time data applications demo built above the pipeline.

## User Stories
### Real-time Business Data Analysis: 
As a data scientist in a online shopping company, I want to analyze the real-time order data so that I can get the trending products and sales of different product categories in a real-time fashion. Those results could provide the latest insights with a low latencies, so the company can react responsively to prepare products and promotion activities actively.
### Fraud Detection:
For modern network payment systems, system administrators want to carry out an anti-fraud analysis of each transaction, by analyzing the transaction amount, the payer transaction frequency, the payee collection frequency, etc. to determine whether it is necessary to intercept the transaction in real time or increase the authentication code to confirm the transaction, kafka real-time can be in this regard to reduce the completion of the anti-fraud time required to determine
### Use real-time data in healthcare, monitoring patient status.
As a doctor or a nurse, we can ensure the patients are receiving an appropriate level of care. If the patients are in an emergency situation and need to be transferred to ICU as soon as possible. Then the system can help doctors know the situation of patients and get other cares before worse situation happened. 

## MVP

A streaming data analysis pipeline,which is able to:  
Ingress data from sources to message queue  
Consume real-time data from message queue  
Process and analyze using streaming processing system  
Persist the results to data sinks (File System/Database)  

## Project Setup

1. Install and Deploy Apache Flink following instructions on https://nightlies.apache.org/flink/flink-docs-stable/docs/try-flink/local_installation/  
2. Create a Maven project and use `pom.xml` and all java files in our folder
3. Compile and run `frund2.java` and `patient.java`

## Team Project Sprint 1 (BU Access Needed)

https://docs.google.com/presentation/d/1gxP8A0nqWP1TXydMY4JriSYfQrVRvRxHhSRsDAz5wFs/edit?usp=sharing

## Team Project Sprint 2 (BU Access Needed)

https://docs.google.com/presentation/d/1InkqPtdU2IcGaexBYUiyjUelMpovVCtQhOd1kfJtAaE/edit?usp=sharing

## Reference

The project refers projects, documents, code, and other recources from the following sources:

1. Apache Flink https://nightlies.apache.org/flink/flink-docs-release-1.17/  

2. Apache Kafka https://kafka.apache.org/  

