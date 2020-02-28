package ru.otus.job01.model.dict;

import java.util.HashMap;
import java.util.Map;

public class MarksCriteria {

    private final Map<Integer, String> criteria = new HashMap<>();
    private final String badMark = "НЕУД. (необходимо увидеться с деканом)";

    {
        criteria.put(100, "ОТЛИЧНО");
        criteria.put(75, "ХОРОШО");
        criteria.put(50, "УДОВЛЕТВОРИТЕЛЬНО");
    }

    public String calcMark(Integer percent) {
        return criteria.entrySet()
                .stream()
                .filter(x -> x.getKey() <= percent)
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(badMark)
                ;
    }
}
//  5 - excellent mark, 4 - good mark, 3 - satisfactory mark, 2 - bad mark; U — провал (unclassified).