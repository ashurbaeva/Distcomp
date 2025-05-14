package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class DiscussionClient {

    private final RestClient restClient;

    public DiscussionClient(RestClient restClient) {
        this.restClient = restClient;
    }

    // Получение всех сообщений
    public List<MessageResponseTo> getAllMessages() {
        return restClient.get()
                .uri("/api/v1.0/messages")
                .retrieve()
                .body(List.class); // Spring автоматически преобразует JSON в List<MessageResponseTo>
    }

    // Создание нового сообщения
    public MessageResponseTo createMessage(MessageRequestTo requestTo) {
        return restClient.post()
                .uri("/api/v1.0/messages")
                .body(requestTo)
                .retrieve()
                .body(MessageResponseTo.class);
    }

    // Получение сообщения по ID
    public MessageResponseTo getMessageById(Long id) {
        return restClient.get()
                .uri("/api/v1.0/messages/{id}", id)
                .retrieve()
                .body(MessageResponseTo.class);
    }

    // Обновление сообщения
    public MessageResponseTo updateMessage(MessageUpdate messageUpdate) {
        return restClient.put()
                .uri("/api/v1.0/messages")
                .body(messageUpdate)
                .retrieve()
                .body(MessageResponseTo.class);
    }

    // Удаление сообщения
    public void deleteMessage(Long id) {
        restClient.delete()
                .uri("/api/v1.0/messages/{id}", id)
                .retrieve()
                .toBodilessEntity(); // Возвращаем пустой ответ
    }
}
