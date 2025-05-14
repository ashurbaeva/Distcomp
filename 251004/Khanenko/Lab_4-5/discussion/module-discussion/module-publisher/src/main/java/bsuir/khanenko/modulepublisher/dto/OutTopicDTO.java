package bsuir.khanenko.modulepublisher.dto;

import java.util.List;

public class OutTopicDTO {
    private MessageResponseTo messageResponseTo;
    private List<MessageResponseTo> messageResponseTos;
    private String status;
    private String error;

    public MessageResponseTo getMessageResponseTo() {
        return messageResponseTo;
    }

    public void setMessageResponseTo(MessageResponseTo messageResponseTo) {
        this.messageResponseTo = messageResponseTo;
    }

    public List<MessageResponseTo> getMessageResponseTos() {
        return messageResponseTos;
    }

    public void setMessageResponseTos(List<MessageResponseTo> messageResponseTos) {
        this.messageResponseTos = messageResponseTos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OutTopicDTO(MessageResponseTo messageResponseTo, String status) {
        this.messageResponseTo = messageResponseTo;
        this.status = status;
    }

    public OutTopicDTO(List<MessageResponseTo> messageResponseTos, String status) {
        this.messageResponseTos = messageResponseTos;
        this.status = status;
    }

    public OutTopicDTO(String s, String decline) {
    }

    public OutTopicDTO(MessageResponseTo messageResponseTo, List<MessageResponseTo> messageResponseTos, String status, String error) {
        this.messageResponseTo = messageResponseTo;
        this.messageResponseTos = messageResponseTos;
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(MessageResponseTo messageResponseTo) {
        this.messageResponseTo = messageResponseTo;
    }

    public OutTopicDTO(List<MessageResponseTo> messageResponseTos) {
        this.messageResponseTos = messageResponseTos;
    }

    public OutTopicDTO(String status) {
        this.status = status;
    }
}