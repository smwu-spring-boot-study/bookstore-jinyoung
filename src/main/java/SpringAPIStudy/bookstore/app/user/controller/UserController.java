package SpringAPIStudy.bookstore.app.user.controller;

import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.user.entity.User;
import SpringAPIStudy.bookstore.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User getCurrentUser(@AuthenticationPrincipal CustomUserDetails user) {
        return userRepository.findBysocialId(user.getSocialId())
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
    }

}