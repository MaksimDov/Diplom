package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertRepo extends JpaRepository<Advert, Long> {

}
