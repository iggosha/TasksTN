package ru.golovkov.taskstn.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovkov.taskstn.mapper.UserMapper;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.model.entity.User;
import ru.golovkov.taskstn.repository.UserRepository;
import ru.golovkov.taskstn.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAll() {
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    @Override
    public UserSuggestionResponseDto getUserSuggestions(String query) {
        UserSuggestionResponseDto userSuggestionResponseDto = new UserSuggestionResponseDto();
        List<UserSuggestionResponseDto.UserSuggestionItem> userSuggestionItemList = userSuggestionResponseDto.getUserSuggestionItemList();
        String[] words = query.split("\\s+");
        for (String word : words) {
            if (word.length() < 3) {
                return userSuggestionResponseDto;
            }
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "fio"));
        List<User> foundUsers = new ArrayList<>();
        if (words.length == 2) {
            foundUsers = userRepository.findByTwoWords(words[0], words[1], pageable);
        } else if (words.length == 1) {
            foundUsers = userRepository.findBySingleWord(words[0], pageable);
        }
        userSuggestionItemList.addAll(foundUsers
                .stream()
                .map(user -> {
                    UserSuggestionResponseDto.UserSuggestionItem userSuggestionItem = new UserSuggestionResponseDto.UserSuggestionItem();
                    userSuggestionItem.setValueId(user.getEmail());
                    userSuggestionItem.setValue(user.getFio());
                    userSuggestionItem.setDirectoryType("USER");
                    return userSuggestionItem;
                })
                .toList());
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (foundUsers.isEmpty() && Pattern.matches(emailRegex, query)) {
            userSuggestionItemList.add(getEmailSuggestion(query));
        }
        return userSuggestionResponseDto;
    }

    private UserSuggestionResponseDto.UserSuggestionItem getEmailSuggestion(String query) {
        UserSuggestionResponseDto.UserSuggestionItem userSuggestionItem = new UserSuggestionResponseDto.UserSuggestionItem();
        userSuggestionItem.setValueId(query);
        userSuggestionItem.setValue("");
        return userSuggestionItem;
    }
}
