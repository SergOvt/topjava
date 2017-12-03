package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(value = {Profiles.JPA})
public class JpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    @Override
    public void testGet() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MealTestData.assertMatch(actual, ADMIN_MEAL1);
        UserTestData.assertMatch(actual.getUser(), ADMIN);
    }
}
