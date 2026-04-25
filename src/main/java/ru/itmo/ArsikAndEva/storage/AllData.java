package ru.itmo.ArsikAndEva.storage;

import ru.itmo.ArsikAndEva.model.*;

import java.io.Serializable;
import java.util.HashMap;

public record AllData(HashMap<Long, Booking> bookings, HashMap<Long, Instrument> instruments,
                      HashMap<Long, Checkout> checkouts) implements Serializable {
}
