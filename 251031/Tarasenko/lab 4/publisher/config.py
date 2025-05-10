# publisher/config.py

# Параметры REST API
HOST = 'localhost'
PORT = 24110
API_PREFIX = '/api/v1.0'

# Параметры подключения к Kafka
KAFKA_BOOTSTRAP_SERVERS = ['localhost:9092']
IN_TOPIC = 'InTopic'
OUT_TOPIC = 'OutTopic'
