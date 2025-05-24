package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.util.List;

/**
 * Repozytorium dla encji User.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Wyszukuje użytkowników, których email zawiera dany fragment (case-insensitive).
     *
     * @param emailFragment fragment wyszukiwanej części adresu e-mail
     * @return lista pasujących użytkowników
     */
    List<User> findByEmailIgnoreCaseContaining(String emailFragment);

}

