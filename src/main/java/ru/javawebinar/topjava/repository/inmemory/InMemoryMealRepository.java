package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository;
    private AtomicInteger counter;
    private int userId;

//    {
//       MealsUtil.MEALS.forEach(meal -> save(meal, userId));
//    }

    public InMemoryMealRepository(int userId) {
        this.userId = userId;
        this.repository = new ConcurrentHashMap<>();
        this.counter = new AtomicInteger(0);
    }

    @Override
    public Meal save(Meal meal, int userId) {
//        if (repository==null) {
//            this.userId = userId;
//            this.repository = new ConcurrentHashMap<>();
//            this.counter = new AtomicInteger(0);
//        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Comparator<Meal> comparator = Comparator.comparing(Meal::getDate);
        if (repository==null) {
            repository = new ConcurrentHashMap<>();
        }
        return repository.values().stream().sorted(comparator.reversed()).collect(Collectors.toList());
    }
}

