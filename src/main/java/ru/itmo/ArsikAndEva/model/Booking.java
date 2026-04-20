package main.java.ru.itmo.ArsikAndEva.model;

import main.java.ru.itmo.ArsikAndEva.model.enums.BookingStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class Booking {
    // Уникальный номер бронирования. Программа назначает сама.
    private long id;

    // Какой прибор бронируют (id прибора).
    // Должен ссылаться на реально существующий Instrument.
    private long instrumentId;
    // Время начала бронирования.
    private Instant startAt;
    // Время конца бронирования.
    private Instant endAt;
    // Статус бронирования: ACTIVE или CANCELLED.
    private BookingStatus status;
    // Кто забронировал (логин). На ранних этапах можно "SYSTEM".
    private String ownerUsername;
    // Когда создали бронь. Программа ставит автоматически.
    private final Instant createdAt;
    // Когда обновляли. Программа обновляет автоматически.
    private  Instant updatedAt;

    public Booking( long id, long instrumentId,  Instant startAt, Instant endAt, BookingStatus status, String ownerUsername, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.instrumentId = instrumentId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Booking(long id, Instant createdAt){
        this.id = id;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
        String formattedStart = form.format(startAt);
        String formattedEnd = form.format(endAt);
        return String.format("Booking #%d | Inst: %d | Start: %s | End: %s | Status: %s",
                id, instrumentId, formattedStart, formattedEnd, status);
    }
}