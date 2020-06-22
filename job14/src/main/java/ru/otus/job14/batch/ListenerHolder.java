package ru.otus.job14.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сбор статистики записанной информации в реляционную БД.
 */
@Service
public class ListenerHolder {

    private final List<String> statistics = new ArrayList<>();

    public void clear() {
        statistics.clear();
    }

    public String getStatisticsAsString() {
        return statistics.isEmpty()
                ? "Задание не выполнилось"
                : "Записано в БД:\n" + String.join("\n", statistics);
    }

    public StepExecutionListener getStepExecutionListener(String type) {
        return new RegistrationListener(type);
    }

    // Листенер, который зафиксирует количество записанных в БД записей
    public class RegistrationListener implements StepExecutionListener {
        private final String type;
        public RegistrationListener(String type) {
            this.type = type;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
        }
        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            int n = stepExecution.getWriteCount();
            if (n > 0) {
                statistics.add(type + " " + n);
            }
            return null;
        }
    }

}
