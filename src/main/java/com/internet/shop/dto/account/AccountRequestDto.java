package com.internet.shop.dto.account;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class AccountRequestDto {

    @NotNull(message = "{amount.not.null}")
    @Range(min = 1, message = "{amount.size.zero}")
    private long amount;

}
