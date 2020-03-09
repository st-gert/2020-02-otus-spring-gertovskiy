package ru.otus.job02.model;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Вопрос с ответом.
 * Содержит вопрос, варианты ответов, правильный ответ, ответ экзаменующегося.
 *
 * Конкретные классы реализуют воппросы с выбором ответа из нескольких вариантов или со свободным ответом.
 *
 * @param <T> - тип ответа: Integer для ответов с выбором, String для сободных ответов.
 */
public abstract class QuestionAnswer<T> {

    protected final String question;
    protected final T correctAnswer;

    protected T userAnswer;

    public QuestionAnswer(String question, T correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    // Задать вопрос.
    // Возвращает текст вопроса и максимальный номер варианта ответа
    public abstract Pair<String, Integer> questionToString();

    // Получить ответ
    public void setUserAnswer(T userAnswer) {
        this.userAnswer = userAnswer;
    }

    // Оценить ответ
    abstract boolean isCorrect();

    // Выдать результат
    Triple<String, String, String> questionWithAnswers() {
        return Triple.of(
                question,
                getUserAnswerString(),
                isCorrect() ? null : getCorrectAnswerString()
        );
    }
    protected abstract String getCorrectAnswerString();
    protected abstract String getUserAnswerString();

    // generated getters & setters
    public String getQuestion() {
        return question;
    }
    public T getCorrectAnswer() {
        return correctAnswer;
    }
    public T getUserAnswer() {
        return userAnswer;
    }
}
