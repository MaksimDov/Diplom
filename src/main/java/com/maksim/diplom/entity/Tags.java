package com.maksim.diplom.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags_table")
public class Tags {
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
     * Поле идентификатора объявления.
     */
    private Long advertId;
}
