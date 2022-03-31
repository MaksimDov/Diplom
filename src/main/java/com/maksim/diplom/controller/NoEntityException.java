package com.maksim.diplom.controller;

public class NoEntityException extends Exception {
    public NoEntityException(Long userId) {
        super("Пользователь с id = " + userId + " не найден.");
    }
}
