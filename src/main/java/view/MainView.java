package view;

import controller.IMainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import model.Conversation;
import model.MainModel;
import model.User;
import sun.plugin.javascript.navig.Anchor;

import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.R;

/**
 * The MainView is the main class of the view package. Linking all the different views together and forwards info
 * given to the class from the model package via the observer class.
 */

public class MainView extends AnchorPane implements Initializable, IMainController, IMainView, Observer {

    private MainModel mainModel;
    private ChatView chatView;
    private LoginView loginView;
    private CreateConvoView createConvoView;
    private UserPageView userPage;
    private User detailedUser;

    //contactDetailView
    @FXML
    AnchorPane contactDetailView;

    @FXML
    ImageView contactDetailViewProfilemageView;

    @FXML
    ImageView contactDetailViewStatusImageView;

    @FXML
    Label contactDetailViewStatusLabel;

    @FXML
    Label contactDetailViewNameLabel;


    //mainView
    @FXML
    AnchorPane mainViewAnchorPane;

    @FXML
    FlowPane contactsFlowPane;

    @FXML
    FlowPane conversationsFlowPane;

    @FXML
    HBox mainViewHBox;

    @FXML
    HBox loginHBox;

    @FXML
    HBox createConvoHBox;

    @FXML
    StackPane mainViewStackPane;

    @FXML
    ImageView currentUserImageView;

    @FXML
    ImageView statusImageView;

    @FXML
    MenuButton statusMenu;

    @FXML
    Button newConvoButton;


    /**
     * @param location
     * @param resources Initializes the class and loads the views that makes out the complete mainView.
     *                  Proceeds to show the loginpage to the user
     *                  Finally it adds itself as an observer to the model
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayLoginPage();

        //chatView.startTiming();

        //Don't really know if this is the way to do it, casting makes it unreplacable, but otherwise this goes into
        //the interface? which seems wrong.

    }

    public MainView(MainModel mainModel){

        this.mainModel = mainModel;
        this.chatView = new ChatView(mainModel);
        this.loginView = new LoginView(mainModel);
        this.createConvoView = new CreateConvoView(mainModel, this);
        this.userPage = new UserPageView(this, mainModel);
    }


    /**
     * @param o   is the observable class that called the update method
     * @param arg Decodes the arg to see what kind of task the view should do
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MainModel) {
            switch ((MainModel.UpdateTypes)arg) {
                case ACTIVE_CONVERSATION:
                    chatView.update();
                    break;
                case CONTACTS:
                    updateContactsList();
                    break;
                case CONVERSATIONS:
                    updateConversationsList();
                    break;
                case INIT:
                    displayMainView();
                    //Cant be run in Init since there are no conversations yet
                    displayChat();
                    chatView.update();
                    updateContactsList();
                    updateConversationsList();
                    addPremadeStatuses();

                    //updateCreateNewConvoLists();
                    break;
                case USER_INFO:
                    updateUserInfoTextFields();
                    updateCurrentUserInfo();
            }
        }
        currentUserImageView.setImage(new Image(mainModel.getActiveUser().getProfileImagePath()));
    }

    private void updateUserInfoTextFields() {
        userPage.updateUserInfoTextFields();
    }

    /**
     * Clears the contactFlowPane and fills it with new ContactListItems corresponding to different Users
     */
    public void updateContactsList() {
        contactsFlowPane.getChildren().clear();
        Iterator<User> iterator = mainModel.getContacts();
        while (iterator.hasNext()) {
            contactsFlowPane.getChildren().add(new ContactListItem(iterator.next(), this));
        }
    }


    /**
     * Clears the conversationsFlowPane and fills it with new ConversationListItems corresponding to
     * different Conversations.
     */
    public void updateConversationsList() {

        conversationsFlowPane.getChildren().clear();
        Iterator<Conversation> iterator = mainModel.getConversations().values().iterator();
        while (iterator.hasNext()) {
            conversationsFlowPane.getChildren().add(new ConversationListItem(iterator.next(),(MainModel) this.mainModel));
        }

    }

    @Override
    public void displayLoginPage() {
        mainViewHBox.toBack();
        loginHBox.toFront();
        loginHBox.getChildren().clear();
        loginHBox.getChildren().add(loginView);
    }

    @Override
    public void displayCreateConvoPage() {
        createConvoHBox.toFront();
        createConvoHBox.getChildren().clear();
        createConvoHBox.getChildren().add(createConvoView);
        createConvoView.setMinWidth(mainViewAnchorPane.getWidth());
        //createConvoView.prefHeightProperty().bind(mainViewAnchorPane.heightProperty());
    }

    @Override
    public void displayContacts() {
        updateContactsList();
    }

    @Override
    public void displayConversations() {
        updateConversationsList();
    }

    @Override
    public void displayChat() {
        mainViewAnchorPane.getChildren().add(chatView);
    }

    @Override
    public void displaySettings() {

    }

    @FXML
    @Override
    public void displayUserPage() {
        mainViewAnchorPane.getChildren().clear();
        mainViewAnchorPane.getChildren().add(userPage);
        userPage.prefWidthProperty().bind(mainViewAnchorPane.widthProperty());
        userPage.prefHeightProperty().bind(mainViewAnchorPane.heightProperty());
        updateUserInfoTextFields();

    }

    @Override
    public void displayMainView() {
        mainViewHBox.toFront();
    }

    @FXML
    public void addPremadeStatuses(){
        int counter=0;
        for (String status : mainModel.getActiveUser().getPremadeStatuses()){
            MenuItem m;// = new MenuItem(status);

            if(counter==0){
                ImageView imageView = new ImageView("pics/statusGreen.png");
                m=new MenuItem(status, imageView);
            }else if(counter==1){
                ImageView imageView = new ImageView("pics/statusOrange.png");
                m=new MenuItem(status, imageView);
            }else{
                ImageView imageView = new ImageView("pics/statusRed.png");
                m=new MenuItem(status, imageView);
            }
            statusMenu.getItems().add(m);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    statusMenu.setText(m.getText());
                    //Todo store clicked status in json or user
                    //mainModel.getActiveUser().setStatus(m.getText());
                    mainModel.setStatus(m.getText());

                }
            });
            counter++;
        }
    }


    public void backToChat(){
        mainViewAnchorPane.getChildren().clear();
        mainViewAnchorPane.getChildren().add(chatView);
    }


    public void updateContactsList(ArrayList<User> contacts) {

        contactsFlowPane.getChildren().clear();

        for (User user : contacts) {

            ContactListItem contactListItemView = new ContactListItem(user, this);
            contactsFlowPane.getChildren().add(contactListItemView);
        }
    }

    public void updateCurrentUserInfo() {
        currentUserImageView.setImage(new Image(mainModel.getActiveUser().getProfileImagePath()));//TODO denna fungerar tydligen inte
        statusImageView.setImage(new Image((mainModel.getActiveUser().getStatusImagePath())));
    }


    //contactDetailView functionality
    @FXML
    public void contactDetailViewCloseButtonClicked() {
        contactDetailView.toBack();
    }

    @FXML
    public void contactDetailViewCreateConvoButtonClicked() {
        ArrayList<User> users = new ArrayList<>();
        users.add(detailedUser);
        users.add(mainModel.getActiveUser());
        mainModel.createConversation(users, detailedUser.getFullName());
        contactDetailView.toBack();
        displayMainView();
    }

    public void loadDetailView(User user) {
        detailedUser = user;
        this.contactDetailViewNameLabel.setText(user.getFullName());
        Image profileImage = new Image(user.getProfileImagePath());
        this.contactDetailViewProfilemageView.setImage(profileImage);
        this.contactDetailViewStatusImageView.setImage(new Image(user.getStatusImagePath()));
        this.contactDetailViewStatusLabel.setText(user.getStatus());
        contactDetailView.toFront();
    }

    @FXML
    public void newConvoButtonClicked() {
        displayCreateConvoPage();
    }


}