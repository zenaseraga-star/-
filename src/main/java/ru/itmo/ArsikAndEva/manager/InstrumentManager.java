package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.db.InstrumentRepository;
import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.validator.InstrumentValidator;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InstrumentManager {
    private Map<Long, Instrument> instruments = new HashMap<>();
    private InstrumentValidator instrumentValidator = new InstrumentValidator();
    private final InstrumentRepository repo;

    public InstrumentManager(InstrumentRepository repo){
        this.repo = repo;
        loadAll();
    }

    public void loadAll() {
        try {
            instruments.clear();
            for (Instrument it : repo.findAll()) {
                instruments.put(it.getId(), it);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка загрузки приборов из БД: " + e.getMessage());
        }
    }


    public void add(Instrument instrument) {
        instrumentValidator.validate(instrument);
        try {
            long id = repo.insert(instrument);
            instrument.setId(id);
            instruments.put(id, instrument);
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")){
                throw new ValidationException("Инветарный номер уже занят");
            }
            throw new RuntimeException("Ошибка БД при добавлении прибора: " + e.getMessage());
        }
    }

    public Optional<Instrument> getById(Long id){
       return Optional.ofNullable(instruments.get(id));
    }

    public List<Instrument> getAll(){
        return new ArrayList<>(instruments.values());
    }

    public void update(Long id, Instrument instrument) {
        if (!instruments.containsKey(id))
            throw new EntityNotFoundException("инструмента с id " + id + " нет в списке");
        instrumentValidator.validate(instrument);
        instrument.setId(id);
        instruments.put(id, instrument);
        instrument.setUpdatedAt(Instant.now());
        try {
            repo.update(instrument);
            instruments.put(id, instrument);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ValidationException("Инвентарный номер уже занят");
            }
            throw new RuntimeException("Ошибка БД при обновлении прибора: " + e.getMessage(), e);
        }
    }

    public void remove(Long id){
        if (!instruments.containsKey(id))
            throw new EntityNotFoundException("инструмента с id " + id + " нет в списке");
        try{
            repo.delete(id);
            instruments.remove(id);
        } catch (SQLException e){
            if (e.getSQLState().equals("23505")){
                throw new ValidationException("Нельзя удалить прибор: на него есть брони или выдачи");
            }
            throw new RuntimeException("Ошибка БД при удалении прибора " + e.getMessage(), e);
        }
    }

//    public HashMap<Long, Instrument> getData(){
//        return new HashMap<>(instruments);
//    }
//
//    public void loadData(HashMap<Long, Instrument> newInst){
//        this.instruments.clear();
//        this.instruments.putAll(newInst);
//    }
}