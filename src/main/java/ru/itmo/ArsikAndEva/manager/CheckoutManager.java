package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.db.CheckoutRepository;
import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;
import ru.itmo.ArsikAndEva.validator.BookingValidator;
import ru.itmo.ArsikAndEva.validator.CheckoutValidator;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CheckoutManager {
    private final Map<Long, Checkout> checkoutMap = new HashMap<>();

    private final CheckoutValidator validator = new CheckoutValidator();
    private final InstrumentManager instrumentManager;
    private final CheckoutRepository repo;

    public CheckoutManager(InstrumentManager instrumentManager, CheckoutRepository repo) {
        this.instrumentManager = instrumentManager;
        this.repo = repo;
        loadAll();
    }

    public void loadAll(){
        try{
            checkoutMap.clear();
            for (Checkout chk : repo.findAll()){
                checkoutMap.put(chk.getId(), chk);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка загрузки выдачи из БД: " + e.getMessage());
        }
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


        checkout.setTakenAt(Instant.now());
        checkout.setCreatedAt(Instant.now());
        try{
            long id = repo.insert(checkout);
            checkout.setId(id);
            checkoutMap.put(id, checkout);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при создании выдачи: " + e.getMessage(), e);
        }
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
        try {
            repo.update(checkout);
            checkoutMap.put(id, checkout);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при обновлении выдачи: " + e.getMessage(), e);
        }
    }

    public void remove(Long id){
        if (!checkoutMap.containsKey(id))
            throw new EntityNotFoundException("Выдача с id " + id + " нет");
        try {
            repo.delete(id);
            checkoutMap.remove(id);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при удалении выдачи: " + e.getMessage(), e);
        }
    }


    public void returnInstrument(Long checkoutId, ReturnCondition condition){
        Checkout checkout = getById(checkoutId).orElseThrow(() -> new EntityNotFoundException("Выдача с таким id не существует"));

        if (checkout.getReturnedAt() != null)
            throw new ValidationException("Прибор уже вернули");

        checkout.setReturnedAt(Instant.now());
        checkout.setReturnCondition(condition);

        try {
            repo.update(checkout);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при возврате: " + e.getMessage(), e);
        }

        if (condition == ReturnCondition.DAMAGED){
            Instrument instrument = instrumentManager.getById(checkout.getInstrumentId()).orElseThrow(() -> new EntityNotFoundException("Прибор не найден"));
            instrument.setStatus(InstrumentStatus.OUT_OF_SERVICE);
            instrumentManager.update(instrument.getId(), instrument);
        }
    }

//
//    public  HashMap<Long, Checkout> getData(){
//        return new HashMap<>(checkoutMap);
//    }
//    public void loadData(HashMap<Long, Checkout> newCheckouts){
//        this.checkoutMap.clear();
//        this.checkoutMap.putAll(newCheckouts);
//    }
}
