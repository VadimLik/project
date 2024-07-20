package com.bank.account.controller;

import com.bank.account.exception.AccountDetailsErrorResponce;
import com.bank.account.exception.AccountDetailsNotCreatedException;
import com.bank.account.exception.AccountDetailsNotFoundException;
import com.bank.account.exception.AccountDetailsUpdateException;
import com.bank.account.exception.AuditNotFoundException;
import com.bank.account.exception.NotEnoughMoneyException;
import com.bank.account.models.AccountDetails;
import com.bank.account.models.Audit;
import com.bank.account.service.AccountDetailsService;
import com.bank.account.service.AuditService;
import com.bank.account.validators.AccountDetailsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountDetailsService accountDetailsService;
    private final AuditService auditService;
    private final AccountDetailsValidator validator;

    @Autowired
    public AccountController(AccountDetailsService accountDetailsService, AccountDetailsValidator validator, AuditService auditService) {
        this.accountDetailsService = accountDetailsService;
        this.validator = validator;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDetails>> getAllAccounts(){

        ResponseEntity<List<AccountDetails>> responseEntity = new ResponseEntity<>(accountDetailsService.getAllAccountDetails(), HttpStatus.OK);
        return responseEntity;

    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetails> getAccountDetails(@RequestParam("id") long id){

        ResponseEntity<AccountDetails> response = new ResponseEntity<>(accountDetailsService.getAccountDetails(id), HttpStatus.OK);
        return  response;

    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAccountDetails(@RequestBody @Valid AccountDetails accountDetails,
                                                    BindingResult bindingResult) {

        validator.validate(accountDetails, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new AccountDetailsNotCreatedException(errorMsg.toString());
        } else {
            accountDetailsService.createAccountDetails(accountDetails);
            return ResponseEntity.ok(HttpStatus.OK);
        }

    }

    @PutMapping
    ResponseEntity<HttpStatus> updateAccountDetails(@RequestBody @Valid AccountDetails accountDetails,
                                                    BindingResult bindingResult) {

        validator.validate(accountDetails, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new AccountDetailsNotCreatedException(errorMsg.toString());
        } else {
            accountDetailsService.updateAccountDetails(accountDetails,accountDetails.getId());
            return ResponseEntity.ok(HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteAccountDetails(@RequestParam("id") Long id) {

        accountDetailsService.deleteAccountDetails(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @GetMapping("/audit/{id}")
    public ResponseEntity<Audit> getAudit(@RequestParam("id") Long id) {

        return new ResponseEntity<>(auditService.getAuditById(id), HttpStatus.OK);

    }

    @GetMapping("/audit")
    public ResponseEntity<List<Audit>> getAllAudit() {

        return new ResponseEntity<>(auditService.getAllAudits(), HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(AccountDetailsNotFoundException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce("Аккаунт соответствующего id не найден");
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(AuditNotFoundException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce("Аудит соотвествующего id не найден");
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(AccountDetailsUpdateException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce(e.getMessage());
        return  new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(AccountDetailsNotCreatedException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce(e.getMessage());
        return  new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(NotEnoughMoneyException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce(e.getMessage());
        return  new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<AccountDetailsErrorResponce> handleException(RuntimeException e) {

        AccountDetailsErrorResponce responce = new AccountDetailsErrorResponce(e.getMessage());
        return  new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);

    }
}
