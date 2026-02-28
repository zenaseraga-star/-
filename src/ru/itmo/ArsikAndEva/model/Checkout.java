package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;

import java.time.Instant;
package model;

import java.time.Instant;

public final class Checkout {
// Уникальный номер выдачи. Программа назначает сама.
private final long id;
// Какой прибор выдали (id прибора).
// Должен ссылаться на реально существующий Instrument.
private long instrumentId;
// Кому выдали (логин). Нельзя пустое. До 64 символов.
private  String username;
// Комментарий (например “for field sampling”). Можно
//пусто. До 128 символов.
private  String comment;
// Когда взяли прибор. Программа ставит автоматически.
private  Instant takenAt;
// Когда вернули. Может быть null, если ещё не вернули.
private Instant returnedAt;
// Состояние при возврате: OK или DAMAGED. Может быть
//null, если ещё не вернули.
private ReturnCondition returnCondition;
// Кто оформил выдачу (логин). На ранних этапах можно
//"SYSTEM".
private String ownerUsername;
// Когда запись создана. Программа ставит автоматически.

private final Instant createdAt;
public Checkout(long id, long instrumentId,  String username, String comment, Instant takenAt, Instant returnedAt, ReturnCondition returnCondition, String ownerUsername, Instant createdAt ){
this.id = id;
this.instrumentId = instrumentId;
this.username = username;
this.comment = comment;
this.takenAt = takenAt;
this.returnedAt = returnedAt;
this.returnCondition = returnCondition;
this.ownerUsername = ownerUsername;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Instant takenAt) {
        this.takenAt = takenAt;
    }

    public Instant getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(Instant returnedAt) {
        this.returnedAt = returnedAt;
    }

    public ReturnCondition getReturnCondition() {
        return returnCondition;
    }

    public void setReturnCondition(ReturnCondition returnCondition) {
        this.returnCondition = returnCondition;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}

