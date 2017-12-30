package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/ajax/profile/meals")
public class MealAjaxController extends AbstractMealController {

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @PostMapping
    public void createWithLocation(@RequestParam("id") Integer id,
                                   @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                   @RequestParam("description") String description,
                                   @RequestParam("calories") Integer calories) {
        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew()) super.create(meal);
    }

    @PostMapping(value = "/filter")
    public List<MealWithExceed> getBetween(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam("endTime") LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}