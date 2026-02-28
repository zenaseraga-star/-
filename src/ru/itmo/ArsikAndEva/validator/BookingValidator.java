package ru.itmo.ArsikAndEva.validator;
import model;
import exception.ValidationException;

public class BookingValidator implements Validator<Booking> {
    @Override
    public void validate(Booking booking){
        if(booking == null) {
            throw  new ValidationException("Бронь не осуществлялась" );
        }
        if(booking.getStatus() == null){
            throw new ValidationException("У брони должен быть статус");
        }
        if(booking.getOwnerUsername() == null || booking.getOwnerUsername().isEmpty() || booking.getOwnerUsername().isBlank()){
            throw new ValidationException("Надо указать свой логин");
        }

        
        }
        

        
    }
