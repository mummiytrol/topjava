package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;


@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealService service;
    private final int userId = SecurityUtil.authUserId();

    @Autowired
    public void setService(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return service.getAll(userId);
    }

    public Meal create(Meal meal) {
        return service.create(meal, userId);
    }

    public void delete(int id) {
        service.delete(id, userId);
    }

    public Meal get(int id) {
        return service.get(id, userId);
    }

    public Meal update(Meal meal) {
        return service.update(meal, userId);
    }
}