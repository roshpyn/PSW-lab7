package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.Course;
import psw.lab7.lab7.models.User;
import psw.lab7.lab7.models.UserApplication;

import java.util.List;

public interface UserApplicationRepository extends JpaRepository<UserApplication,Long> {
    List<UserApplication> findByCourse(Course course);
    List<UserApplication> findByUser(User user);
}
