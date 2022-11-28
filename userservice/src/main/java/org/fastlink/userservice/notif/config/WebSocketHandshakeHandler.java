package org.fastlink.userservice.notif.config;

import lombok.AllArgsConstructor;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.service.FastlinkUserService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.LinkedList;
import java.util.Map;

@AllArgsConstructor
@Component
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler
{

    private final FastlinkUserService userService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes)
    {

        Message<?> message = (Message<?>) attributes.get("simpMessage");
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var raw = message
                    .getHeaders()
                    .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                var username = ((Map<?,?>) raw).get("username");
                String name = ((LinkedList<?>) username).get(0).toString();
                WebSocketUser user = new WebSocketUser(name);
                accessor.setUser(user);
                return user;
            }

        }
        return null;
    }
}
