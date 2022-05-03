package com.maksim.diplom.repos;

import com.maksim.diplom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * Интерфейс User repository.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * Поиск по логину или почте.
     *
     * @param login логин пользователя
     * @param email почта пользователя
     * @return User
     */
    User findByLoginOrEmail(String login, String email);

    /**
     * Поиск по логину или id пользователя.
     *
     * @param login логин
     * @param id id пользователя
     * @return ser
     */
    User findByLoginOrId(String login, Long id);

    /**
     * Поиск по почте.
     *
     * @param email почта
     * @return User
     */
    User findByEmail(String email);

    /**
     * Поиск по номеру телефона.
     *
     * @param phone номер телефона
     * @return User
     */
    User findByPhoneNumber(String phone);

    /**
     * Поиск по логину.
     *
     * @param login логин
     * @return User
     */
    User findByLogin(String login);

    /**
     * Поиск по id.
     *
     * @param id идентификатор пользователя
     * @return User
     */
    User findById(long id);

    /**
     * Поиск максимального id
     * @return maxId
     */
    @Transactional
    @Query(nativeQuery = true, value = "select max(id) from users_table")
    Long getMaxId();

    /**
     * Удаление по id
     * @param id идентификатор
     */
    @Transactional
    void deleteAllById(Long id);

    /**
     * Обновление данных верификации
     * @param confirm
     * @param id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update users_table set confirm = ?1 where id = ?2")
    void updateConfirm(String confirm, Long id);

    /**
     * Обновление данных
     * @param name
     * @param login
     * @param email
     * @param phone_Number
     * @param id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update users_table set name = ?1, login = ?2, email = ?3, phone_number = ?4 where id = ?5")
    void updateDate(String name, String login, String email, String phone_Number, Long id);
}
