package ru.croc.team4.bot.flow.stage;

import lombok.Getter;
import ru.croc.team4.bot.flow.command.ReturnToMenuCommand;
import ru.croc.team4.bot.flow.command.ReturnToPrevStageCommand;
import ru.croc.team4.bot.flow.command.HelpCommand;
import ru.croc.team4.bot.flow.command.registration.RegisterCommand;
import ru.croc.team4.bot.flow.command.registration.SendContactCommand;
import ru.croc.team4.bot.flow.command.ticket.purchase.*;
import ru.croc.team4.bot.flow.command.ticket.search.SearchTicketCommand;

public enum Stage {
    NOT_AUTHORIZED(
            "Добро пожаловать в бот кинотеатра \"Места для поцелуев\"",
            InputType.NONE,
            RegisterCommand.NAME,
            HelpCommand.NAME
    ),
    REG_NUMBER(
            "Для доступа к покупке билетов нужен ваш контакт.\nНажмите кнопку \"Мой контакт\" для отправки",
            InputType.CONTACT,
            SendContactCommand.NAME
    ),
    AUTH_MAIN_MENU(
            "Добро пожаловать в бот кинотеатра \"Места для поцелуев\"",
            InputType.NONE,
            CreateTicketCommand.NAME,
            SearchTicketCommand.NAME,
            HelpCommand.NAME
    ),
    ENTER_FILM(
            "Выберите фильм",
            InputType.TEXT,
            ReturnToPrevStageCommand.NAME
    ),
    ENTER_SESSION(
            "Выберите сеанс",
            InputType.TEXT,
            ReturnToPrevStageCommand.NAME
    ),
    ENTER_ROW(
            "Выберите ряд в зале",
            InputType.TEXT,
            ReturnToPrevStageCommand.NAME
    ),
    ENTER_PLACE(
            "Выберите место в ряду",
            InputType.TEXT,
            ReturnToPrevStageCommand.NAME
    ),
    CONFIRM_TICKET(
            "Подтвердите бронирование: ",
            InputType.NONE,
            ConfirmBookingCommand.NAME,
            ReturnToMenuCommand.NAME,
            ReturnToPrevStageCommand.NAME
    )
    ;

    @Getter
    private final String header;
    @Getter
    private final String[] buttons;
    @Getter
    private int maxButtonsInRow = 1;
    private final InputType waitInputType;

    Stage(String header, InputType waitInputType, String... buttons) {
        this.header = header;
        this.buttons = buttons;
        this.waitInputType = waitInputType;
    }

    Stage(String header, InputType waitInputType, int maxButtonsInRow, String... buttons) {
        this.header = header;
        this.buttons = buttons;
        this.maxButtonsInRow = maxButtonsInRow;
        this.waitInputType = waitInputType;
    }

    public InputType waitInputType() {
        return waitInputType;
    }
}
