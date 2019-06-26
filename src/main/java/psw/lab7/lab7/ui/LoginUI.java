package psw.lab7.lab7.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import psw.lab7.lab7.models.User;
import psw.lab7.lab7.repositories.UserRepository;

@SpringUI(path = "/login")
public class LoginUI extends UI {

    @Autowired
    private UserRepository userRepository;

    GridLayout root;
    byte incorrectLogIn = 0;

    TextField loginField;
    TextField passTextField;
    PasswordField passwordField;
    CheckBox checkBoxShowPassword;
    Button buttonLogIn;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        root = new GridLayout(3,3);
        root.setSizeFull();
        initCenter();
        initListeners();




        setContent(root);
    }

    private void initListeners() {
        checkBoxShowPassword.addValueChangeListener(l ->{
            boolean value = checkBoxShowPassword.getValue();
            passTextField.setVisible(value);
            passwordField.setVisible(!value);
            if(value){
                passTextField.setValue(passwordField.getValue());
                return;
            }
            passwordField.setValue(passTextField.getValue());

        });
        buttonLogIn.addClickListener(l ->{
            if(incorrectLogIn >= 2){
                buttonLogIn.setDisableOnClick(true);
                return;
            }

            String login = loginField.getValue();
            String password = (checkBoxShowPassword.getValue())?passTextField.getValue():passwordField.getValue();

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
                    break;
                case TRAINER:

                    break;
                case TRAINEE:

                    break;
            }

        });
    }

    private void initCenter(){
        FormLayout formLayout = new FormLayout();
        loginField = new TextField("Login:");
        formLayout.addComponent(loginField);

        passTextField = new TextField("Password:");
        passTextField.setVisible(false);
        formLayout.addComponent(passTextField);

        passwordField = new PasswordField("Password:");
        formLayout.addComponent(passwordField);

        checkBoxShowPassword = new CheckBox("Show password");
        formLayout.addComponent(checkBoxShowPassword);

        buttonLogIn = new Button("Log in");
        formLayout.addComponent(buttonLogIn);

        root.addComponent(formLayout,1,1);
    }
}
