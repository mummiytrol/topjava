package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {


        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            repository.put(meal.getUserId(), meals);
            return meal;
        }
        if (meal.getUserId() == userId) return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        return null;
        // handle case: update, but not present in storage
  //      return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (meals.get(id).getUserId() == userId) {
            meals.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
//        return meals.values().stream()
//                .filter(meal -> meal.getUserId() == userId)
//                .findFirst().orElse(null);
        return repository.get(userId).get(id);

    }

    @Override
    public List<Meal> getAll(int userId) {
        Comparator<Meal> comparator = Comparator.comparing(Meal::getDate);
//        return repository.get(userId).values().stream().sorted(comparator.reversed()).collect(Collectors.toList());
        return meals.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }
}

