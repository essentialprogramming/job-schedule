package com.api.repository;

import com.api.entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Integer> {

    Optional<Story> findByStoryKey(String storyKey);
}
