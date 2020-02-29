package ru.otus.job01;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.job01.service.ExamController;

public class Main01 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-context.xml");
        ExamController examController = (ExamController) ctx.getBean("examController");
        examController.execScenario();
    }

}
