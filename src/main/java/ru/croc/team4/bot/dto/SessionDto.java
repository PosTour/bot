package ru.croc.team4.bot.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

public record SessionDto(
        UUID id
        , UUID movieId
        , String hallName
        , LocalDate startDate
        , LocalTime startTime
        , Map<Category, Integer> prices
        , Boolean isDeleted) {
}
