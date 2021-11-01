package com.internet.shop.service;


import com.internet.shop.dmo.Account;
import com.internet.shop.dmo.User;
import com.internet.shop.dto.account.AccountResponseDto;
import com.internet.shop.dto.user.UserRequestDto;
import com.internet.shop.dto.user.UserResponseDto;
import com.internet.shop.exception.UserNotFoundException;
import com.internet.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final String USER_NOT_FOUND_MSG = "User with id = %s not found";

    private final UserRepository userRepository;


    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setName(userRequestDto.getName());

        Account account = new Account();
        account.setAmount(userRequestDto.getAccount().getAmount());

        user.setAccount(account);
        account.setUser(user);

        User createdUser = userRepository.save(user);
        return toUserResponseDto(createdUser);
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        user.setEmail(userRequestDto.getEmail());
        user.setName(userRequestDto.getName());

        Account account = user.getAccount();
        account.setAmount(userRequestDto.getAccount().getAmount());


        User updatedUser = userRepository.save(user);
        return toUserResponseDto(updatedUser);
    }

    public UserResponseDto get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        return toUserResponseDto(user);
    }

    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserResponseDto);
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


    private UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setAccount(toAccountResponseDto(user.getAccount()));
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setName(user.getName());
        return userResponseDto;
    }

    private AccountResponseDto toAccountResponseDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setAmount(account.getAmount());
        return accountResponseDto;
    }
}
