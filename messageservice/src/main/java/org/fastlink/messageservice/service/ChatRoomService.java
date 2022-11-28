package org.fastlink.messageservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.messageservice.model.ChatRoom;
import org.fastlink.messageservice.repository.ChatMessageRepository;
import org.fastlink.messageservice.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    public List<String> findUserContacts(String senderId) {
        List<ChatRoom> chatRooms = this.chatRoomRepository.findChatRoomsBySenderId(senderId);
        return chatRooms
                .stream()
                .sorted((room1, room2) -> this.findChatsLatestMessageDate(room1).compareTo(this.findChatsLatestMessageDate(room2)))
                .map(ChatRoom::getRecipientId)
                .collect(Collectors.toList());
    }

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

         return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                 .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                     var chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public Date findChatsLatestMessageDate(ChatRoom chat) {
        return this.chatMessageRepository.findFirstByChatIdOrderByTimestampDesc(chat.getChatId()).get().getTimestamp();
    }
}
