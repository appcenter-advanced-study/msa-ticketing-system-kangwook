package com.cgv.movie;


import com.cgv.reservation.domain.reservation.ReservationRepository;
import com.cgv.movie.domain.seat.Seat;
import com.cgv.movie.domain.seat.SeatRepository;
import com.cgv.movie.domain.seat.SeatService;
import com.cgv.movie.domain.seat.dto.SeatReq;
import com.cgv.reservation.global.exception.CustomException;
import com.cgv.reservation.global.kafka.ReservationConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("mirror")
@EmbeddedKafka(partitions = 1, topics = {"reservation-1"})
public class KafkaTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationProducer reservationProducer;

    @Autowired
    private ReservationConsumer reservationConsumer;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SeatRepository seatRepository;


    private Long seatId;


    @BeforeEach
    void setUp() {
        // 기존 데이터 정리
        reservationRepository.deleteAll();
        seatRepository.deleteAll();

        seatService.createSeat(1L,new SeatReq(3,3));

        seatId=seatRepository.findBySchedule_IdAndRowIndexAndColumnIndex(1L,1L,1L).get().getId();


    }


    @Test
    @DisplayName("카프카 예매 동시성 제어 테스트")
    void testConcurrentReservation() throws InterruptedException {
        int numberOfThreads = 1000; // 요청 수
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 동시 처리할 스레드 풀
        CountDownLatch latch = new CountDownLatch(numberOfThreads); // 모든 요청이 끝날 때까지 기다리기 위한 CountDownLatch
        AtomicInteger successCount = new AtomicInteger(0); // 성공한 예약 수
        List<Exception> exceptions = new ArrayList<>(); // 예외를 담을 리스트
        List<Long> responseTimes = new ArrayList<>(); // 각 요청의 응답 시간을 담을 리스트

        // Kafka Consumer에 latch, successCount, exceptionList 주입
        reservationConsumer.setLatch(latch);
        reservationConsumer.setSuccessCount(successCount);
        reservationConsumer.setExceptionList(exceptions);

        // 테스트 시작 시간 기록
        long testStartTime = System.nanoTime();

        for (int i = 0; i < numberOfThreads; i++) {
            final String username = "user" + i; // 각 사용자마다 고유 이름 생성

            executorService.submit(() -> {
                long startTime = System.nanoTime(); // 시작 시간 기록
                try {
                    // 예약 요청을 보내고 성공하면 카운트 증가
                    reservationProducer.sendReservationRequest(username,seatId,1L);
                } catch (CustomException e) {
                    synchronized (exceptions) {
                        exceptions.add(e); // 사용자 정의 예외 처리
                    }
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e); // 기타 예외 처리
                    }
                } finally {
                    // 응답 시간 계산
                    long endTime = System.nanoTime(); // 종료 시간 기록
                    long responseTime = endTime - startTime; // 응답 시간 계산
                    synchronized (responseTimes) {
                        responseTimes.add(responseTime); // 응답 시간 리스트에 추가
                    }
                }
            });
        }

        latch.await(); // 모든 요청이 완료될 때까지 대기
        executorService.shutdown(); // Executor 서비스 종료

        // 테스트 종료 시간 기록
        long testEndTime = System.nanoTime();

        // 전체 테스트 시간 계산 (단위: 밀리초)
        long totalTestTimeMillis = (testEndTime - testStartTime) / 1_000_000; // nano -> milliseconds


        // 예약된 좌석 정보 확인
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // 예외 종류 출력
        exceptions.forEach(ex -> System.out.println("예외 종류: " + ex.getClass().getSimpleName()));

        // 테스트 결과 출력
        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("좌석 예약 여부: " + seat.getIsReserved());
        System.out.println("발생한 예외 수: " + exceptions.size());


        // 응답 시간 계산
        List<Long> responseTimeList = new ArrayList<>(responseTimes);
        long fastestResponse = responseTimeList.stream().min(Long::compare).orElse(0L) / 1_000_000; // nanoseconds -> milliseconds
        long slowestResponse = responseTimeList.stream().max(Long::compare).orElse(0L) / 1_000_000; // nanoseconds -> milliseconds
        double averageResponse = responseTimeList.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0; // nanoseconds -> milliseconds


        // 응답 시간 관련 정보 출력
        System.out.println("최단 응답 시간: " + fastestResponse + "ms");
        System.out.println("최장 응답 시간: " + slowestResponse + "ms");
        System.out.println("평균 응답 시간: " + averageResponse + "ms");
        // 전체 테스트 시간 출력
        System.out.println("전체 테스트 시간: " + totalTestTimeMillis + "ms");

        // 최종 검증: 예약이 되어 있어야 하며, 성공한 예약은 1건만 있어야 하고, 나머지는 예외가 발생해야 합니다.
        assertTrue(seat.getIsReserved(), "좌석은 예약 상태여야 합니다.");
        assertEquals(1, successCount.get(), "동시에 하나만 성공해야 합니다.");
        assertEquals(numberOfThreads - 1, exceptions.size(), "나머지는 예외가 발생해야 합니다.");
    }
}
