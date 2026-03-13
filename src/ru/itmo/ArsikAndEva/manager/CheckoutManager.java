package ru.itmo.ArsikAndEva.manager;
import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;
import ru.itmo.ArsikAndEva.validator.CheckoutValidator;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class CheckoutManager {
    private final Map<Long, Checkout> checkoutMap = new HashMap<>();
    private final AtomicLong getId = new AtomicLong();
    CheckoutValidator validator = new CheckoutValidator();

    private final InstrumentManager instrumentManager;

    public CheckoutManager(InstrumentManager instrumentManager) {
        this.instrumentManager = instrumentManager;
    }

    public void add(Checkout checkout){
        validator.validate(checkout);

        Instrument instrument = instrumentManager.getById(checkout.getInstrumentId());

        if (instrument.getStatus() == InstrumentStatus.OUT_OF_SERVICE){
            throw new ValidationException("Инструмент сломан, поэтому брать его смысла нет");
        }

        boolean isBusy = checkoutMap.values().stream().anyMatch(chk -> chk.getInstrumentId() == checkout.getInstrumentId() && chk.getReturnedAt() == null);
        if (isBusy)
            throw new ValidationException("Этот прибор занят");


        long id = getId.incrementAndGet();
        checkout.setId(id);
        checkout.setTakenAt(Instant.now());
        checkout.setCreatedAt(Instant.now());
        checkoutMap.put(id, checkout);
    }

    public Checkout getById(Long id){
        if (!checkoutMap.containsKey(id))
            throw new EntityNotFoundException("Выдача с номером " + id + " не найдена");
        return checkoutMap.get(id);
    }

    public List<Checkout> getAll(){
        return new ArrayList<>(checkoutMap.values());
    }

    public void update(Long id, Checkout checkout){
        if (!checkoutMap.containsKey(id))
            throw new EntityNotFoundException("Выдача с номером " + id + "не была найдена");

        instrumentManager.getById(checkout.getInstrumentId());

        validator.validate(checkout);
        checkout.setId(id);
        checkoutMap.put(id, checkout);
    }

    public void remove(Long id){
        if (!checkoutMap.containsKey(id))
            throw new EntityNotFoundException("Выдача с id " + id + " нет");
        checkoutMap.remove(id);
    }


    public void returnInstrument(Long checkoutId, ReturnCondition condition){
        if (!checkoutMap.containsKey(checkoutId))
            throw new EntityNotFoundException("Выдача с номером " + checkoutId + " не найден");

        if (checkoutMap.get(checkoutId).getReturnedAt() != null)
            throw new ValidationException("Прибор уже вернули");

        checkoutMap.get(checkoutId).setReturnedAt(Instant.now());
        checkoutMap.get(checkoutId).setReturnCondition(condition);

        if (condition == ReturnCondition.DAMAGED){
            Instrument instrument = instrumentManager.getById(checkoutMap.get(checkoutId).getInstrumentId());
            instrument.setStatus(InstrumentStatus.OUT_OF_SERVICE);
            instrumentManager.update(instrument.getId(), instrument);
        }
    }
}
