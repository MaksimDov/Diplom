package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Advert;
import com.maksim.diplom.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagsRepo extends JpaRepository <Tags, Long> {

    List<Tags> findAllByAdvertId(Long advertId);
}
