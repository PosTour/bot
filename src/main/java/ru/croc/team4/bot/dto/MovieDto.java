package ru.croc.team4.bot.dto;

import java.util.UUID;

public record MovieDto(UUID id,
                       String title,
                       Integer durationInMinutes,
                       String description) {
}
