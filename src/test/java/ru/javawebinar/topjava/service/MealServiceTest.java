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
        long nanoTime = System.nanoTime();
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test delete. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void deleteNotFound() throws Exception {
        long nanoTime = System.nanoTime();
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test deleteNotFound. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void deleteNotOwn() throws Exception {
        long nanoTime = System.nanoTime();
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test deleteNotOwn. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void create() throws Exception {
        long nanoTime = System.nanoTime();
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test create. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void get() throws Exception {
        long nanoTime = System.nanoTime();
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test get. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void getNotFound() throws Exception {
        long nanoTime = System.nanoTime();
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test getNotFound. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void getNotOwn() throws Exception {
        long nanoTime = System.nanoTime();
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test getNotOwn. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void update() throws Exception {
        long nanoTime = System.nanoTime();
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test update. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void updateNotOwn() throws Exception {
        long nanoTime = System.nanoTime();
        assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test updateNotOwn. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void getAll() throws Exception {
        long nanoTime = System.nanoTime();
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test getAll. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        long nanoTime = System.nanoTime();
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test getBetweenInclusive. Runtime is {} ms", (double) nanoTime/1000000d);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        long nanoTime = System.nanoTime();
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
        nanoTime = System.nanoTime() - nanoTime;
        log.info("Test getBetweenWithNullDates. Runtime is {} ms", (double) nanoTime/1000000d);
    }
}