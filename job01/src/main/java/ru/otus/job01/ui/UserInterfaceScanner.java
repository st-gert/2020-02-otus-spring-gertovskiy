package ru.otus.job01.ui;

import org.apache.commons.lang3.tuple.Triple;
import ru.otus.job01.model.Person;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInterfaceScanner implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Person inputPreson() {
        Person person = new Person();
        outputString("Представьтесь, пожалуйста.\nВведите Ваше имя:");
        person.setFirstName(inputString());
        outputString("Введите Вашу фамилию:");
        person.setLastName(inputString());
        return person;
    }

    @Override
    public void outputBeginingInfo(String examName) {
        outputString("\nНАЧИНАЕМ ЭКЗАМЕН\n" + examName);
    }

    @Override
    public String askQuestionString(String question) {
        outputString("\n" + question);
        outputString("Введите ответ:");
        return inputString();
    }

    @Override
    public int askQuestionChoice(String question, int maxNumer) {
        outputString("\n" + question);
        outputString("Введите номер правильного ответа:");
        return inputInt(maxNumer);
    }

    @Override
    public void outputAnswers(List<Triple<String, String, String>> answers) {
        outputString("\nРЕЗУЛЬТАТЫ");
        for (Triple<String, String, String> triple : answers) {
            outputString("Вопрос: " + triple.getLeft());
            outputString("    Ответ: " + triple.getMiddle());
            String correct = triple.getRight();
            if(correct == null) {
                outputString("    Правильно");
            } else {
                outputString("    Ошибка. Правильный ответ: " + correct);
            }
        }
    }

    @Override
    public void outputResult(String personName, int percent, String mark) {
        outputString("Процент правильных ответов: " + percent);
        outputString("\n" + personName + ", Ваша оценка: " + mark);
    }


    private void outputString(String txt) {
        System.out.println(txt);
    }

    private String inputString() {
        return scanner.next();
    }

    private int inputInt(int rangeTo) {
        Pattern pattern = Pattern.compile("[1-" + rangeTo + "]");
        scanner.nextLine();
        while (true) {
            if (scanner.hasNext(pattern)) {
                String s = scanner.next(pattern);
                return Integer.parseInt(s);
            } else {
                outputString("Недопустимый ответ. Повторите, плз.");
            }
            scanner.nextLine();
        }
    }

    @Override
    public void close() {
        scanner.close();
    }

}
