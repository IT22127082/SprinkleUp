package com.sprinkleup.controller;

import com.sprinkleup.exception.ForbiddenException;
import com.sprinkleup.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Shows a friendly page instead of a stack trace. */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e, Model model) {
        model.addAttribute("heading", "Crumbs! Nothing here.");
        model.addAttribute("message", e.getMessage());
        return "error/not-found";
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbidden(ForbiddenException e, Model model) {
        model.addAttribute("heading", "Staff only");
        model.addAttribute("message", e.getMessage());
        return "error/not-found";
    }
}
