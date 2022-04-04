package com.maksim.diplom.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_tags")
public class UsersTags {
    /**
     * Поле идентификатора.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле идентификатора объявления.
     */
    private Long usersId;
    /**
     * Поле имя.
     */
    private String tagName;
}
