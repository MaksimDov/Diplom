package com.maksim.diplom.repos;

import com.maksim.diplom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Интерфейс User repository.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * Find by login or email user.
     *
     * @param login the login
     * @param email       the email
     * @return the user
     */
    User findByLoginOrEmail(String login, String email);

    /**
     * Find by login or id user.
     *
     * @param login the login
     * @param id       the user's id
     * @return the user
     */
    User findByLoginOrId(String login, Long id);

    /**
     * Find user by login.
     *
     * @param login the user's login
     * @return the user
     */
    User findByEmail(String login);

    /**
     * Find user by email.
     *
     * @param email the user's email
     * @return the user
     */
    User findByLogin(String email);

    /**
     * Find user by id.
     *
     * @param id the user's id
     * @return the user
     */
    User findById(long id);
}
