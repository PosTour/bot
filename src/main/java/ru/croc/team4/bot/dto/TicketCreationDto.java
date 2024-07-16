package ru.croc.team4.bot.dto;

import java.util.UUID;

public record TicketCreationDto(long chatId,
                                String bookingCode,
                                UUID sessionId,
                                UUID placeId) {
}