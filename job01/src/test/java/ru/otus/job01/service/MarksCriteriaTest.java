package ru.otus.job01.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job01.model.dict.MarksCriteria;

import static org.junit.jupiter.api.Assertions.*;

public class MarksCriteriaTest {

    @Test
    @DisplayName("Проверка вычисления оценок по проценту")
    public void criteriaTest() {
        MarksCriteria criteria = new MarksCriteria();
        assertAll(
                () -> assertEquals("НЕУД. (необходимо увидеться с деканом)", criteria.calcMark(0)),
                () -> assertEquals("НЕУД. (необходимо увидеться с деканом)", criteria.calcMark(10)),
                () -> assertEquals("НЕУД. (необходимо увидеться с деканом)", criteria.calcMark(20)),
                () -> assertEquals("НЕУД. (необходимо увидеться с деканом)", criteria.calcMark(30)),
                () -> assertEquals("НЕУД. (необходимо увидеться с деканом)", criteria.calcMark(40)),
                () -> assertEquals("УДОВЛЕТВОРИТЕЛЬНО", criteria.calcMark(50)),
                () -> assertEquals("УДОВЛЕТВОРИТЕЛЬНО", criteria.calcMark(60)),
                () -> assertEquals("УДОВЛЕТВОРИТЕЛЬНО", criteria.calcMark(70)),
                () -> assertEquals("ХОРОШО", criteria.calcMark(75)),
                () -> assertEquals("ХОРОШО", criteria.calcMark(80)),
                () -> assertEquals("ХОРОШО", criteria.calcMark(90)),
                () -> assertEquals("ОТЛИЧНО", criteria.calcMark(100))
        );
    }
}
