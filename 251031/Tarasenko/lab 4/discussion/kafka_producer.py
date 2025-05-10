# discussion/kafka_producer.py

from kafka import KafkaProducer
import json
from .config import KAFKA_BOOTSTRAP_SERVERS, OUT_TOPIC

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    value_serializer=lambda v: json.dumps(v).encode('utf-8'),
    key_serializer=lambda k: str(k).encode('utf-8')
)

def send_moderation_result(post_data: dict):
    """
    Отправка результатов модерации в топик OutTopic.
    Используется идентификатор твита в качестве ключа.
    """
    key = post_data.get('tweet', {}).get('id', '0')
    producer.send(OUT_TOPIC, key=key, value=post_data)
    producer.flush()
