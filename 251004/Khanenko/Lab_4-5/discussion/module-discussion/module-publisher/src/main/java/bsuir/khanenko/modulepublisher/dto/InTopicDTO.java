package bsuir.khanenko.modulepublisher.dto;

public class InTopicDTO {
    private String method;
    private MessageRequestTo messageRequestTo;
    private String status;

    public InTopicDTO(String method, MessageRequestTo messageRequestTo, String status) {
        this.method = method;
        this.messageRequestTo = messageRequestTo;
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public MessageRequestTo getMessageRequestTo() {
        return messageRequestTo;
    }

    public void setMessageRequestTo(MessageRequestTo messageRequestTo) {
        this.messageRequestTo = messageRequestTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InTopicDTO() {
    }
}
