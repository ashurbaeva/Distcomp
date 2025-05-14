package com.bsuir.dc.dto.kafka;

import com.bsuir.dc.dto.request.MessageRequestTo;

public class InTopicDTO {
    private String method;
    private MessageRequestTo messageRequestDTO;
    private String status;

    public InTopicDTO(String method, MessageRequestTo messageRequestDTO, String status) {
        this.method = method;
        this.messageRequestDTO = messageRequestDTO;
        this.status = status;
    }

    public InTopicDTO() {}

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public MessageRequestTo getMessageRequestDTO() { return messageRequestDTO; }
    public void setMessageRequestDTO(MessageRequestTo messageRequestDTO) { this.messageRequestDTO = messageRequestDTO; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}