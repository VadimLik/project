package com.bank.account.aspect;

import com.bank.account.exception.AccountDetailsNotCreatedException;
import com.bank.account.exception.NotEnoughMoneyException;
import com.bank.account.models.AccountDetails;
import com.bank.account.models.Audit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

/**
 * Аспект для логирования,отслеживания исключений и аудирования модуля account,
 * применительно к микросервисной архитектуре Spring Boot приложения.
 */

@Component
@Aspect
@Slf4j
public class AccountAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccountAspect.class);

    /**
    Логирование старта методов:
    */

    @Before("com.bank.account.aspect.Pointcuts.allMethods()")

    public void beforeAllMethods(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("Старт метода " + methodSignature);

    }

    /**
    Логирование успешного завершения методов:
    */

    @AfterReturning("com.bank.account.aspect.Pointcuts.allMethods()")

    public void AfterReturningAllMethods(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("Успешно завершен метод " + methodSignature);

    }

    /**
    Логирование исключений:
    */

    @AfterThrowing(pointcut = "com.bank.account.aspect.Pointcuts.allMethods()", throwing = "exception")
    public void AfterTrowingAllMethods(JoinPoint joinPoint, Throwable exception) {

        logger.info("Выброшено исключение : " + exception);
        logger.info(exception.getMessage());

    }

    /**
    Аудирование метода create*:
    */

    @Around("com.bank.account.aspect.Pointcuts.createAccountDetails()")
    public  Object AroundCreateMethods(ProceedingJoinPoint proceedingJoinPoint) {

        ObjectMapper objectMapper = new ObjectMapper();
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (e instanceof AccountDetailsNotCreatedException) {
                throw new AccountDetailsNotCreatedException(e.getMessage());
            }else {
                throw new RuntimeException(e.getMessage());
            }
        }
        AccountDetails accountDetails = (AccountDetails) result;
        logger.info("Создаем Audit");
        Audit audit = new Audit();
        audit.setEntityType(accountDetails.getClass().getSimpleName());
        audit.setOperationType(proceedingJoinPoint.getSignature().getName());
        audit.setCreatedBy("Создано пользователем с profileId = " + accountDetails.getProfileId());
        audit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        try {
            audit.setEntityJson(objectMapper.writeValueAsString(accountDetails));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Не удалось получить JSON у %s", accountDetails.getClass().getSimpleName()));
        }
        accountDetails.setAudit(audit);
        logger.info("Присвоили AccountDetails созданное значение Audit ");
        return result;

    }

    /**
    Аудирование метода update*:
    */

    @Around("com.bank.account.aspect.Pointcuts.updateAccountDetails()")
    public  Object AroundUpdateMethods(ProceedingJoinPoint proceedingJoinPoint) {

        ObjectMapper objectMapper = new ObjectMapper();
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (e instanceof NotEnoughMoneyException) {
                throw new NotEnoughMoneyException(e.getMessage());
            }else {
                throw new RuntimeException(e.getMessage());
            }
        }
        AccountDetails accountDetails = (AccountDetails) result;
        logger.info("Вносим изменения в Audit");
        Audit audit = accountDetails.getAudit();
        audit.setModifiedBy("Изменено пользователем с profileId = " + accountDetails.getProfileId());
        audit.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        try {
            accountDetails.setAudit(null);
            audit.setNewEntityJson(objectMapper.writeValueAsString(accountDetails));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Не удалось получить JSON у %s", accountDetails.getClass().getSimpleName()));
        }
        accountDetails.setAudit(audit);
        logger.info("Присвоили AccountDetails измененное значение Audit ");
        return result;

    }
}


