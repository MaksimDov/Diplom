package com.maksim.diplom.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс представления объявления.
 */
@Data
@Entity
@Table(name = "advert_table")
public class Advert {
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
     * Поле описания.
     */
    private String description;
    /**
     * Поле идентификатора пользователя.
     */
    private Long UserId;
    /**
     * Поле города.
     */
    private String city;
}
