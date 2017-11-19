package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL1_ID, USER_ID);
        assertMatch(meal, MEAL1);

    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        service.delete(MEAL2_ID, USER_ID);
        service.delete(MEAL3_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4);
    }

    @Test
    public void getBetweenDates() throws Exception {
        List<Meal> meals = service.getBetweenDates(DATE, DATE, USER_ID);
        assertMatch(meals, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(DATE, TIME_START), LocalDateTime.of(DATE, TIME_END), USER_ID);
        assertMatch(meals, MEAL3, MEAL2);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Updated");
        updated.setCalories(0);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void create() throws Exception {
        Meal meal = new Meal(LocalDateTime.of(2017, 11, 10, 23, 0), "Тест", 1000);
        Meal createdMeal = service.create(meal, USER_ID);
        meal.setId(createdMeal.getId());
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, createdMeal, MEAL3, MEAL2, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal meal = service.get(MEAL1_ID, USER_ID);
        meal.setId(1);
        service.update(meal , USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getForeign() throws Exception {
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeign() throws Exception {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() throws Exception {
        service.update(MEAL1, ADMIN_ID);
    }

}