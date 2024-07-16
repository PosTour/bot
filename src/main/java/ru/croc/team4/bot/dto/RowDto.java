package ru.croc.team4.bot.dto;

import java.util.UUID;

public record RowDto(UUID id,
                     Integer rowNumber,
                     UUID SessionId) {
}
