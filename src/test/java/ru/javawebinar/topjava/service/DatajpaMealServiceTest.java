package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;


@ActiveProfiles(value = Profiles.DATAJPA)
public class DatajpaMealServiceTest extends AbstractMealServiceTest {}
