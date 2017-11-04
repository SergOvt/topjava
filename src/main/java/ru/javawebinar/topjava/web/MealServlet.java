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


public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final MealDao mealDao = new MealDaoVirtual();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to users");
        request.setAttribute("meals", MealsUtil.getListWithExceeded(mealDao.getAll(), 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Action from meals: ");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            try {
                int calories = Integer.parseInt(request.getParameter("calories"));
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
                String description = request.getParameter("description");
                mealDao.create(new Meal(MealDao.countId.incrementAndGet(), dateTime, description, calories));
            } catch (Exception e) {
                log.debug("Incorrect data from add form");
            }
        }

        if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                mealDao.delete(id);
        }

        if ("readyUpdate".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("updatingMeal", mealDao.get(id));
        }

        if ("update".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                int calories = Integer.parseInt(request.getParameter("calories"));
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
                String description = request.getParameter("description");
                Meal meal = new Meal (id, dateTime, description, calories);
                mealDao.update(meal);
            } catch (Exception e) {
                log.debug("Incorrect data from update form");
            }
        }
        request.setAttribute("meals", MealsUtil.getListWithExceeded(mealDao.getAll(), 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
