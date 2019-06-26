package psw.lab7.lab7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import psw.lab7.lab7.models.BlockOfLessons;

public interface BlockOfLessonsRepository extends JpaRepository<BlockOfLessons,Long> {
}
