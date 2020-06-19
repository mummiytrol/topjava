package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealService service;

    @Autowired
    public void setService(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<MealTo> meals = MealsUtil.getFilteredTos(service.getAll(SecurityUtil.authUserId()),
                MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDateTime().toLocalDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal create(Meal meal) {
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        Meal meal = service.get(id, userId);
        if (meal == null) throw new NotFoundException("Not found meal with id="+id);
        if  (meal.getUserId() != userId) throw new NotFoundException("Meal belongs to another user");
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        Meal meal = service.get(id, userId);
        if (meal == null) throw new NotFoundException("Not found meal with id="+id);
        if  (meal.getUserId() != userId) throw new NotFoundException("Meal belongs to another user");
        return meal;
    }

    public Meal update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        if (meal == null) throw new NotFoundException("Not found meal with id="+id);
        if  (meal.getUserId() != userId) throw new NotFoundException("Meal belongs to another user");
        return service.update(meal, SecurityUtil.authUserId());
    }

}