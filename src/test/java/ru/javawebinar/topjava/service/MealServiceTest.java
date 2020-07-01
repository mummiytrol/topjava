package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    @Autowired
    private MealService service;

    @Test
    public void delete() throws Exception {
        Date startTime = new Date();
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
        Date finishTime = new Date();
        log.info("Test delete. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void deleteNotFound() throws Exception {
        Date startTime = new Date();
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        Date finishTime = new Date();
        log.info("Test deleteNotFound. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void deleteNotOwn() throws Exception {
        Date startTime = new Date();
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
        Date finishTime = new Date();
        log.info("Test deleteNotOwn. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void create() throws Exception {
        Date startTime = new Date();
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
        Date finishTime = new Date();
        log.info("Test create. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void get() throws Exception {
        Date startTime = new Date();
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
        Date finishTime = new Date();
        log.info("Test get. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void getNotFound() throws Exception {
        Date startTime = new Date();
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
        Date finishTime = new Date();
        log.info("Test getNotFound. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void getNotOwn() throws Exception {
        Date startTime = new Date();
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
        Date finishTime = new Date();
        log.info("Test getNotOwn. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void update() throws Exception {
        Date startTime = new Date();
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
        Date finishTime = new Date();
        log.info("Test update. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void updateNotOwn() throws Exception {
        Date startTime = new Date();
        assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
        Date finishTime = new Date();
        log.info("Test updateNotOwn. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void getAll() throws Exception {
        Date startTime = new Date();
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
        Date finishTime = new Date();
        log.info("Test getAll. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        Date startTime = new Date();
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
        Date finishTime = new Date();
        log.info("Test getBetweenInclusive. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        Date startTime = new Date();
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
        Date finishTime = new Date();
        log.info("Test getBetweenWithNullDates. Runtime is {} s", finishTime.getTime() - startTime.getTime());
    }
}