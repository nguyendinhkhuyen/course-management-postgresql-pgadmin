package com.example.courses.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalErrors {
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView missing(NotFoundException ex) {
        ModelAndView mv = new ModelAndView("error-friendly");
        mv.addObject("message", ex.getMessage());
        mv.setStatus(org.springframework.http.HttpStatus.NOT_FOUND);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView failure(Exception ex) {
        ModelAndView mv = new ModelAndView("error-friendly");
        mv.addObject("message", "Có lỗi xảy ra. Vui lòng kiểm tra dữ liệu và thử lại.");
        mv.setStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }
}
