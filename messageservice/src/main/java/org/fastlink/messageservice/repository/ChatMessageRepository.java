package org.fastlink.messageservice.repository;

import org.fastlink.messageservice.model.ChatMessage;
import org.fastlink.messageservice.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);
    List<ChatMessage> findByChatId(String chatId);
    Optional<ChatMessage> findFirstByChatIdOrderByTimestampDesc(String chatId);
}