package com.maksim.diplom.repos;

import com.maksim.diplom.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepo extends JpaRepository <Tags, Long> {
}
