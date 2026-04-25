package ru.itmo.ArsikAndEva.model;

import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Instrument implements Serializable {

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
                      String location, InstrumentStatus status) {
        this.name = name;
        this.type = type;
        this.inventoryNumber = inventoryNumber;
        this.location = location;
        this.status = status;
        this.ownerUsername = "System";
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ID: %d \t Name: %-15s",
                id, name, type, status, location);
    }
}