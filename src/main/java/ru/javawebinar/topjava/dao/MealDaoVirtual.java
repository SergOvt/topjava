package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDaoVirtual implements MealDao {

    private final Map<Integer, Meal> MEALS = new ConcurrentHashMap<>();
    private static final AtomicInteger countId = new AtomicInteger();
    private static final Logger log = getLogger(MealDaoVirtual.class);

    {
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void create(Meal meal) {
        int id = countId.incrementAndGet();
        meal.setId(id);
        MEALS.put(id, meal);
        log.info("Meal ID " + countId + " created");
    }

    @Override
    public Meal get(int id) {
        Meal meal = MEALS.get(getExistedKey(id));
        log.info("Meal ID " + id + " readed");
        return meal;
    }

    @Override
    public void update(Meal meal) {
        MEALS.put(getExistedKey(meal.getId()), meal);
        log.info("Meal ID " + meal.getId() + " updated");
    }

    @Override
    public void delete(int id) {
        MEALS.remove(getExistedKey(id));
        log.info("Meal ID " + id + " deleted");
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> resultList = new ArrayList<>(MEALS.values());
        Collections.sort(resultList);
        log.info("Get list of meals");
        return resultList;
    }


    private int getExistedKey(int id) {
        if (MEALS.containsKey(id)) return id;
        log.warn("ID " + id + " is not exist");
        throw new IllegalArgumentException("ID is not exist");
    }

}
