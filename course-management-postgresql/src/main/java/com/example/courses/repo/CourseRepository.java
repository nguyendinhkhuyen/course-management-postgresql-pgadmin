package com.example.courses.repo;

import com.example.courses.model.Course;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    boolean existsByCategoryId(Long id);

    @Query("select count(c)>0 from Course c where lower(c.name)=lower(:name) and c.category.id=:categoryId and (:excludeId is null or c.id<>:excludeId)")
    boolean duplicate(@Param("name") String name, @Param("categoryId") Long categoryId, @Param("excludeId") Long excludeId);
}
