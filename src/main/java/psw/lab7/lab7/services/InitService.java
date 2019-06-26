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
                UserENUM.ADMIN,"Adam","Adamowicz", LocalDate.now());
        admin = userRepository.save(admin);
    }
}
