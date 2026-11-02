package com.sprinkleup.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Values every page can use, such as date formats for ${order.createdAt.format(dateTimeFmt)}. */
@ControllerAdvice
public class ViewHelpers {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMM yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a");

    @ModelAttribute("dateFmt")
    public DateTimeFormatter dateFmt() {
        return DATE;
    }

    @ModelAttribute("dateTimeFmt")
    public DateTimeFormatter dateTimeFmt() {
        return DATE_TIME;
    }

    @ModelAttribute("today")
    public LocalDate today() {
        return LocalDate.now();
    }
}
