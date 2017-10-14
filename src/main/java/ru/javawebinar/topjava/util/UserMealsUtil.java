package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> result = getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
                LocalTime.of(23, 0), 2000);

    }


    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesPerDayMap = mealList.stream().collect(Collectors.toMap(
                o -> o.getDateTime().toLocalDate(), UserMeal::getCalories, (a, b) -> a + b));

        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();

        for (UserMeal meal : mealList) { // Как можно это сделать стримом?
            Predicate<Integer> predicate = calories -> calories > caloriesPerDay;
            mealWithExceedList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                    predicate.test(caloriesPerDayMap.get(meal.getDateTime().toLocalDate()))));
        }

        return mealWithExceedList.stream().filter(meal ->
                meal.getDateTime().toLocalTime().isAfter(startTime) && meal.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toList());
    }


    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            if (caloriesPerDayMap.containsKey(date))
                caloriesPerDayMap.put(date, caloriesPerDayMap.get(date) + meal.getCalories());
            else caloriesPerDayMap.put(date, meal.getCalories());
        }

        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();
        for (UserMeal meal : mealList) {
            Predicate<Integer> predicate = calories -> calories > caloriesPerDay;
            mealWithExceedList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                    predicate.test(caloriesPerDayMap.get(meal.getDateTime().toLocalDate()))));
        }

        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMealWithExceed meal : mealWithExceedList) {
            LocalTime time = meal.getDateTime().toLocalTime();
            if(time.isAfter(startTime) && time.isBefore(endTime)) resultList.add(meal);
        }

        return resultList;
    }

    }
