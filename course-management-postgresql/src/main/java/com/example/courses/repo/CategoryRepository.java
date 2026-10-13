package com.example.courses.repo;

import com.example.courses.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CourseCategory, Long> {
}
