import json
from uuid import UUID
from app.db.redis_client import get_redis
from app.db.cassandra import get_post_by_id_from_cassandra

REDIS_EXPIRE = 300  # 5 мин

async def get_post_by_id(post_id: UUID):
    redis = await get_redis()
    cached = await redis.get(f'post:{post_id}')
    if cached:
        return json.loads(cached)

    post = await get_post_by_id_from_cassandra(post_id)
    if post:
        await redis.set(f'post:{post_id}', json.dumps(post), ex=REDIS_EXPIRE)
    return post
