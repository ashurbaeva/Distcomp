import redis.asyncio as redis

redis_client = None

async def get_redis():
    global redis_client
    if not redis_client:
        redis_client = redis.Redis(host='localhost', port=6379, decode_responses=True)
    return redis_client
