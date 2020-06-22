package ru.otus.job14.changelog;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfig {

    @Bean
    public Mongock mongock(MongoClient mongoClient, @Value("${spring.data.mongodb.database}") String dbName) {
        return new SpringMongockBuilder(
                mongoClient,
                dbName,
                DatabaseChangelog.class.getPackage().getName()
                ).build();
    }
}
