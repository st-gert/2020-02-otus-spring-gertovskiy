package ru.otus.job14.shell;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job14.batch.ListenerHolder;
import ru.otus.job14.model.jpa.BookJpa;
import ru.otus.job14.repository.BookJpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class BatchShell {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final ListenerHolder listenerHolder;
    private final BookJpaRepository bookJpaRepository;

    public BatchShell(JobLauncher jobLauncher, Job job,
                      ListenerHolder listenerHolder, BookJpaRepository bookJpaRepository) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.listenerHolder = listenerHolder;
        this.bookJpaRepository = bookJpaRepository;
    }

    // Старт процедуры миграции данных из БД Mongo в реляционную БД
    @ShellMethod(value = "Start of the migration procedure.", key = {"start", "migrate"})
    public String migrate() throws JobExecutionException {
        listenerHolder.clear();
        jobLauncher.run(job, new JobParameters());
        return listenerHolder.getStatisticsAsString();
    }

    // Чтение списка книг из реляционной БД
    @ShellMethod(value = "Reading a books list from the relational DB.", key = {"read-sql", "rs"})
    @Transactional
    public String readJpa () {
        List<BookJpa> books = bookJpaRepository.findAll();
        return "Список книг из реляционной БД:\n\n" +
                books.stream()
                .sorted(Comparator.comparing(BookJpa::getTitle))
                .map(BookJpa::toString)
                .collect(Collectors.joining("\n\n"))
                ;
    }

}
