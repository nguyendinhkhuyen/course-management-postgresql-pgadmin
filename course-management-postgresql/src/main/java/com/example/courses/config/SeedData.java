package com.example.courses.config;

import com.example.courses.model.*;
import com.example.courses.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

import java.math.BigDecimal;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner seed(CategoryRepository cats, CourseRepository courses, ImageRepository images) {
        return args -> {
            if (cats.count() > 0 || courses.count() > 0) return;
            String[] names = {"Lập trình Web", "Thiết kế", "Ngoại ngữ"};
            CourseCategory[] categories = new CourseCategory[3];
            for (int i = 0; i < 3; i++) {
                CourseCategory cat = new CourseCategory();
                cat.setName(names[i]);
                cat.setDescription("Nhóm khóa học " + names[i]);
                categories[i] = cats.save(cat);
            }
            String[] titles = {"Java Spring Boot", "ReactJS căn bản", "UI UX Design", "Tiếng Anh giao tiếp", "Python nâng cao"};
            int[] hours = {48, 24, 36, 20, 60};
            int[] fees = {3500000, 1800000, 2600000, 1500000, 4200000};
            int[] group = {0, 0, 1, 2, 0};
            for (int i = 0; i < 5; i++) {
                Course c = new Course();
                c.setName(titles[i]);
                c.setDuration(hours[i]);
                c.setTuitionFee(BigDecimal.valueOf(fees[i]));
                c.setCategory(categories[group[i]]);
                c.setActive(i != 4);
                c.setDescription("Khóa học thực hành: " + titles[i]);
                c = courses.save(c);
                if (i < 3) {
                    CourseImage img = new CourseImage();
                    img.setCourse(c);
                    img.setImageUrl("https://picsum.photos/seed/course" + i + "/720/420");
                    img.setThumbnail(true);
                    images.save(img);
                }
            }
        };
    }
}
