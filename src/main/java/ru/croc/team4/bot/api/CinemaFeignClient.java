package ru.croc.team4.bot.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.croc.team4.bot.dto.*;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "cinemaFeignClient", url = "${url.cinema}")
public interface CinemaFeignClient {
    @PostMapping(value = "/api/users/signup")
    UserDto signUp(UserDto userDto);

    @GetMapping(value = "/api/ticket/findByChatId/{chatId}")
    List<TicketOutputDto> getTicketsById(@PathVariable ("chatId") long chatId);

    @PostMapping(value = "/api/ticket")
    TicketCreationDto createTicket(TicketCreationDto ticketDto);

    @GetMapping(value = "/api/movie/{movieId}")
    MovieDto getMovieBy(@PathVariable ("movieId") UUID movieId);

    @GetMapping(value = "/api/movie/all")
    List<MovieDto> getMovies();

    @GetMapping(value = "/api/session/findById/{sessionId}")
    SessionDto getSessionBy(@PathVariable ("sessionId") UUID sessionId);

    @GetMapping(value = "/api/session/findByMovieId/{movieId}")
    List<SessionDto> getSessionsBy(@PathVariable ("movieId") UUID movieId);

    @GetMapping(value = "/api/row/{sessionId}")
    List<RowDto> getRowsBy(@PathVariable ("sessionId") UUID sessionId);

    @GetMapping(value = "/api/place/{rowId}")
    List<PlaceDto> getPlacesBy(@PathVariable ("rowId") UUID rowId);
}
