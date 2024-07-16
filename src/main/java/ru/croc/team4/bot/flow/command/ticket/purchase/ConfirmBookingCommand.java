package ru.croc.team4.bot.flow.command.ticket.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.api.CinemaFeignClient;
import ru.croc.team4.bot.dto.TicketCreationDto;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.command.Command;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmBookingCommand implements Command {
    public static final String NAME = "requestpayment";

    private final CinemaFeignClient feignClient;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "Бронировать";
    }

    @Override
    public List<Stage> getKnownStages() {
        return List.of(
                Stage.CONFIRM_TICKET
        );
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        sendTextMessage(chatState,
                "\uD83E\uDD73 Оплата прошла успешно!",
                sender);

        feignClient.createTicket(new TicketCreationDto(
                chatState.getChatId(),
                DateTimeFormatter.ofPattern("SSSSSS").format(Instant.now()),
                chatState.getSession(),
                chatState.getPlace()));

        chatState.resetRequest();
        chatState.resetMenuMessageId();
        Command.enterStage(Stage.AUTH_MAIN_MENU, chatState, sender);
    }
}
