package psw.lab7.lab7.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import psw.lab7.lab7.UserENUM;
import psw.lab7.lab7.models.User;
import psw.lab7.lab7.repositories.*;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringUI(path = "")
public class AppUI extends UI {
    private byte incorrectLogIn = 0;
    private User loggedUser = null;

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

    boolean checkEmail(String email) {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    TabSheet tabsheet;

    @Override
    protected void init(VaadinRequest vaadinRequest){
        TabSheet tabsheet;
        tabsheet = new TabSheet();



        initLoginTab();
        tabsheet.addTab(tabLogin,"Log in");
        initSignUpTab();
        tabsheet.addTab(tabSignUp,"Sign up");


        tabsheet.setSizeFull();
        setContent(tabsheet);

    }

    //tab login
    GridLayout tabLogin = new GridLayout(3,3);

    TextField loginLoginTextField;
    TextField loginPasswordTextField;
    PasswordField loginPasswordField;
    CheckBox loginShowPasswordCheckBox;
    Button loginButton;

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

            //TODO
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
                    break;
                case TRAINER:

                    break;
                case TRAINEE:

                    break;
            }

        });
    }
    //tab sign up

    GridLayout tabSignUp = new GridLayout(3,3);

    TextField registerNameTextField;
    TextField registerSurnameTextField;
    TextField registerLoginTextField;
    TextField registerEmailTextField;
    TextField registerPassword1TextField;
    PasswordField registerPassword1Field;
    TextField registerPassword2TextField;
    PasswordField registerPassword2Field;
    CheckBox registerShowPasswordCheckBox;
    Button registerButton;
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
        registerShowPasswordCheckBox.addValueChangeListener(l ->{
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


        });
        registerButton.addClickListener(l ->{
            String login = registerLoginTextField.getValue();
            try {
                User eqistingUser = userRepository.findByLogin(login);
                Notification.show("Username taken!", Notification.Type.ERROR_MESSAGE);
                return;
            }
            catch (Exception e){
                System.out.println(e);
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
            String name = registerNameTextField.getStyleName();
            if(name.equals("")){
                Notification.show("Name is not filled", Notification.Type.ERROR_MESSAGE);
                return;
            }
            String surname = registerSurnameTextField.getStyleName();
            if(surname.equals("")){
                Notification.show("Name is not filled", Notification.Type.ERROR_MESSAGE);
                return;
            }
            User user = new User(0L,login,password1, UserENUM.TRAINEE,name,surname, LocalDate.now());
            userRepository.save(user);

        });
    }


}
