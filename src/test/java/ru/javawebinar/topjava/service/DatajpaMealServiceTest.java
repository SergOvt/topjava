package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(value = Profiles.DATAJPA)
public class DatajpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void testGetWithUser() throws Exception {
        Meal actual = service.getWithUser(MEAL1_ID, USER_ID);
        MealTestData.assertMatch(actual, MEAL1);
        UserTestData.assertMatch(actual.getUser(), USER);
    }

    @Test
    public void testGetWithUserNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }

}
