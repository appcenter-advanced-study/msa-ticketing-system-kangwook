package com.cgv.ticket.domain.ticket;


import com.cgv.ticket.domain.ticket.dto.TicketRes;
import com.cgv.ticket.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cgv.ticket.global.common.StatusCode.*;

@Tag(name = "[티켓]", description = "티켓 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "티켓 조회", description = "사용자의 모든 티켓을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TicketRes>>> getTicketList(
            @RequestParam String userName) {
        return ResponseEntity
                .status(TICKET_FOUND.getStatus())
                .body(CommonResponse.from(TICKET_FOUND.getMessage(),
                        ticketService.getTicketList(userName)));
    }

    @Operation(summary = "티켓 생성", description = "영화 티켓을 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<Object>> createTicket(
            @RequestParam String userName,
            @RequestParam Long seatId) {
        ticketService.createTicket(userName,seatId);
        return ResponseEntity
                .status(TICKET_CREATE_REQUEST.getStatus())
                .body(CommonResponse.from(TICKET_CREATE_REQUEST.getMessage()));
    }

    @Operation(summary = "예매 취소", description = "해당 예매를 취소합니다.")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Object> cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return ResponseEntity
                .status(TICKET_CANCEL.getStatus())
                .body(CommonResponse.from(TICKET_CANCEL.getMessage()));
    }

}
