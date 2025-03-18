package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.UserRequestDto;
import cz.upce.fei.nnpia.dto.UserResponseDto;
import cz.upce.fei.nnpia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;


@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDto
    findUser(@PathVariable long id) {
        return userService.findUser(id).toResponseDto();
    }

//    @GetMapping
//    public Collection<User> findUsers(@RequestParam(required = false) String email) {
//        if (email != null) {
//            User user = userService.findUserByEmail(email);
//            if (user == null) {
//                return Collections.emptyList();
//            } else {
//                return Collections.singletonList(user);
//            }
//        }
//        return userService.findUsers();
//    }

//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody UserRequestDto user) {
//        log.info("Request for creating user obtained {}", user);
//
//
//        User createdUser = userService.createUser(user.toUser());
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser.toResponseDto());
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id,@RequestBody UserRequestDto userRequestDto) {
        log.info("Request for updating user obtained {}", userRequestDto);
        User user = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(user.toResponseDto());
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable long id) {
//        log.info("Request for deleting user obtained");
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }

}