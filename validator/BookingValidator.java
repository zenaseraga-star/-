package ru.itmo.ArsikAndEva.validator;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.exception.ValidationException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingValidator implements Validator<Booking> {
    @Override
    public void validate(Booking booking) throws ValidationException{
        if(booking == null) {
            throw  new ValidationException("Бронь не осуществлялась" );
        }
        if(booking.getStatus() == null){
            throw new ValidationException("У брони должен быть статус");
        }
        if(booking.getOwnerUsername() == null || booking.getOwnerUsername().isEmpty() || booking.getOwnerUsername().isBlank()){
            throw new ValidationException("Надо указать свой логин");
        }
        if(booking.getStartAt().isAfter(booking.getEndAt())){
            throw new ValidationException(" Время начала брони не может быть после конца");
        }


    }
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static boolean isValidate(String date){
        if(date == null || date.isEmpty()){
            return false;
        }
        try{
            LocalDateTime.parse(date, formatter);
            return true;
        }
        catch (DateTimeException e){
            return false;
        }
    }
    public static void validateTime(String time){
        if(!isValidate(time)){
            throw new ValidationException( " неверный формат даты ");
        }
    }
}

