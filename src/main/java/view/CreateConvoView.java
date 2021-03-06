package view;

import controller.ICreateConvoController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import model.MainModel;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class CreateConvoView extends AnchorPane implements ICreateConvoView{

    @FXML
    private AnchorPane createConvoView;
    @FXML
    private FlowPane contactPane;
    @FXML
    private FlowPane convoPane;
    @FXML
    private Button createConvoButton;
    @FXML
    private Button moveUsersButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button saveNameButton;
    @FXML
    private TextField saveNameTextField;
    @FXML
    private Label saveNameLabel;

    private MainModel mainModel;

    CreateConvoView(MainModel mainModel) {
        this.mainModel = mainModel;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CreateConvoView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    void bindController(ICreateConvoController controller){
        createConvoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.onCreateConversationButtonClicked();
            }
        });
        moveUsersButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMoveUsersButtonClicked();
            }
        });
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.onCloseButtonClicked();
            }
        });
    }

    @Override
    public void updateCreateConversationLists() {
        contactPane.getChildren().clear();
        convoPane.getChildren().clear();
        Iterator<User> itr = mainModel.getContacts();
        while (itr.hasNext()) {
            SmallContactListItem newConvoContact = new SmallContactListItem(itr.next());
            contactPane.getChildren().add(newConvoContact);
        }
    }

    @Override
    public void onMoveUsersButtonClicked() {
        ArrayList<Node> paneList = new ArrayList<>();
        paneList.addAll(this.getConvoPane().getChildren());
        paneList.addAll(this.getContactPane().getChildren());

        for (Node node : paneList) {

            SmallContactListItem smallContactListItem = (SmallContactListItem) node;

            if (smallContactListItem.isClicked()) {

                if (this.getContactPane().getChildren().contains(node)) {

                    this.getContactPane().getChildren().remove(node);
                    this.getConvoPane().getChildren().add(smallContactListItem);
                } else {

                    this.getConvoPane().getChildren().remove(node);
                    this.getContactPane().getChildren().add(smallContactListItem);
                }
                smallContactListItem.setClicked(false);
                smallContactListItem.setStyle("-fx-background-color: none");
            }
        }
    }

    @Override
    public ArrayList<User> getSelectedUsers() {

        ArrayList<User> selectedUsers = new ArrayList<>();

        for (Node node : convoPane.getChildren())
        {
            SmallContactListItem smallContactListItem = (SmallContactListItem) node;
            selectedUsers.add(smallContactListItem.getUser());
        }
        return selectedUsers;
    }



    private FlowPane getContactPane() {
        return contactPane;
    }

    private FlowPane getConvoPane() {
        return convoPane;
    }

    @Override
    public String getSaveNameTextFieldText() {
        return saveNameTextField.getText();
    }

}
