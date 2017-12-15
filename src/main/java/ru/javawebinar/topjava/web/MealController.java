package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealController extends RootController{

    @GetMapping("/mealForm")
    private String get(HttpServletRequest request) {
        Meal meal;
        if (request.getParameter("id") == null) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            int userId = AuthorizedUser.id();
            int id = getId(request);
            log.info("get meal {} for user {}", id, userId);
            meal = mealService.get(id, userId);
        }
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = AuthorizedUser.id();
        int id = getId(request);
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        return "redirect:meals";
    }

    @PostMapping("/mealForm")
    public String save(HttpServletRequest request) {
        int userId = AuthorizedUser.id();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String paramId = request.getParameter("id");
        if (paramId.isEmpty()) {
            log.info("create {} for user {}", meal, userId);
            mealService.create(meal, userId);
        }
        else {
            int id = Integer.valueOf(paramId);
            assureIdConsistent(meal, id);
            log.info("update {} for user {}", meal, userId);
            mealService.update(meal, userId);
        }
        return "redirect:meals";
    }

    @PostMapping("/meals")
    public String getBetween(HttpServletRequest request) {
        int userId = AuthorizedUser.id();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = mealService.getBetweenDates(
                startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId);

        List<MealWithExceed> mealsWithExceedFiltered =  MealsUtil.getFilteredWithExceeded(mealsDateFiltered,
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        request.setAttribute("meals", mealsWithExceedFiltered);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}