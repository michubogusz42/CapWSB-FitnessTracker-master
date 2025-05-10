package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    // GET /v1/users
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET /v1/users/simple
    @GetMapping("/simple")
    public List<UserDto> getAllSimpleUsers() {
        return userService.findAllUsers().stream()
                .map(u -> {
                    UserDto dto = userMapper.toDto(u);
                    return new UserDto(null, dto.firstName(), dto.lastName(), null, null);
                })
                .collect(Collectors.toList());
    }

    // GET /v1/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /v1/users/email?email=…
    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET /v1/users/older/{time}
    @GetMapping("/older/{time}")
    public List<UserDto> getUsersOlderThan(
            @PathVariable("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userService.findAllUsers().stream()
                .filter(u -> u.getBirthdate().isBefore(date))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // POST /v1/users
    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody UserDto userDto) {
        userService.createUser(userMapper.toEntity(userDto));
        return ResponseEntity.status(201).build();
    }

    // PUT /v1/users/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto userDto) {
        userService.updateUser(userId, userMapper.toEntity(userDto));
        return ResponseEntity.ok().build();
    }

    // DELETE /v1/users/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
