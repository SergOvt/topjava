package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    Meal save(Meal meal);

    void delete(int id);

    Meal get(int id);

    List<MealWithExceed> getAll(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime);
}