package main.java.ru.itmo.ArsikAndEva.manager;
import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.model.Checkout;
import main.java.ru.itmo.ArsikAndEva.model.Instrument;
import main.java.ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import main.java.ru.itmo.ArsikAndEva.model.enums.ReturnCondition;
import main.java.ru.itmo.ArsikAndEva.validator.CheckoutValidator;

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

        Instrument instrument = instrumentManager.getById(checkout.getInstrumentId()).orElseThrow(() -> new EntityNotFoundException("Прибор с таким id не существует"));

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

    public Optional<Checkout> getById(Long id){
        return Optional.ofNullable(checkoutMap.get(id));
    }

    public List<Checkout> getAll(){
        return new ArrayList<>(checkoutMap.values());
    }

    public void update(Long id, Checkout checkout){
        if (!checkoutMap.containsKey(id))
            throw new EntityNotFoundException("Выдача с номером " + id + "не была найдена");

        instrumentManager.getById(checkout.getInstrumentId()).orElseThrow(() -> new EntityNotFoundException("Прибора с таким id не существует"));

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
        Checkout checkout = getById(checkoutId).orElseThrow(() -> new EntityNotFoundException("Выдача с таким id не существует"));

        if (checkout.getReturnedAt() != null)
            throw new ValidationException("Прибор уже вернули");

        checkout.setReturnedAt(Instant.now());
        checkout.setReturnCondition(condition);

        if (condition == ReturnCondition.DAMAGED){
            Instrument instrument = instrumentManager.getById(checkout.getInstrumentId()).orElseThrow(() -> new EntityNotFoundException("Прибор не найден"));
            instrument.setStatus(InstrumentStatus.OUT_OF_SERVICE);
            instrumentManager.update(instrument.getId(), instrument);
        }
    }
}
