package model;

public class DataHandlerDummy implements IDataHandler {
    @Override
    public void saveMessage(int conversationId, Message m) {

    }

    @Override
    public void saveUser(User u) {

    }

    @Override
    public void saveConversation(Conversation c) {

    }

    @Override
    public void updateUser(User u) {

    }

    @Override
    public void updateConversation(Conversation c) {

    }

    @Override
    public Conversation loadConversation(int conversationId) {
        return null;
    }

    @Override
    public User loadUser(int userId) {
        return null;
    }
}