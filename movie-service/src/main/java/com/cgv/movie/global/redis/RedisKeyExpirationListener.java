package com.cgv.movie.global.redis;

import com.cgv.movie.domain.seat.Seat;
import com.cgv.movie.domain.seat.SeatRepository;
import com.cgv.movie.domain.seat.Status;
import com.cgv.movie.global.common.StatusCode;
import com.cgv.movie.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final SeatRepository seatRepository;

    // Redis Key 생성/만료 이벤트를 수신
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, SeatRepository seatRepository) {
        super(listenerContainer);
        this.seatRepository = seatRepository;
    }

    /**
     * 만료된 키에 대한 처리를 수행합니다.
     *
     * @param message redis key
     * @param pattern __keyevent@*__:expired
     */
    @Transactional
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("좌석 선점 ID: {}", expiredKey);

        if (expiredKey.startsWith("seat:")) {
            String seatIdString = expiredKey.split(":")[1];
            Long seatId = Long.parseLong(seatIdString);

            Seat seat=seatRepository.findById(seatId)
                    .orElseThrow(() -> new CustomException(StatusCode.SEAT_NOT_EXIST));

            if(seat.getStatus() == Status.LOCKED){
                seat.changeStatusAvailable();
                log.info("{}번 좌석 자동 락 해제 처리", seatId);
            }
        }

    }
}
