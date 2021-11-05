package com.internet.shop.mapper;

import com.internet.shop.dmo.Account;
import com.internet.shop.dto.account.AccountResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter {

    public AccountResponseDto toAccountResponseDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setAmount(account.getAmount());
        return accountResponseDto;
    }

}
