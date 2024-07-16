package ru.croc.team4.bot.flow.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.List;

@Component
@Slf4j
public class HelpCommand implements Command {
    public static final String NAME = "showhelp";

    @Autowired
    private ResourceLoader resLoad;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "❓ Справка";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.AUTH_MAIN_MENU,
                Stage.NOT_AUTHORIZED
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        String regFlowInfo = chatState.isApproved() ?
            """
            Бот предоставляет возможность покупки билетов в наш кинотеатр "Места для поцелуев"
            
            Для выбора сеанса и места нажмите "Купить билет"
            
            Для просмотра билетов нажмите "Смотреть купленные билеты"
            """
                :
            """
            Бот предоставляет возможность покупки билетов в наш кинотеатр "Места для поцелуев"
            
            *Для входа или регистрации вам необходимо:*
            1. Нажать "Регистрация"
            2. Предоставить свой номер телефона.
            """;

        sendTextMessage(chatState, regFlowInfo, sender);

        Command.enterStage(chatState.getCurrentStage(), chatState, sender);
    }
}
