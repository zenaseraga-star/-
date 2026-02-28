package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;
import java.time.Instant;
import java.util.Objects;

public class Instrument {

    private long id;
    private String name;
    private InstrumentType type;
    private String inventoryNumber;
    private String location;
    private InstrumentStatus status;
    private String ownerUsername;
    private final Instant createdAt;
    private Instant updatedAt;

    public Instrument(String name, InstrumentType type, String inventoryNumber,
                      String location, InstrumentStatus status, String ownerUsername) {
        this.name = name;
        this.type = type;
        this.inventoryNumber = inventoryNumber;
        this.location = location;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
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


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(InstrumentType type) {
        this.type = type;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return id == that.id && Objects.equals(name, that.name) && type == that.type && Objects.equals(inventoryNumber, that.inventoryNumber) && Objects.equals(location, that.location) && status == that.status && Objects.equals(ownerUsername, that.ownerUsername) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, inventoryNumber, location, status, ownerUsername, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", inventoryNumber='" + inventoryNumber + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}