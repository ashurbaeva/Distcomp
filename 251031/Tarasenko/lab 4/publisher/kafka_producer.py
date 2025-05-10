# publisher/kafka_producer.py

from kafka import KafkaProducer
import json
from .config import KAFKA_BOOTSTRAP_SERVERS, IN_TOPIC
from .models import Post

# Инициализация продюсера
producer = KafkaProducer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    value_serializer=lambda v: json.dumps(v).encode('utf-8'),
    key_serializer=lambda k: str(k).encode('utf-8')
)

def send_post(post: Post):
    """
    Отправка сообщения (объекта Post) в топик InTopic.
    Используем идентификатор Tweet в качестве ключа для обеспечения отправки в одну partition.
    """
    key = post.tweet.id  # ключ – id твита
    value = asdict_post(post)
    producer.send(IN_TOPIC, key=key, value=value)
    producer.flush()

def asdict_post(post: Post) -> dict:
    # Преобразование объекта Post в dict
    import dataclasses
    return dataclasses.asdict(post)
