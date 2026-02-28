package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;

import java.time.Instant;
public final class Checkout {
    // Уникальный номер выдачи. Программа назначает сама.
    public long id;
    // Какой прибор выдали (id прибора).
    // Должен ссылаться на реально существующий Instrument.
    public long instrumentId;
    // Кому выдали (логин). Нельзя пустое. До 64 символов.
    public String username;
// Комментарий (например “for field sampling”). Можно пусто. До 128 символов.
    public String comment;
    // Когда взяли прибор. Программа ставит автоматически.
    public Instant takenAt;
    // Когда вернули. Может быть null, если ещё не вернули.
    public Instant returnedAt;
// Состояние при возврате: OK или DAMAGED. Может быть null, если ещё не вернули.
    public ReturnCondition returnCondition;
// Кто оформил выдачу (логин). На ранних этапах можно "SYSTEM".
    public String ownerUsername;
    // Когда запись создана. Программа ставит автоматически.
    public Instant createdAt;
}
