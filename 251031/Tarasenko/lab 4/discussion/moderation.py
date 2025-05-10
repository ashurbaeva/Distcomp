# discussion/moderation.py

def moderate_post(post_data: dict) -> dict:
    """
    Простейшая логика модерации.
    Если контент содержит стоп-слово (например, 'spam'), то статус меняется на DELCINE, иначе на APPROVE.
    """
    content = post_data.get('content', '').lower()
    # Пример стоп-слова
    stop_words = ['spam', 'ban', 'block']
    if any(word in content for word in stop_words):
        post_data['state'] = 'DELCINE'
    else:
        post_data['state'] = 'APPROVE'
    return post_data
