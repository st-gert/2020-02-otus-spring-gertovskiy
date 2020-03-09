package ru.otus.job02.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.job02.Main02;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageSourceTest {
    private static final AnnotationConfigApplicationContext CTX = new AnnotationConfigApplicationContext(Main02.class);
    private static final Locale LOCALE = new Locale("xx", "XX");

    @Test
    @DisplayName("Тест i18n")
    public void i18nTest() {
        MessageSource ms = (MessageSource) CTX.getBean("messageSource");
        String s = ms.getMessage("result.result", null, LOCALE);
        assertEquals("XX RESULTS", s);
    }
}
