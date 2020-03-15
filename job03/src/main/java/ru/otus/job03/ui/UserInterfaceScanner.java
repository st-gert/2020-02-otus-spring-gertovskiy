package ru.otus.job03.ui;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.job03.model.Person;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Реализация пользовательского интерфейса с использованием класса Scanner.
 */

@Service
public class UserInterfaceScanner implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);

    private final MessageSource ms;
    private final Locale locale;

    public UserInterfaceScanner(MessageSource ms,
                                @Value("${file.locale}") Locale locale) {   // параметр приложения из application.yml
        this.ms = ms;
        this.locale = locale;
    }

    private String trans(String code) {
        return ms.getMessage(code, null, locale);
    }

    @Override
    public Person inputPerson() {
        outputString(trans("person.introduce") + ".\n" + trans("person.firstname") + ":");
        String firstName = inputString();
        outputString(trans("person.lastnamme") + ":");
        String lastName = inputString();
        return new Person(firstName, lastName);
    }

    @Override
    public void outputBeginingInfo(String examName) {
        outputString("\n" + trans("begin") + "\n" + examName);
    }

    @Override
    public String askQuestionString(String question) {
        outputString("\n" + question);
        outputString(trans("answer.string") + ":");
        return inputString();
    }

    @Override
    public int askQuestionChoice(String question, int maxNumer) {
        outputString("\n" + question);
        outputString(trans("answer.choice") + ":");
        return inputInt(maxNumer);
    }

    @Override
    public void outputAnswers(List<Triple<String, String, String>> answers) {
        outputString("\n" + trans("result.result"));
        for (Triple<String, String, String> triple : answers) {
            outputString(trans("result.question") + ": " + triple.getLeft());
            outputString("    " + trans("result.answer") + ": " + triple.getMiddle());
            String correct = triple.getRight();
            if(correct == null) {
                outputString("    " + trans("result.correct"));
            } else {
                outputString("    " + trans("result.error") + ": " + correct);
            }
        }
    }

    @Override
    public void outputResult(String personName, int percent, String mark) {
        outputString(trans("mark.percent") + ": " + percent);
        outputString("\n" + personName + ", " + trans("mark.mark") + ": " + trans(mark));
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
                outputString(trans("answer.repeat"));
            }
            scanner.nextLine();
        }
    }

    @Override
    public void close() {
        scanner.close();
    }

    // genegated getters & setters
    public MessageSource getMs() {
        return ms;
    }
    public Locale getLocale() {
        return locale;
    }
}
