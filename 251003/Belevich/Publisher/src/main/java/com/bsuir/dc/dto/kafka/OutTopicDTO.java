package com.bsuir.dc.dto.kafka;

import com.bsuir.dc.dto.response.MessageResponseTo;

import java.util.List;

public class OutTopicDTO {
    private MessageResponseTo messageResponseDTO;
    private List<MessageResponseTo> messageResponsesListDTO;
    private String status;
    private String error;

    public MessageResponseTo getMessageResponseDTO() { return messageResponseDTO; }
    public void setMessageResponseDTO(MessageResponseTo messageResponseDTO) { this.messageResponseDTO = messageResponseDTO;}

    public List<MessageResponseTo> getMessageResponsesListDTO() {return messageResponsesListDTO; }
    public void setMessageResponsesListDTO(List<MessageResponseTo> messageResponsesListDTO) {
        this.messageResponsesListDTO = messageResponsesListDTO;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public OutTopicDTO() {}
}