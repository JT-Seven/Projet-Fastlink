package org.fastlink.messageservice.mapper;

import org.fastlink.messageservice.dto.ChatMessageDto;
import org.fastlink.messageservice.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ChatMessageMapper {

    public ChatMessage convertDtoToEntity(ChatMessageDto chatMessageDto, String chatId) {
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setChatId(chatId);
        chatMessage.setContent(chatMessageDto.getContent());
        chatMessage.setSenderName(chatMessageDto.getSenderName());
        chatMessage.setSenderId(chatMessageDto.getSenderId());
        chatMessage.setRecipientName(chatMessageDto.getRecipientName());
        chatMessage.setRecipientId(chatMessageDto.getRecipientId());
        chatMessage.setTimestamp(new Date());

        return chatMessage;
    }
}
