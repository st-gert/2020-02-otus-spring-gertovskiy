package ru.otus.job02.service;

import org.springframework.stereotype.Service;
import ru.otus.job02.config.ConfigProps;

import java.util.HashMap;
import java.util.Map;

@Service
public class MarksCriteria {

    private final Map<Integer, String> criteria = new HashMap<>();

    public MarksCriteria(ConfigProps configProps) {
        criteria.put(configProps.getCriteriaExcel(), "criteria.excel");
        criteria.put(configProps.getCriteriaGood(), "criteria.good");
        criteria.put(configProps.getCriteriaSatisfl(), "criteria.satisf");
        criteria.put(0, "criteria.bad");
    }

    public String calcMark(Integer percent) {
        return criteria.entrySet()
                .stream()
                .filter(x -> x.getKey() <= percent)
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .get()
                ;
    }
}
//  5 - excellent mark, 4 - good mark, 3 - satisfactory mark, 2 - bad mark; U — провал (unclassified).