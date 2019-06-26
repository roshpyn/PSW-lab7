package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.Course;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
