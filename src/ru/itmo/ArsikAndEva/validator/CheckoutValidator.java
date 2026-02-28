package ru.itmo.ArsikAndEva.validator;
import ru.itmo.ArsikAndEva.model.Instrument;
import model.Checkout;
import ru.itmo.ArsikAndEva.exception.ValidationException;

public class CheckoutValidator implements Validator<Checkout> {
    @Override
    puvlic void validate(Checkout checkout){
        if( checkout.getUsername() == null){
            throw new ValidationException(" Имя не может быть пустым");

        }
         if( checkout.getUsername().length() > 64){
            throw new ValidationException(" Имя не может быть длинее 64 символов");

    }
    if(checkout.getComment().length() > 128){
throw  new  ValidationException("Кoмментарий не может быть длинее 128 символов");
    }
    if (checkout.getReturnCondition() == null){
        throw new  ValidationException(" Состояние инструмента не может быть null");
    }

}
}

