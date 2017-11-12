package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.*;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);


    {
        MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal == null) return null;
        if (AuthorizedUser.id() != meal.getUserId()) throw new NotFoundException("user id " + AuthorizedUser.id() + " is incorrect");
        if (meal.isNew()) meal.setId(counter.incrementAndGet());
        repository.put(meal.getId(), meal);
        log.info("save meal {} by user id {}", meal.toString(), meal.getUserId());
        return meal;
    }

    @Override
    public boolean delete(int id) {
        Meal meal = repository.remove(id);
        if (meal == null) return false;
        if (AuthorizedUser.id() != meal.getUserId()) throw new NotFoundException("user id " + AuthorizedUser.id() + " is incorrect");
        log.info("delete meal {} by user id {}", meal.toString(), meal.getUserId());
        return true;
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        if (meal == null) return null;
        if (AuthorizedUser.id() != meal.getUserId()) throw new NotFoundException("user id " + AuthorizedUser.id() + " is incorrect");
        log.info("get meal {}", meal.toString());
        return meal;
    }

    @Override
    public List<MealWithExceed> getAll(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        log.info("get all filtered meals by user id {}", AuthorizedUser.id());
        List<Meal> meals =  repository.values().stream()
                .filter(meal -> meal.getUserId() == AuthorizedUser.id())
                .collect(Collectors.toList());

        List<MealWithExceed> mealsWithExceed = getFilteredWithExceeded(meals, fromDate, toDate, fromTime, toTime,
                AuthorizedUser.getCaloriesPerDay());
        mealsWithExceed.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        return  mealsWithExceed;
    }

}

