package ru.itmo.ArsikAndEva.storage;

import ru.itmo.ArsikAndEva.model.*;

import java.io.Serializable;
import java.util.HashMap;

public record AllData(HashMap<Integer, Booking> bookings, HashMap<Integer, Instrument> instruments,
                      HashMap<Integer, Checkout> checkouts) implements Serializable {
}
