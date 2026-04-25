package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.validator.InstrumentValidator;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InstrumentManager {
    private Map<Long, Instrument> instruments = new HashMap<>();
    private final AtomicLong giveId = new AtomicLong();
    private InstrumentValidator instrumentValidator = new InstrumentValidator();

    public void add(Instrument instrument) {
        instrumentValidator.validate(instrument);

        long id = giveId.incrementAndGet();
        instrument.setId(id);
        instruments.put(id, instrument);
    }

    public Optional<Instrument> getById(Long id){
       return Optional.ofNullable(instruments.get(id));
    }

    public List<Instrument> getAll(){
        return new ArrayList<>(instruments.values());
    }

    public void update(Long id, Instrument instrument){
        if (!instruments.containsKey(id))
            throw new EntityNotFoundException("инструмента с id " + id + " нет в списке");

        instrumentValidator.validate(instrument);
        instrument.setId(id);
        instruments.put(id, instrument);
        instrument.setUpdatedAt(Instant.now());
    }

    public void remove(Long id){
        if (!instruments.containsKey(id))
            throw new EntityNotFoundException("инструмента с id " + id + " нет в списке");
        instruments.remove(id);
    }


    public HashMap<Long, Instrument> getData(){
        return new HashMap<>(instruments);
    }

    public void loadData(HashMap<Long, Instrument> newInst){
        this.instruments.clear();
        this.instruments.putAll(newInst);

        long maxId = newInst.keySet()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        giveId.set(maxId);
    }
}