package ru.itmo.ArsikAndEva.validator;

import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.storage.AllData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FailValidator implements Validator<AllData> {

    @Override
    public void validate(AllData allData) throws ValidationException {
        if (allData == null) {
            throw new ValidationException("Данные не найдены");
        }
        if (allData.bookings() != null) {
            for (Booking booking : allData.bookings().values()) {
                new BookingValidator().validate(booking);
            }
        }
            if (allData.instruments() != null) {
                for (Instrument instrument : allData.instruments().values()) {
                    new InstrumentValidator().validate(instrument);
                }
            }
                if (allData.checkouts() != null) {
                    for (Checkout checkout : allData.checkouts().values()) {
                        new CheckoutValidator().validate(checkout);
                    }
                }
            }
        }


       //   File file = new File(filepath);
      //  if (!file.exists()) {
        //    throw new ValidationException("Файла не существует " + filepath);

      //  }
   //     if (!file.canRead()) {
   //         throw new ValidationException("Невозможно прочитать файл " + filepath);
  //      }

  //  }

  //  public void validateData(AllData allData) throws ValidationException {
//System.out.println("Данные повреждены");

