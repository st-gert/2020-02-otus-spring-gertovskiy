package ru.otus.job02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.job02.service.ExamController;

@Configuration
@ComponentScan
public class Main02 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main02.class);
        ExamController examController = (ExamController) context.getBean("examController");
        examController.execScenario();
    }

}
