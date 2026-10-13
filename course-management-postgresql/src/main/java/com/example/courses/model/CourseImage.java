package com.example.courses.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CourseImages")
public class CourseImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2048)
    private String imageUrl;
    @Column(nullable = false)
    private boolean isThumbnail;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long x) {
        id = x;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String x) {
        imageUrl = x;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean x) {
        isThumbnail = x;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course x) {
        course = x;
    }
}
