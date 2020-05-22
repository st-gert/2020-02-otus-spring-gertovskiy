package ru.otus.job10.service.impl;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.otus.job10.exception.ApplDbConstraintException;

/**
 * Обработка Exception, выбрасываемых при завершении транзакции.
 */
@Aspect
@Component
@Order(0)
public class ConstraintExceptionHandlerAop {

    @AfterThrowing(
            pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)",
            throwing = "error"
    )
    public void handle(Throwable error) throws Throwable {
        if (error instanceof DataIntegrityViolationException) {
            throw new ApplDbConstraintException();
        } else {
            throw error;
        }
    }
}
