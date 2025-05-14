package com.bsuir.discussion.dto.kafka;

import com.bsuir.discussion.dto.requests.MessageRequestDTO;

public class InTopicDTO {
    private String method;
    private MessageRequestDTO messageRequestDTO;
    private String status;

    public InTopicDTO(String method, MessageRequestDTO messageRequestDTO, String status) {
        this.method = method;
        this.messageRequestDTO = messageRequestDTO;
        this.status = status;
    }

    public InTopicDTO() {}

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public MessageRequestDTO getMessageRequestDTO() { return messageRequestDTO; }
    public void setMessageRequestDTO(MessageRequestDTO messageRequestDTO) { this.messageRequestDTO = messageRequestDTO; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
