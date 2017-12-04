package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(value = {Profiles.DATAJPA})
public class DatajpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        UserTestData.assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), Arrays.asList(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6));
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealsNotFound() throws Exception {
        service.get(1);
    }

    @Test(expected = NotFoundException.class)
    public void getWithEmptyMeals() throws Exception {
        User admin = service.getWithMeals(ADMIN_ID);
        UserTestData.assertMatch(admin, ADMIN);
        Assert.assertEquals(admin.getMeals().size(), 0);
    }

}
