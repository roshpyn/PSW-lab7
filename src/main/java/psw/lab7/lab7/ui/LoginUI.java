package psw.lab7.lab7.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.*;

@SpringUI(path = "")
public class LoginUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalSplitPanel root = new HorizontalSplitPanel();
        root.setSizeFull();
    }
}
