package ru.itmo.ArsikAndEva.validator;

import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.exception.ValidationException;

public class InstrumentValidator implements Validator<Instrument> {

    @Override
    public void validate(Instrument instrument) {
        if (instrument == null) {
            throw new ValidationException("объект прибора не может быть null");
        }

        if (instrument.getName() == null || instrument.getName().isEmpty()) {
            throw new ValidationException("название не может быть пустым");
        }
        if (instrument.getName().length() > 128) {
            throw new ValidationException("название не может быть длиннее 128 символов");
        }

        if (instrument.getType() == null) {
            throw new ValidationException("необходимо выбрать тип прибора");
        }

        if (instrument.getInventoryNumber() != null && instrument.getInventoryNumber().length() > 32) {
            throw new ValidationException("инвентарный номер не может быть длиннее 32 символов");
        }

        if (instrument.getLocation() == null || instrument.getLocation().isEmpty()) {
            throw new ValidationException("нужно написать, где сейчас находится " + instrument.getName());
        }
        if (instrument.getLocation().length() > 64) {
            throw new ValidationException("локация не может быть длиннее 64 символов");
        }

        if (instrument.getStatus() == null) {
            throw new ValidationException("статус прибора должен быть указан");
        }

        if (instrument.getOwnerUsername() == null || instrument.getOwnerUsername().isEmpty()) {
            throw new ValidationException("владелец записи должен быть указан");
        }
    }
}