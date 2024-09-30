package ru.golovkov.taskstn.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import ru.golovkov.taskstn.model.dto.request.SignInRequestDto;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.security.CustomUserDetails;
import ru.golovkov.taskstn.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/sign-up")
    public UserResponseDto signUp(@RequestBody UserRequestDto userRequestDto) {
        return userService.create(userRequestDto);
    }

    @PostMapping("/sign-in")
    public UserResponseDto signIn(@ParameterObject SignInRequestDto signInRequestDto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return userService.getUserResponseDtoFromUserDetails((CustomUserDetails) authentication.getPrincipal()) ;
    }

    @GetMapping("/suggestions")
    public UserSuggestionResponseDto getUserSuggestions(@RequestParam String query) {
        return userService.getUserSuggestions(query);
    }

    @GetMapping("/current")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserResponseDtoFromUserDetails(userDetails);
    }
}
