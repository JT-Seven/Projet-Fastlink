package org.fastlink.messageservice.controller;

import lombok.AllArgsConstructor;
import org.fastlink.messageservice.dto.ChatMessageDto;
import org.fastlink.messageservice.mapper.ChatMessageMapper;
import org.fastlink.messageservice.model.ChatMessage;
import org.fastlink.messageservice.model.ChatNotification;
import org.fastlink.messageservice.service.ChatMessageService;
import org.fastlink.messageservice.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller @AllArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    private final ChatMessageMapper chatMessageMapper;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        var chatId = chatRoomService.getChatId(chatMessageDto.getSenderId(), chatMessageDto.getRecipientId(), true).orElseThrow();

        ChatMessage chatMessage = chatMessageMapper.convertDtoToEntity(chatMessageDto, chatId);

        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessageDto.getRecipientId(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<ChatMessage> findMessage(@PathVariable String id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @GetMapping("/messages/contacts/{userId}")
    public ResponseEntity<List<String>> findContacts(@PathVariable String userId) {
        return ResponseEntity.ok(chatRoomService.findUserContacts(userId));
    }
}
