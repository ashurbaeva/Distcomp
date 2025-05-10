# Используем официальный образ Python
FROM python:3.11-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл зависимостей и устанавливаем их
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Копируем исходный код приложения
COPY ./app ./app

# Запускаем приложение с uvicorn на порту 24110
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "24110"]
