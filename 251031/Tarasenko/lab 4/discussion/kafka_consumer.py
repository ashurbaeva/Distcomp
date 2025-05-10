# discussion/kafka_consumer.py

import threading
import json
from kafka import KafkaConsumer
from .config import KAFKA_BOOTSTRAP_SERVERS, IN_TOPIC
from .moderation import moderate_post
from .kafka_producer import send_moderation_result

def start_consumer():
    """
    Запуск Kafka Consumer для топика InTopic.
    Полученные сообщения модераторятся, и результат отправляется в OutTopic.
    """
    consumer = KafkaConsumer(
        IN_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_deserializer=lambda v: json.loads(v.decode('utf-8')),
        auto_offset_reset='earliest',
        group_id='discussionGroup'
    )

    def consume():
        for message in consumer:
            post_data = message.value
            print(f"Получен Post на модерацию: {post_data}")
            moderated_post = moderate_post(post_data)
            print(f"Результат модерации: {moderated_post}")
            send_moderation_result(moderated_post)
    
    thread = threading.Thread(target=consume, daemon=True)
    thread.start()
