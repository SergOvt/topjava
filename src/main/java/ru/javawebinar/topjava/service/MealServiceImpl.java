package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;
    private static final Logger log = LoggerFactory.getLogger(MealServiceImpl.class);

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        log.info("save meal {} by user id {}", meal.toString(), userId);
        return repository.save(meal, userId);
    }

    @Override
    public void update(Meal meal, int userId) {
        log.info("save meal {} by user id {}", meal.toString(), userId);
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public void delete(int id, int userId) {
        log.info("delete meal id {}", id);
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal id {}", id);
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all meals by user id {}", userId);
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getFilteredAll(LocalDate fromDate, LocalDate toDate, int userId) {
        log.info("get all filtered meals by user id {}", userId);
        return repository.getFilteredAll(fromDate, toDate, userId);
    }



}