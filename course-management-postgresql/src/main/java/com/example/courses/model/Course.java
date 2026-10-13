
package com.example.courses.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "Courses",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_course_name_category",
                columnNames = {"name", "course_category_id"}
        )
)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên khóa học không được để trống")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Nhập học phí")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Học phí phải lớn hơn 0"
    )
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal tuitionFee;

    @NotNull(message = "Nhập thời lượng")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(length = 4000)
    private String description;

    // Quan hệ nhiều khóa học thuộc một loại khóa học
    // Không dùng @NotNull ở đây vì category
    // được gán trong Controller sau khi nhận categoryId
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "course_category_id", nullable = false)
    private CourseCategory category;

    // Một khóa học có nhiều ảnh
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("id ASC")
    private List<CourseImage> images = new ArrayList<>();

    // Phân loại thời lượng, không lưu vào database
    public String getDurationLabel() {
        return duration != null && duration <= 30
                ? "Ngắn hạn"
                : "Dài hạn";
    }

    // Getters và Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public List<CourseImage> getImages() {
        return images;
    }

    public void setImages(List<CourseImage> images) {
        this.images = images;
    }
}
