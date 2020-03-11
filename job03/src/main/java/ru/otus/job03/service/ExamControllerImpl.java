package ru.otus.job03.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job03.dao.QuestionnaireDAO;
import ru.otus.job03.exception.ApplException;
import ru.otus.job03.model.*;
import ru.otus.job03.ui.UserInterface;

import java.util.List;

/**
 * Сценарий проведения экзамена.
 */
@Service("examController")
public class ExamControllerImpl implements ExamController {

    private final QuestionnaireDAO questionnaireDAO;
    private final UserInterface ui;
    private final MarksCriteria marksCriteria;

    public ExamControllerImpl(QuestionnaireDAO questionnaireDAO, UserInterface ui, MarksCriteria marksCriteria) {
        this.questionnaireDAO = questionnaireDAO;
        this.ui = ui;
        this.marksCriteria = marksCriteria;
    }

    @Override
    public void execScenario() {
        // Формируем вопросник
        Questionnaire questionnaire = questionnaireDAO.createQuestionnaire();
        // Получаем данные об экзаменующемся
        Person person = ui.inputPerson();
        // Объявляем наименование экзамена
        ui.outputBeginingInfo(questionnaire.getExamName());
        // Задаем вопросы
        // тип ? - тип ответа пользователя, String или Integer (extends невозможен)
        List<QuestionAnswer<?>> questionsAnswerList = questionnaire.getQuestionsAnswerList();
        int quantity = questionsAnswerList.size();
        int number = 0;
        for (QuestionAnswer<?> question : questionsAnswerList) {
            Pair<String, Integer> pair = question.questionToString();
            String txtAnswers = (++number) + "/" + quantity + ". " + pair.getLeft();
            if (question instanceof QuestionAnswerString) {
                ((QuestionAnswerString) question).setUserAnswer(ui.askQuestionString(txtAnswers));
            } else if (question instanceof QuestionAnswerChoice) {
                ((QuestionAnswerChoice) question).setUserAnswer(ui.askQuestionChoice(txtAnswers, pair.getRight()));
            } else {
                throw new ApplException("Неизвестный тип вопроса");
            }
        }
        // Выводим правильные ответы
        ui.outputAnswers(questionnaire.questionWithAnswersList());
        // вычисляем процент правильных ответов и оценку за экзамен
        int percent = questionnaire.result();
        ui.outputResult(person.toString(), percent, marksCriteria.calcMark(percent));
        ui.close();
    }

    // generated getters & setters
    public QuestionnaireDAO getQuestionnaireDAO() {
        return questionnaireDAO;
    }
    public UserInterface getUi() {
        return ui;
    }
    public MarksCriteria getMarksCriteria() {
        return marksCriteria;
    }
}
