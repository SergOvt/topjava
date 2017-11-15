package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500), 1);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000), 1);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500), 1);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000), 1);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500), 1);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510), 1);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }
        else if (get(meal.getId(), userId) == null) return null;
        return repository.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && userId == meal.getUserId() ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> meals =  repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .collect(Collectors.toList());
        meals.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        return meals;
    }

    @Override
    public List<Meal> getFilteredAll(LocalDate fromDate, LocalDate toDate, int userId) {
        return getFilteredByDate(getAll(userId), fromDate, toDate);
    }

    private List<Meal> getFilteredByDate (Collection<Meal> meals, LocalDate startDate, LocalDate endDate) {
        LocalDate fromDate = startDate == null ? LocalDate.MIN : startDate;
        LocalDate toDate = endDate == null ? LocalDate.MAX : endDate;

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), fromDate, toDate))
                .collect(Collectors.toList());
    }

}

