package ru.otus.job02.model;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Воппрос со свободным ответом.
 */
public class QuestionAnswerString extends QuestionAnswer<String> {

    public QuestionAnswerString(String question, String correctAnswer) {
        super(question, correctAnswer);
    }

    // Задать вопрос
    // Возвращает текст вопроса и максимальный номер варианта ответа
    @Override
    public Pair<String, Integer> questionToString() {
        return Pair.of(question, null);
    }

    // Оценить ответ
    @Override
    boolean isCorrect() {
        return this.correctAnswer.equalsIgnoreCase(this.userAnswer.trim());
    }

    // Выдать результат
    @Override
    protected String getCorrectAnswerString() {
        return correctAnswer;
    }

    @Override
    protected String getUserAnswerString() {
        return userAnswer;
    }

}
