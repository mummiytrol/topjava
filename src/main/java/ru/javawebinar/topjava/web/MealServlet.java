package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<MealTo> meals = MealsUtil.filteredByStreams(MealsUtil.meals, LocalTime.of(0, 0),
                LocalTime.of(23, 59), 2000);

        log.debug("forward to meals");
        req.setAttribute("meals", meals);
        getServletContext().getRequestDispatcher("/meals.jsp").forward(req, resp);


    }
}
