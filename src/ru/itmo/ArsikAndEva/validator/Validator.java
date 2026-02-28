package ru.itmo.ArsikAndEva.validator;

public interface Validator<T> {
    void validate(T item);
}