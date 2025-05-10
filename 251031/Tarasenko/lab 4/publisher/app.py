# publisher/app.py

from flask import Flask, request, jsonify
from .config import HOST, PORT, API_PREFIX
from .models import Post, Tweet, Creator, PostState
from .kafka_producer import send_post
from .kafka_consumer import start_consumer

app = Flask(__name__)

# Пример хранилища Post (в реальном приложении – база данных)
posts_db = {}

def update_post_status(post_data):
    post_id = post_data.get('id')
    if post_id in posts_db:
        posts_db[post_id]['state'] = post_data.get('state')
        print(f"Обновлён статус Post {post_id}: {post_data.get('state')}")
    else:
        print(f"Post с id {post_id} не найден в базе.")

# Запуск consumer для получения результатов модерации
start_consumer(update_post_status)

@app.route(f"{API_PREFIX}/posts", methods=['POST'])
def create_post():
    data = request.get_json()
    # Простейшая валидация и создание Post
    try:
        creator = Creator(**data['tweet']['creator'])
        tweet = Tweet(id=data['tweet']['id'], creator=creator, content=data['tweet']['content'])
        post = Post(id=data['id'], tweet=tweet, content=data['content'])
        posts_db[post.id] = {
            'id': post.id,
            'tweet': {'id': tweet.id, 'content': tweet.content},
            'content': post.content,
            'state': post.state.value,
            'created_at': post.created_at
        }
        # Отправка Post в Kafka
        send_post(post)
        return jsonify({'message': 'Post создан и отправлен на модерацию', 'post': posts_db[post.id]}), 201
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == '__main__':
    app.run(host=HOST, port=PORT)
