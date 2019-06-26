package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);
}
