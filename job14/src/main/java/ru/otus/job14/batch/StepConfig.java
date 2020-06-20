package ru.otus.job14.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.job14.model.jpa.AuthorJpa;
import ru.otus.job14.model.jpa.BookJpa;
import ru.otus.job14.model.jpa.GenreJpa;
import ru.otus.job14.model.jpa.ReviewJpa;
import ru.otus.job14.model.mongo.AuthorMongo;
import ru.otus.job14.model.mongo.BookMongo;
import ru.otus.job14.model.mongo.GenreMongo;
import ru.otus.job14.model.mongo.ReviewMongo;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
public class StepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory emf;
    private final ListenerHolder listenerHolder;

    public StepConfig(StepBuilderFactory stepBuilderFactory, MongoTemplate mongoTemplate,
                      EntityManagerFactory emf, ListenerHolder listenerHolder) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.mongoTemplate = mongoTemplate;
        this.emf = emf;
        this.listenerHolder = listenerHolder;
    }

    private <T> MongoItemReader<T> reader(Class<T> clazz) {
        return new MongoItemReaderBuilder<T>()
                .name("reader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(clazz)
                .sorts((new HashMap<>()))
                .build()
                ;
    }

    private  <T> JpaItemWriter<T> writer() {
        return new JpaItemWriterBuilder<T>()
                .entityManagerFactory(emf)
                .build()
                ;
    }

    @Bean
    public Step stepGenre() {
        return stepBuilderFactory
                .get("stepGenre")
                .<GenreMongo, GenreJpa>chunk(5)
                .reader(reader(GenreMongo.class))
                .processor((ItemProcessor<GenreMongo, GenreJpa>) GenreJpa::new)
                .writer(writer())
                .listener(listenerHolder.getStepExecutionListener("Жанров"))
                .build()
                ;
    }

    @Bean
    public Step stepAuthor() {
        return stepBuilderFactory
                .get("stepAuthor")
                .<AuthorMongo, AuthorJpa>chunk(5)
                .reader(reader(AuthorMongo.class))
                .processor((ItemProcessor<AuthorMongo, AuthorJpa>) AuthorJpa::new)
                .writer(writer())
                .listener(listenerHolder.getStepExecutionListener("Авторов"))
                .build()
                ;
    }

    @Bean
    public Step stepBook() {
        return stepBuilderFactory
                .get("stepBook")
                .<BookMongo, BookJpa>chunk(5)
                .reader(reader(BookMongo.class))
                .processor((ItemProcessor<BookMongo, BookJpa>) BookJpa::new)
                .writer(writer())
                .listener(listenerHolder.getStepExecutionListener("Книг"))
                .build()
                ;
    }

    @Bean
    public Step stepReview() {
        return stepBuilderFactory
                .get("stepReview")
                .<ReviewMongo, ReviewJpa>chunk(5)
                .reader(reader(ReviewMongo.class))
                .processor((ItemProcessor<ReviewMongo, ReviewJpa>) ReviewJpa::new)
                .writer(writer())
                .listener(listenerHolder.getStepExecutionListener("Отзывов"))
                .build()
                ;
    }

}
