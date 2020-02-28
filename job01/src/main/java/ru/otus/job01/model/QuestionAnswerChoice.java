package ru.otus.job01.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Воппрос с выбором ответа из нескольких вариантов.
 */
public class QuestionAnswerChoice extends QuestionAnswer<Integer> {

    private final List<String> answerVariants;

    public QuestionAnswerChoice(String question, int correctAnswer, List<String> answerVariants) {
        super(question, correctAnswer);
        this.answerVariants = answerVariants;
    }

    // Задать вопрос
    // Возвращает текст вопроса и максимальный номер варианта ответа
    @Override
    public Pair<String, Integer> questionToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(question);
        for (String variant: answerVariants) {
            sb.append("\n    ").append(answerVariants.indexOf(variant) + 1).append(". ").append(variant);
        }
        return Pair.of(sb.toString(), answerVariants.size());
    }

    // Оценить ответ
    @Override
    boolean isCorrect() {
        return this.correctAnswer.equals(this.userAnswer);
    }


    // Выдать результат
    @Override
    protected String getCorrectAnswerString() {
        return answerVariants.get(correctAnswer-1);
    }

    @Override
    protected String getUserAnswerString() {
        return answerVariants.get(userAnswer-1);
    }


    // generated getters & setters
    public List<String> getAnswerVariants() {
        return answerVariants;
    }

}
