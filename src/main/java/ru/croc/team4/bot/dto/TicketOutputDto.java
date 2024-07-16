package ru.croc.team4.bot.dto;

import java.util.UUID;

public record TicketOutputDto(String bookingCode,
                              String status,
                              UUID sessionId,
                              Integer rowNumber,
                              Integer placeNumber) {
}