package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AdvertRepo extends JpaRepository<Advert, Long> {
    /**
     * Поиск последнего добавленного объявления
     * @return id
     */
    @Transactional
    @Query(nativeQuery = true, value = "select max(id) from advert_table")
    Long getMaxAdvertId();

    /**
     * Получение всех объявлений
     * @return List<Advert>
     */
    @Override
    List<Advert> findAll();

    /**
     * Поиск объявления по id
     * @param id идентификатор объявления.
     * @return Optional<Advert>
     */
    Optional<Advert> findById(Long id);

    /**
     * Удаление объявления
     * @param id идентификатор объявления
     */
    @Modifying
    @Transactional
    void deleteAllById(@Param("id") Long id);

    /**
     * Поиск объявления принадлежащих пользовтелю
     * @param userId идентификатор пользователя
     * @return List<Advert>
     */
    List<Advert> findAllByUserId(Long userId);

}
