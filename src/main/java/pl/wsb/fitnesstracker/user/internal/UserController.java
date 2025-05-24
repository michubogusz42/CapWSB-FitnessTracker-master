package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-owy kontroler CRUD dla użytkowników.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/simple")
    public List<UserDto> getAllSimpleUsers() {
        return userService.findAllUsers().stream()
                .map(u -> {
                    var d = userMapper.toDto(u);
                    return new UserDto(null, d.firstName(), d.lastName(), null, null);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Wyszukuje po fragmencie emaila (case-insensitive).
     *
     * GET /v1/users/email?email=frag
     */
    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam("email") String fragment) {
        return userService.searchByEmailFragment(fragment).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/older/{date}")
    public List<UserDto> getUsersOlderThan(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userService.findAllUsers().stream()
                .filter(u -> u.getBirthdate().isBefore(date))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody UserDto dto) {
        userService.createUser(userMapper.toEntity(dto));
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto dto) {
        userService.updateUser(userId, userMapper.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

