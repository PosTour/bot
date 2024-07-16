package ru.croc.team4.bot.flow.command.ticket.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.api.CinemaFeignClient;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.command.Command;
import ru.croc.team4.bot.flow.output.MessageCreator;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchTicketCommand implements Command {

    public static final String NAME = "searchtickets";
    private final CinemaFeignClient feignClient;
    private final MessageCreator messageCreator;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "\uD83D\uDD0E Смотреть купленные билеты";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.AUTH_MAIN_MENU
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        sendTextMessage(chatState,
                "" + messageCreator.getTicketsOutput(chatState, feignClient),
                sender);

        chatState.resetMenuMessageId();
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
