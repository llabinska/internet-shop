package com.internet.shop.service;


import com.internet.shop.dmo.Account;
import com.internet.shop.dmo.Role;
import com.internet.shop.dmo.User;
import com.internet.shop.dto.account.AccountResponseDto;
import com.internet.shop.dto.user.UserRequestDto;
import com.internet.shop.dto.user.UserResponseDto;
import com.internet.shop.exception.InvalidPasswordException;
import com.internet.shop.exception.UserAlreadyExistsException;
import com.internet.shop.exception.UserNotFoundException;
import com.internet.shop.repository.RoleRepository;
import com.internet.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final String USER_NOT_FOUND_MSG = "User with id = %s not found";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponseDto findByLoginAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username = [" + username + "] not found");
        }

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("The password is invalid!");
        }

        return toUserResponseDto(user);
    }


    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User userFromDB = userRepository.findByUsername(userRequestDto.getUsername());

        if (userFromDB != null) {
            throw new UserAlreadyExistsException("User with username=[" + userRequestDto.getUsername() + "] already exists");
        }

        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));

        Role role = roleRepository.findByName(Role.RoleName.ROLE_USER.name());
        user.setRoles(Collections.singleton(role));

        if (userRequestDto.getAccount() != null) {
            Account account = new Account();
            account.setAmount(userRequestDto.getAccount().getAmount());
            user.setAccount(account);
            account.setUser(user);
        }


        User createdUser = userRepository.save(user);
        return toUserResponseDto(createdUser);
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());

        if (userRequestDto.getAccount() != null) {
            Account account = user.getAccount();
            account.setAmount(userRequestDto.getAccount().getAmount());
        }

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
        if (user.getAccount() != null) {
            userResponseDto.setAccount(toAccountResponseDto(user.getAccount()));
        }
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setUserName(user.getUsername());
        return userResponseDto;
    }

    private AccountResponseDto toAccountResponseDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setAmount(account.getAmount());
        return accountResponseDto;
    }
}
