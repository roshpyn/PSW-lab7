package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
