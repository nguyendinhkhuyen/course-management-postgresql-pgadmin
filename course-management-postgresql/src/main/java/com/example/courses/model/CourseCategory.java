package com.example.courses.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "CourseCategories")
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên loại không được để trống")
    @Column(nullable = false)
    private String name;
    @Column(length = 2000)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long x) {
        id = x;
    }

    public String getName() {
        return name;
    }

    public void setName(String x) {
        name = x;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String x) {
        description = x;
    }
}
