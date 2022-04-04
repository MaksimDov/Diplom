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
    @Transactional
    @Query(nativeQuery = true, value = "select max(id) from advert_table")
    Long getMaxAdvertId();

    @Override
    List<Advert> findAll();

    Optional<Advert> findById(Long id);

    @Modifying
    @Transactional
    void deleteAllById(@Param("id") Long id);

//    List<Advert> findAllByUserId(Long userId);

}
