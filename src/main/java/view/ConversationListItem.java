package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.MainModel;
import model.Conversation;

import java.io.IOException;

/**
 * ConversationListItem is view that displays one conversation as an item
 */
public class ConversationListItem extends AnchorPane {

    private Conversation conversation;
    private IMainView mainView;

    @FXML
    private ImageView conversationProfileImageView;

    @FXML
    private ImageView conversationStatusImageView;

    @FXML
    private Label conversationNameLabel;

    @FXML
    private Label conversationStatusLabel;


    public ConversationListItem(Conversation conversation, MainModel mainModel, IMainView mainView) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ConversationListItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.conversation = conversation;
        this.mainView=mainView;
        String name = conversation.getName();
        if(name.length() == 0){
            name = mainModel.generatePlaceholderName(conversation);
        }

        this.conversationNameLabel.setText(name);



        if (this.conversation.getParticipants().size() == 2) {
            this.conversationProfileImageView.setImage(new Image(this.conversation.getParticipants().get(0).getProfileImagePath()));
        } else {
            this.conversationProfileImageView.setImage(new Image("pics/groupConvoDefaultImage.jpg"));
        }

    }

    @FXML
    public void conversationListItemClicked() {
        mainView.setActiveConversation(this.conversation.getId());
    }

    public Conversation getConversation() {
        return conversation;
    }
}
