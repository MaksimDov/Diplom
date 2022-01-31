package com.maksim.diplom.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс представления объявления.
 */
@Data
@Entity
@Table(name = "pictures_table")
public class Picture {
    /**
     * Поле идентификатора.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле id объявления.
     */
    private Long advertId;
    /**
     * Поле изображения.
     */
    private String picture;
}
