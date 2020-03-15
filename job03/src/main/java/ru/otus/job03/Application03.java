package ru.otus.job03;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.otus.job03.service.ExamController;

@SpringBootApplication
public class Application03 {

    public static void main(String[] args) {
        ApplicationContext ctx = new SpringApplication(Application03.class).run(args);
        ExamController examController = (ExamController) ctx.getBean("examController");
        examController.execScenario();
    }

}
