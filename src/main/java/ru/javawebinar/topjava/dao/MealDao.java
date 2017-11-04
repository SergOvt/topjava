package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public interface MealDao {
    public static final AtomicInteger countId = new AtomicInteger();

    void create(Meal meal);
    Meal get(int id);
    void update(Meal meal);
    void delete(int id);
    List<Meal> getAll();
}
