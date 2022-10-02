package SpringAPIStudy.bookstore.app.auth.respository;

import SpringAPIStudy.bookstore.app.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) {
        User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    };

}
