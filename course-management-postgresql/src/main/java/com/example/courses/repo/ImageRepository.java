package com.example.courses.repo;

import com.example.courses.model.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ImageRepository extends JpaRepository<CourseImage, Long> {
    List<CourseImage> findByCourseId(Long courseId);
}
