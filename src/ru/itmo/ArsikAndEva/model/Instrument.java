package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.time.Instant;

public final class Instrument {
    // Уникальный номер прибора. Программа назначает сама.
    private final long id;

// Название прибора (чтобы человек понял, что это). Нельзя пустое. До 128 символов.
    private String name;

    // Тип прибора (например PH_METER, BALANCE). Выбирается из списка InstrumentType.
    private InstrumentType type;

// Инвентарный номер (например "INV-00077"). Можно пусто. До 32 символов.
    private String inventoryNumber;

// Где находится (например "Lab-2 bench"). Нельзя пустое. До 64 символов.
    private String location;

    // Статус прибора: ACTIVE или OUT_OF_SERVICE.
    private InstrumentStatus status;

// Кто добавил запись (логин). На ранних этапах можно "SYSTEM".
    private String ownerUsername;

    // Когда создано. Программа ставит автоматически.
    private final Instant createdAt;

    // Когда обновляли. Программа обновляет автоматически.
    private Instant updatedAt;


    public Instrument(long id, String name, InstrumentType type, String inventoryNumber, String location, InstrumentStatus status, String ownerUsername, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.setName(name);
        this.type = type;
        this.inventoryNumber = inventoryNumber;
        this.location = location;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public InstrumentType getType() {
        return type;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public String getLocation() {
        return location;
    }

    public InstrumentStatus getStatus() {
        return status;
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


    public void setName(String name) throws IllegalAccessException {
        if



    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
