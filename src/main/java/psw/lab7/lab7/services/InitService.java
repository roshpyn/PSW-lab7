package psw.lab7.lab7.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psw.lab7.lab7.UserENUM;
import psw.lab7.lab7.models.Course;
import psw.lab7.lab7.models.User;
import psw.lab7.lab7.repositories.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Service
@Slf4j
public class InitService {
    @Autowired
    BlockOfLessonsRepository blockOfLessonsRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    void init(){
        User admin = new User(0L,"admin","admin",
                UserENUM.ADMIN,"admin@admin.admin",
                "Adam","Adamowicz", LocalDate.now());
        admin = userRepository.save(admin);

        User user = new User(0L,"ab","ab",
                UserENUM.TRAINEE,"ab@ab.ab",
                "Jan","Kowalski", LocalDate.now());
        user = userRepository.save(user);


        Course course = new Course(0L,"Test 1");
        course = courseRepository.save(course);

        Course course2 = new Course(0L,"Test 2");
        course2 = courseRepository.save(course2);

    }
}
