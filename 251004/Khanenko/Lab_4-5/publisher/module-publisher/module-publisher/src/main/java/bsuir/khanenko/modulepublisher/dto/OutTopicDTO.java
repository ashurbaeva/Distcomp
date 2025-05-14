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

    public OutTopicDTO() {
    }
}
