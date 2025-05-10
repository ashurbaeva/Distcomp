# publisher/kafka_consumer.py

import threading
import json
from kafka import KafkaConsumer
from .config import KAFKA_BOOTSTRAP_SERVERS, OUT_TOPIC

def start_consumer(update_post_callback):
    """
    Запуск Kafka Consumer для топика OUT_TOPIC.
    update_post_callback – функция, которая обновляет статус Post в системе (например, в БД).
    """
    consumer = KafkaConsumer(
        OUT_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_deserializer=lambda v: json.loads(v.decode('utf-8')),
        auto_offset_reset='earliest',
        group_id='publisherGroup'
    )

    def consume():
        for message in consumer:
            post_data = message.value
            # Здесь вызываем callback для обновления состояния Post
            update_post_callback(post_data)
    
    # Запуск в отдельном потоке
    thread = threading.Thread(target=consume, daemon=True)
    thread.start()
