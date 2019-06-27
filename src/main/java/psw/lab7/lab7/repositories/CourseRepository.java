package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Course findByName(Optional<String> name);
}
