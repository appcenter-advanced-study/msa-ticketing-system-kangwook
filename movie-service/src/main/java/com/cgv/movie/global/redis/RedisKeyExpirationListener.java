package com.cgv.movie.global.redis;

import com.cgv.movie.domain.seat.SeatService;
import com.cgv.movie.global.kafka.SeatEventProducer;
import com.cgv.movie.global.kafka.event.seat.SeatExpiredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final SeatService seatService;
    private final SeatEventProducer seatEventProducer;

    // Redis Key 생성/만료 이벤트를 수신
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, SeatService seatService, SeatEventProducer seatEventProducer) {
        super(listenerContainer);
        this.seatService = seatService;
        this.seatEventProducer = seatEventProducer;
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
            String[] parts = expiredKey.split(":");
            Long seatId = Long.parseLong(parts[1]);
            Long ticketId = Long.parseLong(parts[3]);

            boolean unlocked = seatService.unLockSeat(seatId);

            if(unlocked){
                SeatExpiredEvent seatExpiredEvent= SeatExpiredEvent.builder()
                        .ticketId(ticketId)
                        .build();

                seatEventProducer.sendSeatExpiredEvent(seatExpiredEvent);
            }
        }
    }
}
