package com.cgv.movie.global.redis;

import com.cgv.movie.domain.seat.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final SeatService seatService;

    // Redis Key 생성/만료 이벤트를 수신
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, SeatService seatService) {
        super(listenerContainer);
        this.seatService = seatService;
    }

    /**
     * 만료된 키에 대한 처리를 수행합니다.
     *
     * @param message redis key
     * @param pattern __keyevent@*__:expired
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Redis 만료 키: {}", expiredKey);

        if (expiredKey.startsWith("seat:")) {
            String seatIdString = expiredKey.split(":")[1];
            Long seatId = Long.parseLong(seatIdString);

            seatService.unLockSeat(seatId);
        }

    }
}
