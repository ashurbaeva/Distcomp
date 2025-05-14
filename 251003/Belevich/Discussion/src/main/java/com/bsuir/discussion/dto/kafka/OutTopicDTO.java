package com.bsuir.discussion.dto.kafka;

import com.bsuir.discussion.dto.responses.MessageResponseDTO;

import java.util.List;

public class OutTopicDTO {
    private MessageResponseDTO messageResponseDTO;
    private List<MessageResponseDTO> messageResponsesListDTO;
    private String status;
    private String error;

    public OutTopicDTO(MessageResponseDTO messageResponseDTO, String status) {
        this.messageResponseDTO = messageResponseDTO;
        this.status = status;
    }

    public OutTopicDTO(List<MessageResponseDTO> messageResponsesListDTO, String status) {
        this.status = status;
        this.messageResponsesListDTO = messageResponsesListDTO;
    }

    public OutTopicDTO( String error, String status) {
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO() {}

    public MessageResponseDTO getMessageResponseDTO() { return messageResponseDTO; }
    public void setMessageResponseDTO(MessageResponseDTO messageResponseDTO) {this.messageResponseDTO = messageResponseDTO; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public List<MessageResponseDTO> getMessageResponsesListDTO() { return messageResponsesListDTO; }
    public void setMessageResponsesListDTO(List<MessageResponseDTO> messageResponsesListDTO) {
        this.messageResponsesListDTO = messageResponsesListDTO;
    }
}
