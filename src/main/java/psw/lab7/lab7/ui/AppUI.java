package psw.lab7.lab7.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import psw.lab7.lab7.UserENUM;
import psw.lab7.lab7.models.Course;
import psw.lab7.lab7.models.User;
import psw.lab7.lab7.models.UserApplication;
import psw.lab7.lab7.repositories.*;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@SpringUI
public class AppUI extends UI {
    private byte incorrectLogIn = 0;
    private User loggedUser = null;

    @Autowired
    private BlockOfLessonsRepository blockOfLessonsRepository;

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

    private boolean checkEmail(String email) {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    TabSheet tabsheet;
    @Override
    protected void init(VaadinRequest vaadinRequest){
        tabsheet = new TabSheet();



        initLoginTab();
        tabsheet.addTab(tabLogin,"Log in");
        initSignUpTab();
        tabsheet.addTab(tabSignUp,"Sign up");

        //initTraineeTab();
        //tabsheet.addTab(traineeTabSheet,"Trainee view");


        tabsheet.setSizeFull();
        setContent(tabsheet);

    }

    //tab login

    private GridLayout tabLogin = new GridLayout(3,3);
    private TextField loginLoginTextField;

    private TextField loginPasswordTextField;
    private PasswordField loginPasswordField;
    private CheckBox loginShowPasswordCheckBox;
    private Button loginButton;
    private void initLoginTab(){
        FormLayout formLayout = new FormLayout();
        loginLoginTextField = new TextField("Login:");
        formLayout.addComponent(loginLoginTextField);

        loginPasswordTextField = new TextField("Password:");
        loginPasswordTextField.setVisible(false);
        formLayout.addComponent(loginPasswordTextField);

        loginPasswordField = new PasswordField("Password:");
        formLayout.addComponent(loginPasswordField);

        loginShowPasswordCheckBox = new CheckBox("Show password");
        formLayout.addComponent(loginShowPasswordCheckBox);

        loginButton = new Button("Log in");
        formLayout.addComponent(loginButton);

        tabLoginListeners();
        tabLogin.setSizeFull();
        tabLogin.addComponent(formLayout,1,1);
    }
    private void tabLoginListeners() {
        loginShowPasswordCheckBox.addValueChangeListener(l ->{
            boolean value = loginShowPasswordCheckBox.getValue();
            loginPasswordTextField.setVisible(value);
            loginPasswordField.setVisible(!value);
            if(value){
                loginPasswordTextField.setValue(loginPasswordField.getValue());
                return;
            }
            loginPasswordField.setValue(loginPasswordTextField.getValue());

        });
        loginButton.addClickListener(l ->{
            if(incorrectLogIn >= 2){
                loginButton.setDisableOnClick(true);
                return;
            }

            String login = loginLoginTextField.getValue();
            String password = (loginShowPasswordCheckBox.getValue())?loginPasswordTextField.getValue():loginPasswordField.getValue();

            if(login.isEmpty() || password.isEmpty()){
                incorrectLogIn++;
                return;
            }

            User user;
            try {
                user = userRepository.findByLogin(login);

            }
            catch (Exception e){
                incorrectLogIn++;
                return;
            }

            if(!user.getPassword().equals(password)){
                incorrectLogIn++;
                return;
            }



            switch (user.getType()){
                case ADMIN:
                    Notification.show("Succesfull! Logged as ADMINISTRATOR",Notification.Type.WARNING_MESSAGE);
                    loggedUser = user;
                    initAdminTab();
                    tabsheet.addTab(adminTabSheet,"Admin view");
                    tabsheet.setSelectedTab(adminTabSheet);
                    break;
                case TRAINER:
                    initTrainerTab();
                    tabsheet.addTab(trainerTabSheet,"Trainer view");
                    tabsheet.setSelectedTab(trainerTabSheet);
                    break;
                case TRAINEE:
                    Notification.show("Logged as TRAINEE!",Notification.Type.WARNING_MESSAGE);
                    loggedUser = user;
                    initTraineeTab();
                    tabsheet.addTab(traineeTabSheet,"Trainee view");
                    tabsheet.setSelectedTab(traineeTabSheet);
                    break;
            }

        });
    }
    //tab sign up
    private GridLayout tabSignUp = new GridLayout(3,3);

    private TextField registerNameTextField;
    private TextField registerSurnameTextField;
    private TextField registerLoginTextField;
    private TextField registerEmailTextField;
    private TextField registerPassword1TextField;
    private PasswordField registerPassword1Field;
    private TextField registerPassword2TextField;
    private PasswordField registerPassword2Field;
    private CheckBox registerShowPasswordCheckBox;
    private Button registerButton;

    private void initSignUpTab(){
        FormLayout formLayout = new FormLayout();
        registerNameTextField = new TextField("Name:");
        registerSurnameTextField = new TextField("Surname:");
        registerLoginTextField = new TextField("Login:");
        registerEmailTextField = new TextField("Email:");
        registerPassword1Field = new PasswordField("Password:");
        registerPassword2Field = new PasswordField("Retype password:");
        registerPassword1TextField = new TextField("Password:");
        registerPassword2TextField = new TextField("Retype password:");
        registerPassword1TextField.setVisible(false);
        registerPassword2TextField.setVisible(false);
        registerShowPasswordCheckBox = new CheckBox("Show password");
        registerButton = new Button("Sign up");
        formLayout.addComponents(registerNameTextField,
                registerSurnameTextField,
                registerEmailTextField,
                registerLoginTextField,
                registerPassword1Field,registerPassword2Field,
                registerPassword1TextField,registerPassword2TextField,
                registerShowPasswordCheckBox,
                registerButton);
        tabSignUpListeners();
        tabSignUp.setSizeFull();
        tabSignUp.addComponent(formLayout,1,1);
    }
    private void tabSignUpListeners() {
        registerShowPasswordCheckBox.addValueChangeListener( l -> swapPasswordTextAndViability());
        registerButton.addClickListener( l -> signUp());
    }
    private void swapPasswordTextAndViability(){
        boolean value = registerShowPasswordCheckBox.getValue();

        registerPassword1TextField.setVisible(value);
        registerPassword2TextField.setVisible(value);
        registerPassword1Field.setVisible(!value);
        registerPassword2Field.setVisible(!value);

        if(!value){
            registerPassword1Field.setValue(registerPassword1TextField.getValue());
            registerPassword2Field.setValue(registerPassword2TextField.getValue());
            return;
        }
        registerPassword1TextField.setValue(registerPassword1Field.getValue());
        registerPassword2TextField.setValue(registerPassword2Field.getValue());


    }
    private void signUp(){
        String login = registerLoginTextField.getValue();
        try {
            User eqistingUser = userRepository.findByLogin(login);
            if(eqistingUser.getLogin().equals(login)) {
                Notification.show("Username taken!", Notification.Type.ERROR_MESSAGE);
                return;
            }
        }
        catch (Exception e){
            System.out.println("Null ptr exception expected!");
        }

        boolean value = registerShowPasswordCheckBox.getValue();
        String password1 = (value)?registerPassword1TextField.getValue():registerPassword1Field.getValue();
        String password2 = (value)?registerPassword2TextField.getValue():registerPassword2Field.getValue();
        if(!password1.equals(password2)){
            Notification.show("Passwords don't match!", Notification.Type.ERROR_MESSAGE);
            return;
        }
        String email = registerEmailTextField.getValue();
        if(!checkEmail(email)){
            Notification.show("Email is not valid", Notification.Type.ERROR_MESSAGE);
            return;
        }
        String name = registerNameTextField.getValue();
        if(name.equals("")){
            Notification.show("Name is not filled", Notification.Type.ERROR_MESSAGE);
            return;
        }
        String surname = registerSurnameTextField.getValue();
        if(surname.equals("")){
            Notification.show("Name is not filled", Notification.Type.ERROR_MESSAGE);
            return;
        }
        User user = new User(0L,login,password1, UserENUM.TRAINEE,email,name,surname, LocalDate.now());
        userRepository.save(user);
        Notification.show("Successfull signed", Notification.Type.WARNING_MESSAGE);
    }
    // tab adminView
    //TODO
    private TabSheet adminTabSheet = new TabSheet();
    private GridLayout adminTabAddTrainer= new GridLayout(3,3);
    private Label adminTabAddTrainerLabel;
    private TextField adminTabAddTrainerLogin;
    private TextField adminTabAddTrainerPassword;
    private TextField adminTabAddTrainerEmail;
    private TextField adminTabAddTrainerName;
    private TextField adminTabAddTrainerSurame;
    private Button adminTabAddTrainerButton;

    private GridLayout adminTabConfirmApplication = new GridLayout(3,3);
    private ComboBox<UserApplication> adminTabConfirmApplicationComboBox;
    private List<UserApplication> userApplications;
    private Button adminTabConfirmApplicationConfirmButton;
    private Button adminTabConfirmApplicationDeleteButton;
    
    private void initAdminTab() {
        initAdminTabAddTrainer();
        initadminTabConfirmApplication();
        adminTabSheet.setSizeFull();
    }

    private void initadminTabConfirmApplication() {
        FormLayout formLayout = new FormLayout();
        adminTabSheet.addTab(adminTabConfirmApplication,"Confirm participation");
        userApplications = userApplicationRepository.findAll();
        adminTabConfirmApplicationComboBox = new ComboBox<>("Application",userApplications);
        adminTabConfirmApplicationComboBox.setSizeFull();
        adminTabConfirmApplicationConfirmButton = new Button("CONFIRM");
        adminTabConfirmApplicationDeleteButton = new Button("DELETE");
        formLayout.addComponents(adminTabConfirmApplicationComboBox,
                adminTabConfirmApplicationConfirmButton,
                adminTabConfirmApplicationDeleteButton
                );
        adminTabConfirmApplication.addComponent(formLayout,1,1);
        adminTabConfirmApplication.setSizeFull();
    }

    private void initAdminTabAddTrainer(){
        FormLayout formLayout = new FormLayout();
        adminTabSheet.addTab(adminTabAddTrainer,"Add trainer");
        adminTabAddTrainerLabel = new Label("Add new trainer".toUpperCase());
        adminTabAddTrainerLogin = new TextField("Login:");
        adminTabAddTrainerPassword = new TextField("Password:");
        adminTabAddTrainerEmail = new TextField("Email:");
        adminTabAddTrainerName = new TextField("Name:");
        adminTabAddTrainerSurame = new TextField("Surname:");
        adminTabAddTrainerButton = new Button("ADD");
        formLayout.addComponents(adminTabAddTrainerLabel,
                adminTabAddTrainerName,adminTabAddTrainerSurame,
                adminTabAddTrainerLogin,adminTabAddTrainerPassword,
                adminTabAddTrainerEmail,
                adminTabAddTrainerButton);
        adminTabAddTrainer.addComponent(formLayout,1,1);
        adminTabAddTrainer.setSizeFull();

        AdminTabAddTrainerListeners();
    }

    private void AdminTabAddTrainerListeners() {
        adminTabAddTrainerButton.addClickListener( l ->{
           String login = adminTabAddTrainerLogin.getValue();
           String password = adminTabAddTrainerPassword.getValue();
           String email = adminTabAddTrainerEmail.getValue();
           String name = adminTabAddTrainerName.getValue();
           String surname = adminTabAddTrainerSurame.getValue();
           boolean err = false;
           if(login.isEmpty()) err = true;
           if(password.isEmpty()) err = true;
           if(email.isEmpty()) err = true;
           if(checkEmail(email)) err = true;
           if(name.isEmpty()) err = true;
           if(surname.isEmpty()) err = true;

           User user = new User(0L,
                   login,password,
                   UserENUM.TRAINER,email,
                   name,surname,LocalDate.now());
            user = userRepository.save(user);
        });
    }

    // tab trainerView
    //TODO
    private TabSheet trainerTabSheet = new TabSheet();

    private void initTrainerTab() {
        //trainerTabSheet.setVisible(false);
        adminTabSheet.setSizeFull();
    }

    // tab traineeView
    //TODO now
    private TabSheet traineeTabSheet = new TabSheet();
    private GridLayout userAplication = new GridLayout(3,3);

    private Label userApplicationLabel = new Label("Apply for the course");
    private List<Course> courses = null;
    private List<String> coursesNames = new ArrayList<>();
    private ComboBox<String> courseComboBox;
    private Button traineeApplyButton = new Button("APPLY");
    private Course selectedCourse = null;

    private void initTraineeTab() {
        //traineeTabSheet.setVisible(false);
        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(userApplicationLabel);
        initApplyComboBox();
        courseComboBox = new ComboBox<>("Course:",coursesNames);
        courseComboBox.setPlaceholder("No course selected");
        formLayout.addComponent(courseComboBox);
        traineeApplyButton.setDisableOnClick(true);
        formLayout.addComponent(traineeApplyButton);
        adminTabSheet.setSizeFull();
        userAplication.addComponent(formLayout,1,1);
        userAplication.setSizeFull();
        traineeTabSheet.addTab(userAplication,"Aplly");

        courseComboBox.addValueChangeListener( l -> {
            traineeApplyButton.setDisableOnClick(false);
            try {
                selectedCourse = (Course) courseRepository.findByName(courseComboBox.getSelectedItem());
            }
            catch (Exception e){
                System.out.println(e);
            }
        });
        traineeApplyButton.addClickListener( l -> {
            List<UserApplication>  byCourse = userApplicationRepository.findByCourse(selectedCourse);
            List<UserApplication>  byUser = userApplicationRepository.findByUser(loggedUser);
            if(!byCourse.isEmpty()){
                if(!byUser.isEmpty()){
                    for (UserApplication a : byCourse){
                        for (UserApplication b : byUser){
                            if(a.equals(b)) {
                                Notification.show("Application sended. Wait for the acceptation.",
                                        Notification.Type.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }
                }
            }


            UserApplication userApplication = new UserApplication(0L, LocalDateTime.now(),loggedUser,selectedCourse);
            userApplicationRepository.save(userApplication);

        });
    }

    private void initApplyComboBox() {
        courses = courseRepository.findAll().stream().collect(Collectors.toList());
        for (Course course : courses){
            coursesNames.add(course.getName());
        }
    }
}
