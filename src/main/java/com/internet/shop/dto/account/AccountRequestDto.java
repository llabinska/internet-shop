package com.internet.shop.dto.account;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class AccountRequestDto {

    @Range(min = 1, message = "{amount.size.zero}")
    private long amount;

}
