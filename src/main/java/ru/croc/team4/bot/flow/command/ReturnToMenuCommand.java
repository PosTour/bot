package ru.croc.team4.bot.flow.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.List;

@Component
public class ReturnToMenuCommand implements Command {
    public static final String NAME = "returntomenu";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "\uD83D\uDEAB Вернуться в меню";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.AUTH_MAIN_MENU
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        chatState.resetRequest();
        chatState.resetMenuMessageId();
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
