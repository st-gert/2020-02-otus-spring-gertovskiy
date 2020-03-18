package ru.otus.job04.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Вычисление оценки за экзамен по проценту правильных ответов.
 */
@Service
@ConfigurationProperties(prefix = "criteria")
public class MarksCriteria {

    // Параметры приложения из application.yml
    // Загружаются автоматически (@ConfigurationProperties)
    private int excellent;
    private int good;
    private int satisfactory;

    private final Map<String, Integer> criteria = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        criteria.put("criteria.excel", excellent);
        criteria.put("criteria.good", good);
        criteria.put("criteria.satisf", satisfactory);
        criteria.put("criteria.bad", 0);
    }

    /**
     * Вычисляет оценку за экзамен по проценту правильных ответов.
     * @param percent процент правильных ответов.
     * @return оценка в текстовом формате.
     */
    public String calcMark(Integer percent) {
        return criteria.entrySet()
                .stream()
                .filter(x -> x.getValue() <= percent)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get()
                ;
    }

    // generated gettera & setters
    public int getExcellent() {
        return excellent;
    }
    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }
    public int getGood() {
        return good;
    }
    public void setGood(int good) {
        this.good = good;
    }
    public int getSatisfactory() {
        return satisfactory;
    }
    public void setSatisfactory(int satisfactory) {
        this.satisfactory = satisfactory;
    }
}
