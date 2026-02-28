package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.BookingStatus;

import java.time.Instant;


public final class Booking {
// Уникальный номер бронирования. Программа назначает сама.
    public long id;
    // Какой прибор бронируют (id прибора).
    // Должен ссылаться на реально существующий Instrument.
    public long instrumentId;
    // Время начала бронирования.
    public Instant startAt;
    // Время конца бронирования.
    public Instant endAt;
    // Статус бронирования: ACTIVE или CANCELLED.
    public BookingStatus status;
// Кто забронировал (логин). На ранних этапах можно "SYSTEM".
    public String ownerUsername;
    // Когда создали бронь. Программа ставит автоматически.
    public Instant createdAt;
    // Когда обновляли. Программа обновляет автоматически.
    public Instant updatedAt;
}