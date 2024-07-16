package ru.croc.team4.bot.listener;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.croc.team4.bot.dto.TicketUpdateDto;
import ru.croc.team4.bot.flow.EntryBot;
import ru.croc.team4.bot.flow.command.Command;

@RequiredArgsConstructor
@Component
public class BotListener {
    private final EntryBot telegramBot;
    Logger logger = LoggerFactory.getLogger(Command.class);
    ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "groupId")
    public void listen(String message) {

        TicketUpdateDto ticketUpdateDto = mapper.readValue(message, TicketUpdateDto.class);

        String text;
        switch (ticketUpdateDto.newStatus()) {
            case ("FREE") -> text = "Ваша бронь под номером " + ticketUpdateDto.bookingCode() + " была отменена";
            case ("PAID") -> text = "Ваша бронь под номером " + ticketUpdateDto.bookingCode() + " была оплачена";
            case ("BOOKED") -> {
                return;
            }
            default -> {
                logger.error("Неизвестный статус места");
                return;
            }
        }
        var sendMessage = new SendMessage(
                Long.toString(ticketUpdateDto.chatId()), text);

        try {
            telegramBot.send(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Не удалось отправить уведомление об изменении статуса места");
        }
    }
}
