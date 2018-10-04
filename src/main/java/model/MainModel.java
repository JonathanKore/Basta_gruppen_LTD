package model;


import infrastructure.DataHandlerDummy;
import infrastructure.IDataHandler;
import infrastructure.JsonHandler;
import javafx.scene.image.Image;
import model.data.Conversation;
import model.data.Message;
import model.data.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.*;


/**
 * The façade for the model package.
 */
public class MainModel extends Observable implements IMainModel{
    private User activeUser;
    private Conversation activeConversation;
    private enum UpdateTypes {
        ACTIVE_CONVERSATION, CONTACTS, CONVERSATIONS, INIT
    }
    private IDataHandler dataHandler = new DataHandlerDummy();
    ArrayList<User> newConvoUsers = new ArrayList();
    private Iterator<Conversation> conversationIterator;
    private Iterator<Message> messageIterator;
    private IDataHandler jsonHandler = new JsonHandler();
    private User detailedUser;

    public MainModel(){
        User activeUser = new User(1, "admin", "123", "eva", "olsson");
        jsonHandler.saveUser(activeUser);
    }


    public void initFillers() {
        User contactUser=new User(2, "contact", "222", "olle", "innebandysson" );
        User contactUser2=new User(3, "contact2", "222", "kalle", "kuling" );
        createUser(contactUser);
        createUser(contactUser2);
        addContact(contactUser.getId());
        addContact(contactUser2.getId());
        contactUser.setStatusImagePath("pics/activeStatus.png");
        contactUser.setProfileImagePath("pics/lukasmaly.jpg");
        contactUser.setStatus("Matematisk");
        jsonHandler.saveUser(contactUser);
        jsonHandler.saveUser(contactUser);

    }
    /**
     * @param text
     *
     * Sends a text to the active conversation from the active user
     *
     * Tells the view to update itself
     */
    @Override
    public void sendMessage(String text) {

        int newMessageId = 0;
        int oldHighestId;

        Iterator<Message> itr = loadMessagesInConversation();
        while(itr.hasNext()){
            oldHighestId = itr.next().getId();
            if(oldHighestId >= newMessageId){
                newMessageId = oldHighestId + 1;
            }
        }
        Message m = new Message(newMessageId, activeUser.getId(), text, LocalDateTime.now());
        //jsonHandler.loadConversation(activeConversation.getId()).getMessages();

        //activeConversation.addMessage(m);

        jsonHandler.saveMessage(activeConversation.getId(), m);
        update(UpdateTypes.ACTIVE_CONVERSATION);
    }


    /**
     * @param u the type of "update" that the observers should do
     *
     * Notifies the observers with the String update as an argument
     */
    private void update(UpdateTypes u) {
        String update = "";
        switch(u) {
            case ACTIVE_CONVERSATION:
                update = UpdateTypes.ACTIVE_CONVERSATION.toString();
                break;
            case CONVERSATIONS:
                update = UpdateTypes.CONVERSATIONS.toString();
                break;
            case CONTACTS:
                update = UpdateTypes.CONTACTS.toString();
                break;
            case INIT:
                update = UpdateTypes.INIT.toString();
                break;
        }
        setChanged();
        notifyObservers(update);
    }

    public Conversation loadConversation(int conversationId) {
        return jsonHandler.loadConversation(conversationId);
    }

    public Iterator<Message> loadMessagesInConversation(){

        try {
            Conversation c=loadConversation(getActiveConversation().getId());
            messageIterator = c.getMessages().iterator();
            return messageIterator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addContact(int userId){
        activeUser.addContact(userId);
        jsonHandler.saveUser(activeUser);
    }



    public Iterator<User> getContacts(){
        List<User> list = new ArrayList<>();
        for(int id : activeUser.getContacts()) {
            list.add(getUser(id));
        }
        return list.iterator();
    }

    @Override
    public User getUser(int userId) {
        return jsonHandler.loadUser(userId);
    }

    public HashMap<Integer, User> getUsers() {
        return null;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public void setActiveConversation(int conversationId) {
        this.activeConversation = jsonHandler.loadConversation(conversationId);
    }

    public void createConversation(ArrayList<User> users) {
        int newConversationId = 0;
        int oldConversationId;


        List<Conversation> list = jsonHandler.loadConversations();
        if (list != null) {
            Iterator<Conversation> itr = list.iterator();
            while(itr.hasNext()){
                oldConversationId = itr.next().getId();
                if(oldConversationId >= newConversationId){
                    newConversationId = oldConversationId + 1;
                }
            }
        }
        Conversation conversation = new Conversation(newConversationId, users);
        //conversations.put(conversation.getId(), conversation);
        jsonHandler.saveConversation(conversation);
        activeConversation = conversation;
        //TODO update view conversationlist
    }

    //Exists for testingpurposes,
    public void addConversation(Conversation c) {
        jsonHandler.saveConversation(c);
    }

    public Iterator<Conversation> getConversations() {
        //conversationIterator = jsonHandler.
        return null;
    }

    public void createUser(User u) {
        jsonHandler.saveUser(u);
    }

    public User getActiveUser() {
        return activeUser;
    }

    public Conversation getActiveConversation() {
        return activeConversation;
    }


    /**
     *
     * @param username
     * @param password
     * @return if the login was successfull or not
     *
     * Checks if a User was found with the corresponding username and password
     */
    @Override
    public boolean login(String username, String password) {
        User user = jsonHandler.loadUser(username);
        if(user != null){
            if(user.confirmPassword(password)){
                createUser(user);
                setActiveUser(user);
                initFillers();
                update(UpdateTypes.INIT);

                return true;
            }
        }
        return false;
    }

}
