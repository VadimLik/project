package com.bank.account.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Pointcuts {

    @Pointcut("execution(public * com.bank.account.service.AccountDetailsServiceImp.create*(..))")
    public void createAccountDetails(){}

    @Pointcut("execution(public * com.bank.account.service.AccountDetailsServiceImp.update*(..))")
    public void updateAccountDetails(){}

    @Pointcut("execution(* com.bank.account..*.*(..))")
    public void allMethods(){}
}


