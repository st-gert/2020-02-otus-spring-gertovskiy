package ru.otus.job04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    private MessageSource ms;

    @Test
    @DisplayName("Тест i18n")
    public void i18nTest() {
        String s = ms.getMessage("result.result", null, new Locale("xx", "XX"));
        assertEquals("XX RESULTS", s);
    }
}
