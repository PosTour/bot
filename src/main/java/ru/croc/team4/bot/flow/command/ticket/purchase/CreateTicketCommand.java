package ru.croc.team4.bot.flow.command.ticket.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.api.CinemaFeignClient;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.command.Command;
import ru.croc.team4.bot.flow.command.ticket.Ticket;
import ru.croc.team4.bot.flow.output.MessageCreator;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateTicketCommand implements Command {
    public static final String NAME = "createticket";

    private final CinemaFeignClient feignClient;
    private final MessageCreator messageCreator;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "\uD83C\uDFAC Купить билет";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.AUTH_MAIN_MENU
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        chatState.setTicket(new Ticket());

        chatState.setMovies(feignClient.getMovies());

        if (!chatState.getMovies().isEmpty()) {
            sendTextMessage(chatState,
                    "*Вводите номер интересующего варианта*\nФильмы: \n" + messageCreator.getMoviesOutput(chatState),
                    sender);
        } else {
            sendTextMessage(chatState,
                    "В данный момент в прокате нет фильмов",
                    sender);
        }

        chatState.resetMenuMessageId();
        Command.enterStage(Stage.ENTER_FILM, chatState, sender);
    }
}
