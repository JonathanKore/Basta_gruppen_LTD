package view;

import controller.ILoginController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The interface presented to the user upon application start-up, giving the user a way to enter a username and
 * password to log in
 */
public class LoginView extends AnchorPane implements ILoginView {

    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;
    @FXML
    private Label wrongPasswordLabel;

    /**
     * Loads the fxml document and assigns the ILoginController to handle the relevant input from the user
     */
    public LoginView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        //The following lines creates a controller that we can temporarily use to assign functions to


        //userNameTextField.requestFocus(); //Gives focus to userNameTextField
        wrongPasswordLabel.setVisible(false);

    }

    void bindController(ILoginController controller){
        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.onLoginButtonClicked();
            }
        });
        loginAnchorPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                controller.onKeyPressed(event);
            }
        });
    }

    /**
     * Displays "wrong username or password" to the user
     */
    @Override
    public void showWrongInputNotification() {
        wrongPasswordLabel.setVisible(true);
    }

    /**
     *
     * @return The text currently in the passwordField
     */
    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    /**
     *
     * @return The text currently in the userNameTextField
     */
    @Override
    public String getUsername() {
        return userNameTextField.getText();
    }

    /**
     * Clears the username and password text fields
     */
    @Override
    public void clearTextFields() {
        userNameTextField.clear();
        passwordField.clear();
    }
}
