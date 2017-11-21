package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int MEAL2_ID = START_SEQ + 3;
    public static final int MEAL3_ID = START_SEQ + 4;
    public static final int MEAL4_ID = START_SEQ + 5;
    public static final int MEAL5_ID = START_SEQ + 6;
    public static final int MEAL6_ID = START_SEQ + 7;

    public static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2017, 11, 10, 10, 0),"Завтрак", 500);
    public static final Meal MEAL2 = new Meal(MEAL2_ID, LocalDateTime.of(2017, 11, 10, 14, 0),"Обед", 1000);
    public static final Meal MEAL3 = new Meal(MEAL3_ID, LocalDateTime.of(2017, 11, 10, 19, 0),"Ужин", 500);
    public static final Meal MEAL4 = new Meal(MEAL4_ID, LocalDateTime.of(2017, 11, 11, 10, 0),"Завтрак", 500);
    public static final Meal MEAL5 = new Meal(MEAL5_ID, LocalDateTime.of(2017, 11, 11, 14, 0),"Обед", 1000);
    public static final Meal MEAL6 = new Meal(MEAL6_ID, LocalDateTime.of(2017, 11, 11, 19, 0),"Ужин", 550);

    public static final LocalDate DATE = LocalDate.of(2017, 11, 10);
    public static final LocalTime TIME_START = LocalTime.of(14, 0);
    public static final LocalTime TIME_END = LocalTime.of(19, 0);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(tuple(actual)).isEqualToComparingFieldByField(tuple(expected));
    }
}
