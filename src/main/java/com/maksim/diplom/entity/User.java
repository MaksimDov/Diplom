package com.maksim.diplom.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс представления пользователя.
 */
@Data
@Entity
@Table(name = "users_table")
public class User {
    /**
     * Поле идентификатора.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле имя.
     */
    private String name;
    /**
     * Поле логина.
     */
    private String login;
    /**
     * Поле пароля.
     */
    private String password;
    /**
     * Поле почты.
     */
    private String email;
    /**
     * Поле номера телефона.
     */
    private String phoneNumber;

    public String toString(){
        return "Name: " + name + "\nLogin: " + login + "\nEmail: " + email + "\nPassword: " + password + "\n";
    }
}
