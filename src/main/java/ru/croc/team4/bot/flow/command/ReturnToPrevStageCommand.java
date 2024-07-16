package ru.croc.team4.bot.flow.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.List;

@Component
public class ReturnToPrevStageCommand implements Command {
    public static final String NAME = "returntoprevstage";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "⏪ Вернуться";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.REG_NUMBER,
                Stage.AUTH_MAIN_MENU,
                Stage.ENTER_FILM,
                Stage.ENTER_SESSION,
                Stage.ENTER_ROW,
                Stage.ENTER_PLACE,
                Stage.CONFIRM_TICKET
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        switch (chatState.getCurrentStage()) {
            case ENTER_FILM -> {
                chatState.resetMenuMessageId();
                Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
            }
            case ENTER_SESSION -> {
                chatState.resetMenuMessageId();
                Command.enterStage(Stage.ENTER_FILM, chatState, sender);
            }
            case ENTER_ROW -> {
                chatState.resetMenuMessageId();
                Command.enterStage(Stage.ENTER_SESSION, chatState, sender);
            }
            case ENTER_PLACE -> {
                chatState.resetMenuMessageId();
                Command.enterStage(Stage.ENTER_ROW, chatState, sender);
            }
            case CONFIRM_TICKET -> {
                chatState.resetMenuMessageId();
                Command.enterStage(Stage.ENTER_PLACE, chatState, sender);
            }
        }
    }
}
