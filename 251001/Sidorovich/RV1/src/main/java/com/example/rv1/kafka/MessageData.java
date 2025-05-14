package com.example.rv1.kafka;

import com.example.rv1.dto.requestDto.CommentRequestTo;
import com.example.rv1.dto.responseDto.CommentResponseTo;

import java.util.List;


    public record MessageData(
            Operation operation,
            Long itemId,
            CommentRequestTo requestTO,
            List<CommentResponseTo> responseTOs,
            ExceptionData exception
    )
    {
        public MessageData(Operation operation){
            this(operation, null, null, null, null);
        }
        public MessageData(Operation operation, Long itemId) {
            this(operation, itemId, null, null, null);
        }
        public MessageData(Operation operation, CommentRequestTo requestTO){
            this(operation, null, requestTO, null, null);
        }
        public MessageData(Operation operation, List<CommentResponseTo> responseTOs){
            this(operation, null, null, responseTOs, null);
        }
        public MessageData(ExceptionData exception){
            this(Operation.EXCEPTION, null, null, null, exception);
        }
        public enum Operation{
            GET_ALL,
            GET_BY_ID,
            CREATE,
            UPDATE,
            DELETE_BY_ID,
            EXCEPTION
        }
        public record ExceptionData(
                String simpleName,
                String message
        ){}
    }




