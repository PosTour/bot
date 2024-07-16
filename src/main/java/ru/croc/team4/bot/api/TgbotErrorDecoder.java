package ru.croc.team4.bot.api;

import feign.Response;
import feign.codec.ErrorDecoder;

import ru.croc.team4.bot.exception.TgBotNotFoundException;
import ru.croc.team4.bot.exception.TgBotUnauthorizedException;

public class TgbotErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            if (methodKey.contains("signUp")) {
                return new TgBotUnauthorizedException();
            } else {
                return new TgBotNotFoundException();
            }
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
