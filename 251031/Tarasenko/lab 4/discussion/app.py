# discussion/app.py

from flask import Flask, jsonify
from .config import HOST, PORT, API_PREFIX
from .kafka_consumer import start_consumer

app = Flask(__name__)

# Запускаем Kafka Consumer для модерации
start_consumer()

# Если необходим REST API для получения информации или управления модерацией
@app.route(f"{API_PREFIX}/health", methods=['GET'])
def health_check():
    return jsonify({'status': 'discussion service is running'}), 200

if __name__ == '__main__':
    app.run(host=HOST, port=PORT)
