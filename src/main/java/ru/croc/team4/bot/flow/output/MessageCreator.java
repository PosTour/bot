package ru.croc.team4.bot.flow.output;

import org.springframework.stereotype.Component;
import ru.croc.team4.bot.api.CinemaFeignClient;
import ru.croc.team4.bot.dto.Category;
import ru.croc.team4.bot.storage.ChatState;

@Component
public class MessageCreator {
    public StringBuffer getMoviesOutput(ChatState chatState) {
        var movies = chatState.getMovies();
        StringBuffer text = new StringBuffer();

        if (!movies.isEmpty()) {
            Integer i = 1;
            for (var movie : movies) {
                text.append(i)
                    .append(". Фильм:         ").append(movie.title())
                    .append("\n")
                    .append("    Длительность: ").append(movie.durationInMinutes()).append(" минут")
                    .append("\n")
                    .append("    Описание:     ").append(movie.description())
                    .append("\n");
                i++;
            }
        }

        return text;
    }

    public StringBuffer getSessionsOutput(ChatState chatState) {
        var sessions = chatState.getSessions();
        StringBuffer text = new StringBuffer();

        if (!sessions.isEmpty()) {
            Integer i = 1;
            for (var session : sessions) {
                if (!session.isDeleted()) {
                    text.append(i)
                        .append(". Начало сеанса: ").append(session.startDate())
                        .append("\n")
                        .append("                  ").append(session.startTime())
                        .append("\n    Цены:         ").append(session.prices().get(Category.BAD))
                        .append("\n                  ").append(session.prices().get(Category.GOOD))
                        .append("\n                  ").append(session.prices().get(Category.EXCELLENT))
                        .append("\n")
                        .append("    Зал:          ").append(session.hallName())
                        .append("\n\n");
                    i++;
                }
            }
        }

        return text;
    }

    public StringBuffer getRowsOutput(ChatState chatState) {
        var rows = chatState.getRows();
        StringBuffer text = new StringBuffer();

        if (!rows.isEmpty()) {
            for (var row : rows) {
                text.append(row.rowNumber())
                        .append("\n");
            }
        }

        return text;
    }

    public StringBuffer getPlacesOutput(ChatState chatState) {
        var places = chatState.getPlaces();
        StringBuffer text = new StringBuffer();

        if (!places.isEmpty()) {
            for (var place : places) {
                text.append(place.placeNumber())
                        .append(". Тип: ").append(place.type())
                        .append("   Статус: ").append(place.status())
                        .append("\n");
            }
        } else {
            text.append("В данном ряду нет свободных мест. Вернитесь к предыдущему шагу");
        }

        return text;
    }

    public StringBuffer getTicketsOutput(ChatState chatState, CinemaFeignClient feignClient) {
        var tickets = feignClient.getTicketsById(chatState.getChatId());
        StringBuffer text = new StringBuffer();

        if (!tickets.isEmpty()) {

            for (var ticket : tickets) {
                var session = feignClient.getSessionBy(ticket.sessionId());

                text.append("Код брони:     ").append(ticket.bookingCode()).append("\n")
                        .append("Статус:        ").append(ticket.status()).append("\n")
                        .append("Фильм:         ").append(feignClient.getMovieBy(session.movieId()).title()).append("\n")
                        .append("Начало сеанса: ").append(session.startDate()).append("\n")
                        .append("               ").append(session.startTime()).append("\n")
                        .append("Зал:           ").append(session.hallName()).append("\n")
                        .append("Ряд:           ").append(ticket.rowNumber()).append("\n")
                        .append("Место:         ").append(ticket.placeNumber()).append("\n\n");
            }
        } else {
            text.append("Вы еще не приобретали билеты");
        }

        return text;
    }
}
