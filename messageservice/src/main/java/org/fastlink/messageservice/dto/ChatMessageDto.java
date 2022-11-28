package org.fastlink.messageservice.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto implements Serializable {

    private String senderId;
    private String recipientId;
    private String senderName;
    private String recipientName;
    private String content;
}
