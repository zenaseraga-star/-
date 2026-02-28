package model;
import java.time.Instant;
public final class Booking {
// Уникальный номер бронирования. Программа назначает сама.
private  final long id;

// Какой прибор бронируют (id прибора).
// Должен ссылаться на реально существующий Instrument.
private  long instrumentId;
// Время начала бронирования.
private  Instant startAt;
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
}
