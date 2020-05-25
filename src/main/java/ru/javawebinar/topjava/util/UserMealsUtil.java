package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserMealsUtil {

    private static Function<UserMeal, Object> userMealObjectFunction;

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            if (map.containsKey(date)) {
                int cal = map.get(date) + meal.getCalories();
                map.replace(date, cal);
            } else
                map.put(date, meal.getCalories());
        }
         for (UserMeal meal : meals) {
            LocalTime time = meal.getDateTime().toLocalTime();
            boolean isExcess = false;
            if (TimeUtil.isBetweenHalfOpen(time, startTime, endTime)) {
                if (map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                    isExcess = true;
                }
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), isExcess);
                mealsWithExcess.add(userMealWithExcess);
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess>  filteredList = meals.stream().filter(UserMeal -> UserMeal.getDateTime().toLocalTime().isAfter(startTime) &&
                UserMeal.getDateTime().toLocalTime().isBefore(endTime))
                .map(UserMeal -> {
                    return new UserMealWithExcess(UserMeal.getDateTime(), UserMeal.getDescription(), UserMeal.getCalories(), false);
                })
                .collect(Collectors.toList());
        return filteredList;
    }

}
