package ru.croc.team4.bot.dto;

import java.util.UUID;

public record PlaceDto(UUID id,
                       String status,
                       String type,
                       Integer placeNumber,
                       UUID rowId) {
}

