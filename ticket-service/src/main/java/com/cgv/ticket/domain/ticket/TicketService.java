package com.cgv.ticket.domain.ticket;


import com.cgv.ticket.domain.ticket.dto.TicketRes;
import com.cgv.ticket.global.common.StatusCode;
import com.cgv.ticket.global.exception.CustomException;
import com.cgv.ticket.global.kafka.TicketEventProducer;
import com.cgv.ticket.global.kafka.event.ticket.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketEventProducer ticketEventProducer;

    public List<TicketRes> getTicketList(String userName){
        List<Ticket> Tickets = ticketRepository.findAllByUserName(userName);

        return Tickets.stream()
                .map(TicketRes::from)
                .toList();
    }
    @Transactional
    public void createTicket(String userName, Long seatId){
        Ticket ticket = Ticket.builder()
                .userName(userName)
                .status(Status.PENDING)
                .seatId(seatId)
                .build();

        // 티켓 생성
        ticketRepository.save(ticket);

        // 티켓 생성 이벤트 생성
        TicketCreatedEvent ticketCreatedEvent= TicketCreatedEvent.builder()
                .ticketId(ticket.getId())
                .userName(ticket.getUserName())
                .seatId(seatId)
                .build();

        ticketEventProducer.sendTicketCreatedEvent(ticketCreatedEvent);

    }

    @Transactional
    public void cancelTicket(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CustomException(StatusCode.TICKET_NOT_EXIST));

        if(ticket.getStatus()==Status.AVAILABLE)
            ticket.changeStatusCancel();
        else throw new CustomException(StatusCode.TICKET_UNAVAILABLE_CANCEL);
    }

    @Transactional
    public void awaitingPaymentTicket(Long ticketId){
        try{
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new CustomException(StatusCode.TICKET_NOT_EXIST));

            if(ticket.getStatus()==Status.PENDING)
                ticket.changeStatusAwaitingPayment();
            else log.error("awaitingPaymentTicket 호출: 티켓 상태가 PENDING이 아닙니다. ticketId={}, status={}", ticketId, ticket.getStatus());
        }
        catch (CustomException e) {
            log.error("awaitingpaymentTicket 처리 중 예외 발생: {}", e.getMessage());
        }


    }

    @Transactional
    public void failTicket(Long ticketId){
        try{
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new CustomException(StatusCode.TICKET_NOT_EXIST));

            if(ticket.getStatus()==Status.PENDING)
                ticket.changeStatusFailed();
            else log.error("failTicket 호출: 티켓 상태가 PENDING이 아닙니다. ticketId={}, status={}", ticketId, ticket.getStatus());
        }
        catch (CustomException e) {
            log.error("failTicket 처리 중 예외 발생: {}", e.getMessage());
        }
    }

    @Transactional
    public void expireTicket(Long ticketId){
        try{
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new CustomException(StatusCode.TICKET_NOT_EXIST));

            if(ticket.getStatus()==Status.PENDING || ticket.getStatus()==Status.AWAITING_PAYMENT)
                ticket.changeStatusFailed();
            else log.error("expireTicket 호출: 티켓 상태가 PENDING 또는 AWAITING_PAYMENT이 아닙니다. ticketId={}, status={}", ticketId, ticket.getStatus());
        }
        catch (CustomException e) {
            log.error("expireTicket 처리 중 예외 발생: {}", e.getMessage());
        }

    }

}
