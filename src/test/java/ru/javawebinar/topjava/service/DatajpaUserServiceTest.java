package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = {Profiles.DATAJPA})
public class DatajpaUserServiceTest extends AbstractUserServiceTest {

}
