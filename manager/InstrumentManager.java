package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;
import ru.itmo.ArsikAndEva.validator.InstrumentValidator;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InstrumentManager {
    private final Map<Long, Instrument> instruments = new HashMap<>();
    private final AtomicLong giveId = new AtomicLong();
    private InstrumentValidator instrumentValidator = new InstrumentValidator();


    public InstrumentManager() {
        add(new Instrument("PH-100", InstrumentType.PH_METER, "Lab A", "pH метр цифровой", InstrumentStatus.ACTIVE));
        add(new Instrument("TEMP-200", InstrumentType.THERMOMETER, "Lab B", "Термометр инфракрасный", InstrumentStatus.ACTIVE));
        add(new Instrument("SCALE-300", InstrumentType.BALANCE, "Lab C", "Весы аналитические", InstrumentStatus.ACTIVE));
        add(new Instrument("SPECTRO-101", InstrumentType.SPECTROPHOTOMETER, "Lab D", "Спектрометр UV-VIS", InstrumentStatus.OUT_OF_SERVICE));
        add(new Instrument("MICRO-202", InstrumentType.CONDUCTIVITY_METER, "Lab E", "Микроскоп цифровой", InstrumentStatus.ACTIVE));
        add(new Instrument("CENT-150", InstrumentType.THERMOMETER, "Lab F", "Центрифуга лабораторная", InstrumentStatus.ACTIVE));
        add(new Instrument("OVEN-80", InstrumentType.THERMOMETER, "Lab G", "Сушильный шкаф", InstrumentStatus.ACTIVE));
        add(new Instrument("SHAKER-75", InstrumentType.THERMOMETER, "Lab H", "Шейкер орбитальный", InstrumentStatus.OUT_OF_SERVICE));
        add(new Instrument("BATH-60", InstrumentType.THERMOMETER, "Lab I", "Водяная баня", InstrumentStatus.ACTIVE));
        add(new Instrument("PUMP-50", InstrumentType.THERMOMETER, "Lab J", "Вакуумный насос", InstrumentStatus.ACTIVE));
    }

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

}