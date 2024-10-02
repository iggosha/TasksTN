package ru.golovkov.taskstn.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovkov.taskstn.mapper.UserMapper;
import ru.golovkov.taskstn.model.dto.request.SignInRequestDto;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.model.entity.User;
import ru.golovkov.taskstn.repository.UserRepository;
import ru.golovkov.taskstn.security.CustomUserDetails;
import ru.golovkov.taskstn.service.UserService;
import ru.golovkov.taskstn.validation.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserSuggestionResponseDto getUserSuggestions(String query) {
        List<UserSuggestionResponseDto.UserSuggestionItem> userSuggestionItemList = new ArrayList<>();
        String[] words = query.split("\\s+");
        if (containsAnyShortWord(words)) {
            return new UserSuggestionResponseDto(userSuggestionItemList);
        }
        userSuggestionItemList.addAll(userMapper.toUserSuggestionItemList(findUsersByWords(words)));
        if (userSuggestionItemList.isEmpty() && ValidationUtils.matchesEmailPattern(query)) {
            userSuggestionItemList.add(getEmailSuggestion(query));
        }
        return new UserSuggestionResponseDto(userSuggestionItemList);
    }

    private boolean containsAnyShortWord(String[] words) {
        for (String word : words) {
            if (word.length() < 3) {
                return true;
            }
        }
        return false;
    }

    private List<User> findUsersByWords(String[] words) {
        List<User> foundUsers = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "fio"));
        if (words.length == 2) {
            foundUsers = userRepository.findByTwoWords(words[0], words[1], pageable);
        } else if (words.length == 1) {
            foundUsers = userRepository.findBySingleWord(words[0], pageable);
        }
        return foundUsers;
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto authenticateAndGetUserResponseDto(SignInRequestDto signInRequestDto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return userMapper.toResponseDto(
                ((CustomUserDetails) authentication.getPrincipal()).getUser()
        );
    }

    private UserSuggestionResponseDto.UserSuggestionItem getEmailSuggestion(String query) {
        UserSuggestionResponseDto.UserSuggestionItem userSuggestionItem
                = new UserSuggestionResponseDto.UserSuggestionItem();
        userSuggestionItem.setValueId(query);
        userSuggestionItem.setValue("");
        return userSuggestionItem;
    }
}
