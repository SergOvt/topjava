package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDaoVirtual implements MealDao {

    private static AtomicInteger countId = new AtomicInteger();
    private final List<Meal> MEALS = new CopyOnWriteArrayList<>();
    private static final Logger log = getLogger(MealDaoVirtual.class);

    {
        create(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        create(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        create(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        create(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        create(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        create(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    }

    @Override
    public void create(LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal (countId.incrementAndGet(),dateTime, description, calories);
        MEALS.add(meal);
        log.info("Meal ID " + meal.getId() + " created");
    }

    @Override
    public Meal get(int id) {
        Meal meal = MEALS.get(getExistedIndex(id));
        log.info("Meal ID " + id + " readed");
        return meal;
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        Meal meal = get(id);
        meal.setDateTime(dateTime);
        meal.setDescription(description);
        meal.setCalories(calories);
        log.info("Meal ID " + id + " updated");
    }

    @Override
    public void delete(int id) {
        MEALS.remove(getExistedIndex(id));
        log.info("Meal ID " + id + " deleted");
    }

    @Override
    public List<Meal> getAll() {
        log.info("Get list of meals");
        return new ArrayList<>(MEALS);
    }


    private int getExistedIndex(int id) {
        for (int i = 0; i < MEALS.size(); i++) {
            if (MEALS.get(i).getId() == id) {
                return i;
            }
        }
        log.warn("ID " + id + " is not exist");
        throw new IllegalArgumentException("ID is not exist");
    }

}
