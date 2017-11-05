package ru.javawebinar.topjava.web;

import org.slf4j.*;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoVirtual;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealDao mealDao;

    @Override
    public void init() throws ServletException {
        mealDao = new MealDaoVirtual();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        forwardMeals(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                try {
                    mealDao.create(getMeal(request));
                } catch (Exception e) {
                    log.debug("Incorrect data from add form");
                }
                break;
            case "delete":
                mealDao.delete(getId(request));
                break;
            case "readyUpdate":
                request.setAttribute("updatingMeal", mealDao.get(getId(request)));
                break;
            case "update":
                try {
                    Meal meal = getMeal(request);
                    meal.setId(getId(request));
                    mealDao.update(meal);
                } catch (Exception e) {
                    log.debug("Incorrect data from update form");
                }
        }
        forwardMeals(request, response);
    }

    private void forwardMeals (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", MealsUtil.getListWithExceeded(mealDao.getAll(), 2000));
        request.setAttribute("dateTimeFormatter", DateTimeFormatter.ofPattern("dd.MM.yyyy, hh:mm"));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private int getId(HttpServletRequest request){
        return Integer.parseInt(request.getParameter("id"));
    }

    private Meal getMeal(HttpServletRequest request) {
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        return new Meal(dateTime, description, calories);
    }

}
