package ru.itmo.ArsikAndEva.validator;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.exception.ValidationException;

public class CheckoutValidator implements Validator<Checkout> {
    @Override
    public void validate(Checkout checkout){
        if (checkout == null){
            throw new ValidationException("Объект выдачи не может быть null");
        }

        if(checkout.getUsername() == null || checkout.getUsername().isBlank()){
            throw new ValidationException("Имя не может быть пустым");

        }
        if(checkout.getUsername().length() > 64){
            throw new ValidationException("Имя не может быть длиннее 64 символов");
        }

        if(checkout.getComment() != null && checkout.getComment().length() > 128){
            throw  new  ValidationException("Кoмментарий не может быть длиннее 128 символов");
        }

        if (checkout.getOwnerUsername() == null || checkout.getOwnerUsername().isBlank()){
            throw new ValidationException("Должно быть указано, кто оформлял выдачу");
        }
    }
}
