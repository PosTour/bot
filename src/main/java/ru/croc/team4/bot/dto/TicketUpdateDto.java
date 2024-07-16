package ru.croc.team4.bot.dto;

public record TicketUpdateDto(String bookingCode,
                              long chatId,
                              String newStatus) {
}
