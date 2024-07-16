package ru.croc.team4.bot.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.croc.team4.bot.dto.*;
import ru.croc.team4.bot.flow.command.ticket.Ticket;
import ru.croc.team4.bot.flow.stage.Stage;

import java.util.*;

@NoArgsConstructor
public class ChatState {

    @Getter
    private long chatId;

    @Getter
    private String phone;

    @Getter
    private UUID movie;

    @Getter
    private UUID session;

    @Getter
    private UUID row;

    @Getter
    private UUID place;

    @Getter
    private Ticket ticket;

    @Getter
    private List<MovieDto> movies;

    @Getter
    private List<SessionDto> sessions;

    @Getter
    private List<RowDto> rows;

    @Getter
    private List<PlaceDto> places;

    @Getter
    private Stage currentStage;

    @Getter
    private int menuMessageId = -1;

    @Getter
    private boolean approved = false;

    @JsonIgnore
    private final List<Update> updates = new ArrayList<>();

    @Setter
    @JsonIgnore
    private ChatStateMap storage;

    public ChatState(long chatId, ChatStateMap storage) {
        this.chatId = chatId;
        this.setStorage(storage);
    }

    @JsonIgnore
    public String getChatIdStr() {
        return String.valueOf(getChatId());
    }

    public void setCurrentStage(Stage stage) {
        currentStage = stage;
        updates.clear();
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setPlace(UUID place) {
        this.place = place;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setSession(UUID session) {
        this.session = session;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setRow(UUID row) {
        this.row = row;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setMovie(UUID movie) {
        this.movie = movie;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setPlaces(List<PlaceDto> places) {
        this.places = places;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setSessions(List<SessionDto> sessions) {
        this.sessions = sessions;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setRows(List<RowDto> rows) {
        this.rows = rows;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setMovies(List<MovieDto> movies) {
        this.movies = movies;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void addUpdate(Update update) {
        updates.add(update);
    }

    @JsonIgnore
    public int getLastReceivedMessageId() {
        if (updates.isEmpty()) {
            return -1;
        }
        Update last = updates.get(updates.size() - 1);
        if (last.hasMessage()) {
            return last.getMessage().getMessageId();
        }
        if (last.hasCallbackQuery()) {
            return last.getCallbackQuery().getMessage().getMessageId();
        }
        return -1;
    }

    public boolean canRedrawMenu() {
        return getMenuMessageId() != -1;
    }

    public void resetMenuMessageId() {
        setMenuMessageId(-1);
    }

    public void setMenuMessageId(int menuMessageId) {
        this.menuMessageId = menuMessageId;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public void resetRequest() {
        this.ticket = null;
        this.movie = null;
        this.session = null;
        this.row = null;
        this.place = null;
        this.movies = null;
        this.sessions = null;
        this.rows = null;
        this.places = null;
        getStorage().ifPresent(ChatStateMap::store);
    }

    public Optional<ChatStateMap> getStorage() {
        return Optional.ofNullable(storage);
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
        getStorage().ifPresent(ChatStateMap::store);
    }
}