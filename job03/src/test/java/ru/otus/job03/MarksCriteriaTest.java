package ru.otus.job03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job03.service.MarksCriteria;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarksCriteriaTest {

    private MarksCriteria criteria;

    @BeforeEach
    void createMarksCriteria() {
        criteria = new MarksCriteria();
        criteria.setExcellent(100);
        criteria.setGood(75);
        criteria.setSatisfactory(50);
        criteria.postConstruct();
    }

    @Test
    @DisplayName("Проверка вычисления оценок по проценту")
    public void criteriaTest() {
        assertAll(
                () -> assertEquals("criteria.bad", criteria.calcMark(0)),
                () -> assertEquals("criteria.bad", criteria.calcMark(10)),
                () -> assertEquals("criteria.bad", criteria.calcMark(20)),
                () -> assertEquals("criteria.bad", criteria.calcMark(30)),
                () -> assertEquals("criteria.bad", criteria.calcMark(40)),
                () -> assertEquals("criteria.satisf", criteria.calcMark(50)),
                () -> assertEquals("criteria.satisf", criteria.calcMark(60)),
                () -> assertEquals("criteria.satisf", criteria.calcMark(70)),
                () -> assertEquals("criteria.good", criteria.calcMark(75)),
                () -> assertEquals("criteria.good", criteria.calcMark(80)),
                () -> assertEquals("criteria.good", criteria.calcMark(90)),
                () -> assertEquals("criteria.excel", criteria.calcMark(100))
        );
    }
}
