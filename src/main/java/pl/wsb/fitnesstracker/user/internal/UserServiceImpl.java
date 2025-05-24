package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Implementacja logiki CRUD dla użytkowników.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(userId);
        }
    }

    public User updateUser(Long userId, User updated) {
        return userRepository.findById(userId)
                .map(existing -> {
                    existing.setFirstName(updated.getFirstName());
                    existing.setLastName(updated.getLastName());
                    existing.setBirthdate(updated.getBirthdate());
                    existing.setEmail(updated.getEmail());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        // ten override zostaje puste, bo używamy metody fragmentowej:
        return Optional.empty();
    }

    /**
     * Zwraca listę użytkowników, których e-mail zawiera podany fragment (ignorując wielkość liter).
     */
    public List<User> searchByEmailFragment(String fragment) {
        return userRepository.findByEmailIgnoreCaseContaining(fragment);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}



