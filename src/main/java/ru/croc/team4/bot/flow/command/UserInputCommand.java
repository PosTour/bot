package ru.croc.team4.bot.flow.command;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.croc.team4.bot.api.CinemaFeignClient;
import ru.croc.team4.bot.dto.UserDto;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.command.registration.SendContactCommand;
import ru.croc.team4.bot.flow.output.MessageCreator;
import ru.croc.team4.bot.flow.stage.Stage;
import ru.croc.team4.bot.storage.ChatState;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserInputCommand implements Command {

    public static final String NAME = "###userinput$$$";
    private final CinemaFeignClient feignClient;
    private final MessageCreator messageCreator;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isApplicable(String textMessage, ChatState chatState) {
        return getName().equals(textMessage);
    }

    @Override
    public void acceptMessage(List<String> entries, ChatState chatState, EntryBot sender) throws TelegramApiException {
        chatState.resetMenuMessageId();

        String entry = entries.isEmpty() ? null : entries.getFirst();
        switch (chatState.getCurrentStage()) {
            case REG_NUMBER -> {
                String phoneNumber = normalizePhoneNumber(entry);
                if (phoneNumber != null) {
                    chatState.setPhone(phoneNumber);
                    sendTextMessage(chatState, "Номер принят", sender);

                    feignClient.signUp(new UserDto(chatState.getChatId(), phoneNumber));
                    chatState.setApproved(true);

                    Command.enterStage(chatState.isApproved() ?
                            Stage.AUTH_MAIN_MENU : Stage.NOT_AUTHORIZED, chatState, sender);
                } else {
                    sendValidationMessage(
                            chatState,
                            "Ввести номер телефона можно только через кнопку " + (new SendContactCommand().getLabel()) + ". Попробуйте еще раз",
                            sender
                    );
                    reenterStage(chatState, sender);
                }
            }
            case ENTER_FILM -> {
                boolean isNumber;
                try {
                    assert entry != null;
                    Integer.parseInt(entry);
                    isNumber = true;
                } catch (NumberFormatException e) {
                    isNumber = false;
                }
                if (Integer.parseInt(entry) > 0 &&
                    Integer.parseInt(entry) < chatState.getMovies().size() + 1 &&
                    isNumber) {

                    var movie = chatState.getMovies()
                                .get(Integer.parseInt(entry) - 1);

                    chatState.setMovie(movie.id());

                    var ticket = chatState.getTicket();
                    ticket.setMovieName(movie.title());
                    chatState.setTicket(ticket);

                    chatState.setSessions(feignClient.getSessionsBy(movie.id()));

                    sendTextMessage(chatState,
                            "Сеансы: \n" + messageCreator.getSessionsOutput(chatState),
                            sender);

                    Command.enterStage(Stage.ENTER_SESSION, chatState, sender);
                } else {
                    sendValidationMessage(
                            chatState,
                            "Нужно выбрать вариант из предложенных. Попробуйте еще раз",
                            sender
                    );
                    reenterStage(chatState, sender);
                }
            }
            case ENTER_SESSION -> {
                boolean isNumber;
                try {
                    assert entry != null;
                    Integer.parseInt(entry);
                    isNumber = true;
                } catch (NumberFormatException e) {
                    isNumber = false;
                }
                if (Integer.parseInt(entry) > 0 &&
                    Integer.parseInt(entry) < chatState.getSessions().size() + 1 &&
                    isNumber) {

                    var session = chatState.getSessions()
                                    .get(Integer.parseInt(entry) - 1);

                    chatState.setSession(session.id());

                    var ticket = chatState.getTicket();
                    ticket.setStartTime(session.startTime());
                    ticket.setStartDate(session.startDate());
                    ticket.setHallName(session.hallName());
                    chatState.setTicket(ticket);

                    chatState.setRows(feignClient.getRowsBy(session.id()));


                    sendTextMessage(chatState,
                            "Ряды: \n" + messageCreator.getRowsOutput(chatState),
                            sender);

                    Command.enterStage(Stage.ENTER_ROW, chatState, sender);
                } else {
                    sendValidationMessage(
                            chatState,
                            "Нужно выбрать вариант из предложенных. Попробуйте еще раз",
                            sender
                    );
                    reenterStage(chatState, sender);
                }
            }
            case ENTER_ROW -> {
                boolean isNumber;
                try {
                    assert entry != null;
                    Integer.parseInt(entry);
                    isNumber = true;
                } catch (NumberFormatException e) {
                    isNumber = false;
                }
                if (Integer.parseInt(entry) > 0 &&
                    Integer.parseInt(entry) < chatState.getRows().size() + 1 &&
                    isNumber) {


                    var row = chatState.getRows()
                                .get(Integer.parseInt(entry) - 1);

                    chatState.setRow(row.id());

                    var ticket = chatState.getTicket();
                    ticket.setRowNumber(row.rowNumber());
                    chatState.setTicket(ticket);

                    chatState.setPlaces(feignClient.getPlacesBy(row.id()));

                    sendTextMessage(chatState,
                            "Места: \n" + messageCreator.getPlacesOutput(chatState),
                            sender);

                    Command.enterStage(Stage.ENTER_PLACE, chatState, sender);
                } else {
                    sendValidationMessage(
                            chatState,
                            "Нужно выбрать вариант из предложенных. Попробуйте еще раз",
                            sender
                    );
                    reenterStage(chatState, sender);
                }
            }
            case ENTER_PLACE -> {
                boolean isNumber;
                try {
                    assert entry != null;
                    Integer.parseInt(entry);
                    isNumber = true;
                } catch (NumberFormatException e) {
                    isNumber = false;
                }
                if (Integer.parseInt(entry) > 0 &&
                    Integer.parseInt(entry) < chatState.getPlaces().size() + 1 &&
                    isNumber) {

                    var place = chatState.getPlaces()
                            .get(Integer.parseInt(entry) - 1);

                    chatState.setPlace(place.id());

                    var ticket = chatState.getTicket();
                    ticket.setPlaceNumber(place.placeNumber());
                    chatState.setTicket(ticket);

                    sendTextMessage(chatState,
                            ticket.getMovieName() + "\n"
                            + ticket.getStartTime().toString()  + "\n"
                            + "Место: " + ticket.getHallName() + "\n"
                            + "Ряд: " + ticket.getRowNumber() + "\n"
                            + "Место" + ticket.getPlaceNumber() + "\n",
                            sender);


                    Command.enterStage(Stage.CONFIRM_TICKET, chatState, sender);
                } else {
                    sendValidationMessage(
                            chatState,
                            "Нужно выбрать вариант из предложенных. Попробуйте еще раз",
                            sender
                    );
                    reenterStage(chatState, sender);
                }
            }
        }
    }
    private String normalizePhoneNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        char[] arr = number.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : arr) {
            if (c >= '0' && c <= '9') {
                result.append(c);
            }
        }
        if (result.length() == 11) {
            return "7" + result.substring(1);
        }
        if (result.length() < 10) {
            return null;
        }
        return result.toString();
    }
}

