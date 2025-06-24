package com.petweb.sponge.chat;

import com.petweb.sponge.exception.error.NotFoundToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

//    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.SEND) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            if (!this.validateAccessToken(accessToken)) {
                throw new NotFoundToken();
            }
//            Long id = jwtUtil.getId(accessToken);
//            String loginType = jwtUtil.getLoginType(accessToken);

            // 헤더에 사용자 정보 추가
//            accessor.setNativeHeader("id", String.valueOf(id));
//            accessor.setNativeHeader("loginType", loginType);
        }


        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

    private boolean validateAccessToken(String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            log.error("토큰 없음");
            return false;
        }
        String token = accessToken.trim();
//        try {
////            jwtUtil.isExpired(token);
//            return true;
//        } catch (ExpiredJwtException | SignatureException e) {
//            log.error("토큰 검증 실패: {}", e.getMessage());
//            return false;
//        }
//    }
        return false;
    }
}
