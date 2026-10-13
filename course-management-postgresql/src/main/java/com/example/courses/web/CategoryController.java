package com.example.courses.web;

import com.example.courses.model.CourseCategory;
import com.example.courses.repo.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categories;
    private final CourseRepository courses;

    public CategoryController(CategoryRepository c, CourseRepository r) {
        categories = c;
        courses = r;
    }

    @GetMapping
    public String list(Model m) {
        m.addAttribute("categories", categories.findAll());
        m.addAttribute("category", new CourseCategory());
        return "categories/list";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute CourseCategory category, BindingResult errors, Model m, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            m.addAttribute("categories", categories.findAll());
            return "categories/list";
        }
        categories.save(category);
        flash.addFlashAttribute("success", "Đã lưu loại khóa học.");
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model m) {
        m.addAttribute("category", categories.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy loại khóa học: " + id)));
        return "categories/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute CourseCategory category, BindingResult errors) {
        CourseCategory existing = categories.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy loại khóa học: " + id));
        if (errors.hasErrors()) return "categories/form";
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        categories.save(existing);
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        CourseCategory cat = categories.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy loại khóa học: " + id));
        if (courses.existsByCategoryId(id)) {
            flash.addFlashAttribute("error", "Không thể xóa loại khóa học đang có khóa học.");
            return "redirect:/categories";
        }
        categories.delete(cat);
        flash.addFlashAttribute("success", "Đã xóa loại khóa học.");
        return "redirect:/categories";
    }
}
