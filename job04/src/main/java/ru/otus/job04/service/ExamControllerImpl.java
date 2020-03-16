package ru.otus.job04.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job04.dao.QuestionnaireDAO;
import ru.otus.job04.exception.ApplException;
import ru.otus.job04.model.Person;
import ru.otus.job04.model.QuestionAnswer;
import ru.otus.job04.model.QuestionAnswerChoice;
import ru.otus.job04.model.QuestionAnswerString;
import ru.otus.job04.model.Questionnaire;
import ru.otus.job04.ui.UIExamScenario;

import java.util.List;
import java.util.Locale;

/**
 * Сценарий проведения экзамена.
 */
@Service
public class ExamControllerImpl implements ExamController {

    private final QuestionnaireDAO questionnaireDAO;
    private final UIExamScenario uiScenario;
    private final MarksCriteria marksCriteria;
    private Person person;

    public ExamControllerImpl(QuestionnaireDAO questionnaireDAO, UIExamScenario uiScenario,
                              MarksCriteria marksCriteria) {
        this.questionnaireDAO = questionnaireDAO;
        this.uiScenario = uiScenario;
        this.marksCriteria = marksCriteria;
    }

    // Получаем данные об экзаменующемся
    @Override
    public void setPerson(Person person) {
        this.person = person;
    }

    // Сценарий экзамена
    @Override
    public void execScenario(Locale locale) {
        // Формируем вопросник
        Questionnaire questionnaire = questionnaireDAO.createQuestionnaire(locale);
        // Объявляем наименование экзамена
        uiScenario.setLocale(locale);
        uiScenario.outputBeginingInfo(questionnaire.getExamName());
        // Задаем вопросы
        // тип ? - тип ответа пользователя, String или Integer (extends невозможен)
        List<QuestionAnswer<?>> questionsAnswerList = questionnaire.getQuestionsAnswerList();
        int quantity = questionsAnswerList.size();
        int number = 0;
        for (QuestionAnswer<?> question : questionsAnswerList) {
            Pair<String, Integer> pair = question.questionToString();
            String txtAnswers = (++number) + "/" + quantity + ". " + pair.getLeft();
            if (question instanceof QuestionAnswerString) {
                ((QuestionAnswerString) question).setUserAnswer(uiScenario.askQuestionString(txtAnswers));
            } else if (question instanceof QuestionAnswerChoice) {
                ((QuestionAnswerChoice) question).setUserAnswer(uiScenario.askQuestionChoice(txtAnswers, pair.getRight()));
            } else {
                throw new ApplException("Неизвестный тип вопроса");
            }
        }
        // Выводим правильные ответы
        uiScenario.outputAnswers(questionnaire.questionWithAnswersList());
        // вычисляем процент правильных ответов и оценку за экзамен
        int percent = questionnaire.result();
        uiScenario.outputResult(person.toString(), percent, marksCriteria.calcMark(percent));
    }

    // generated getters & setters
    public QuestionnaireDAO getQuestionnaireDAO() {
        return questionnaireDAO;
    }
    public UIExamScenario getUiScenario() {
        return uiScenario;
    }
    public MarksCriteria getMarksCriteria() {
        return marksCriteria;
    }
}
