package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles(value = {Profiles.JPA})
public class JpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    @Override
    public void get() throws Exception {
        User user = service.get(USER_ID);
        UserTestData.assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), Arrays.asList(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6));
    }
}
