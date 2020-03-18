package ru.otus.job04.ui;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class UIExamShellPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Otus-Exam:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }

}
