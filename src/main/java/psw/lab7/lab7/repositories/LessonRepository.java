package psw.lab7.lab7.repositories;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.Lesson;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
