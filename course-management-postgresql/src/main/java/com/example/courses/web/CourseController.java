package com.example.courses.web;

import com.example.courses.model.*;
import com.example.courses.repo.*;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courses;
    private final CategoryRepository categories;
    private final ImageRepository images;

    public CourseController(CourseRepository c, CategoryRepository cat, ImageRepository i) {
        courses = c;
        categories = cat;
        images = i;
    }

    @ModelAttribute("categories")
    public List<CourseCategory> categories() {
        return categories.findAll(Sort.by("name"));
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String q, @RequestParam(required = false) Long categoryId, @RequestParam(defaultValue = "false") boolean activeOnly, @RequestParam(required = false) BigDecimal minFee, @RequestParam(required = false) BigDecimal maxFee, @RequestParam(defaultValue = "") String sort, Model m) {
        if (minFee != null && maxFee != null && minFee.compareTo(maxFee) > 0)
            m.addAttribute("error", "Học phí tối thiểu không được lớn hơn học phí tối đa.");
        else {
            Specification<Course> spec = (root, query, cb) -> cb.conjunction();
            if (!q.isBlank()) {
                String term = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), term));
            }
            if (categoryId != null)
                spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
            if (activeOnly) spec = spec.and((root, query, cb) -> cb.isTrue(root.get("isActive")));
            if (minFee != null)
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("tuitionFee"), minFee));
            if (maxFee != null)
                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("tuitionFee"), maxFee));
            Sort order = switch (sort) {
                case "feeAsc" -> Sort.by("tuitionFee").ascending();
                case "feeDesc" -> Sort.by("tuitionFee").descending();
                case "durationAsc" -> Sort.by("duration").ascending();
                default -> Sort.by("id").ascending();
            };
            m.addAttribute("courses", courses.findAll(spec, order));
        }
        m.addAttribute("q", q);
        m.addAttribute("categoryId", categoryId);
        m.addAttribute("activeOnly", activeOnly);
        m.addAttribute("minFee", minFee);
        m.addAttribute("maxFee", maxFee);
        m.addAttribute("sort", sort);
        return "courses/list";
    }

    @GetMapping("/new")
    public String create(Model m) {
        m.addAttribute("course", new Course());
        return "courses/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model m) {
        m.addAttribute("course", get(id));
        return "courses/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("course") Course input, BindingResult errors, @RequestParam(required = false) Long categoryId, Model m, RedirectAttributes flash) {
        if (categoryId == null || !categories.existsById(categoryId))
            errors.rejectValue("category", "invalid", "Loại khóa học không tồn tại.");
        else input.setCategory(categories.findById(categoryId).orElseThrow());
        if (input.getName() != null && !input.getName().isBlank() && categoryId != null && categories.existsById(categoryId) && courses.duplicate(input.getName().trim(), categoryId, input.getId()))
            errors.rejectValue("name", "duplicate", "Tên khóa học đã tồn tại trong loại này.");
        if (errors.hasErrors()) return "courses/form";
        Course saved;
        if (input.getId() != null) {
            saved = get(input.getId());
            saved.setName(input.getName().trim());
            saved.setTuitionFee(input.getTuitionFee());
            saved.setDuration(input.getDuration());
            saved.setActive(input.isActive());
            saved.setDescription(input.getDescription());
            saved.setCategory(input.getCategory());
        } else {
            saved = input;
            saved.setName(input.getName().trim());
        }
        try {
            courses.saveAndFlush(saved);
        } catch (DataIntegrityViolationException ex) {
            errors.rejectValue("name", "duplicate", "Dữ liệu bị trùng hoặc không hợp lệ.");
            return "courses/form";
        }
        flash.addFlashAttribute("success", "Đã lưu khóa học.");
        return "redirect:/courses";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model m) {
        Course c = get(id);
        m.addAttribute("course", c);
        m.addAttribute("images", images.findByCourseId(id));
        return "courses/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        courses.delete(get(id));
        flash.addFlashAttribute("success", "Đã xóa khóa học.");
        return "redirect:/courses";
    }

    @PostMapping("/{id}/images")
    @Transactional
    public String addImage(@PathVariable Long id, @RequestParam String imageUrl, RedirectAttributes flash) {
        Course c = get(id);
        String url = imageUrl.trim();
        if (url.length() > 2048 || !url.matches("(?i)^https?://[^\s]+$")) {
            flash.addFlashAttribute("error", "URL ảnh phải bắt đầu bằng http:// hoặc https:// và hợp lệ.");
            return "redirect:/courses/" + id;
        }
        CourseImage img = new CourseImage();
        img.setCourse(c);
        img.setImageUrl(url);
        img.setThumbnail(images.findByCourseId(id).stream().noneMatch(CourseImage::isThumbnail));
        images.save(img);
        flash.addFlashAttribute("success", "Đã thêm ảnh.");
        return "redirect:/courses/" + id;
    }

    @PostMapping("/{id}/images/{imageId}/thumbnail")
    @Transactional
    public String thumbnail(@PathVariable Long id, @PathVariable Long imageId, RedirectAttributes flash) {
        get(id);
        CourseImage chosen = images.findById(imageId).orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh: " + imageId));
        if (!chosen.getCourse().getId().equals(id)) throw new NotFoundException("Ảnh không thuộc khóa học này.");
        for (CourseImage img : images.findByCourseId(id)) img.setThumbnail(img.getId().equals(imageId));
        flash.addFlashAttribute("success", "Đã đổi ảnh đại diện.");
        return "redirect:/courses/" + id;
    }

    @PostMapping("/{id}/images/{imageId}/delete")
    @Transactional
    public String deleteImage(@PathVariable Long id, @PathVariable Long imageId, RedirectAttributes flash) {
        get(id);
        CourseImage img = images.findById(imageId).orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh: " + imageId));
        if (!img.getCourse().getId().equals(id)) throw new NotFoundException("Ảnh không thuộc khóa học này.");
        images.delete(img);
        flash.addFlashAttribute("success", "Đã xóa ảnh.");
        return "redirect:/courses/" + id;
    }

    private Course get(Long id) {
        return courses.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học: " + id));
    }
}
