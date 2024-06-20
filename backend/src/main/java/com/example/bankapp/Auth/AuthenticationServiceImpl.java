package com.example.bankapp.Auth;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Config.UserAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountService accountService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public AuthenticationServiceImpl(AccountService accountService, UserAuthenticationProvider userAuthenticationProvider) {
        this.accountService = accountService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    public boolean authenticate(Long idAccount, String password) {
        return accountService.authenticateLogin(idAccount, password);
    }

    @Override
    public String createToken(Long idAccount) {
        return userAuthenticationProvider.createToken(idAccount);
    }
}
