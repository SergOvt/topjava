package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
public class MealRestController{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;


    public Meal save(Meal meal) {
        log.info("save {}", meal.toString());
        return service.save(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public List<MealWithExceed> getFilteredAll(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        log.info("getFilteredAll for id {}", AuthorizedUser.id());
        return service.getAll(fromDate, toDate, fromTime, toTime);
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll for id {}", AuthorizedUser.id());
        return service.getAll(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);

    }
}