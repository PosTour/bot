package ru.croc.team4.bot.flow.command.ticket;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class Ticket {
    private String movieName;

    private LocalTime startTime;

    private LocalDate startDate;

    private String hallName;

    private int rowNumber;

    private int placeNumber;
}
