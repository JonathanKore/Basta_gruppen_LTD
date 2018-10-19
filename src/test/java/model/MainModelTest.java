package model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MainModelTest {

    @Test
    public void sendMessage() {

        User user1 = new User(1, "ett", "123", "bengt", "testsson", StatusType.Available, true);
        User user2 = new User(2, "två", "123", "bengt2", "testsson2", StatusType.Available, true);
        HashMap<Integer, User> userMap = new HashMap<>();
        userMap.put(1, user1);
        userMap.put(2, user2);
        ArrayList<User> userList = new ArrayList<>();
        userList.addAll(userMap.values());
        Conversation c = new Conversation(1, "", userList);
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();


        MainModel model = new MainModel(userMap, conversationMap);
        model.addConversation(c);
        model.setActiveUser(user1);
        model.setActiveConversation(1);

        model.sendMessage("hejsan");
    }

    @Test
    public void readMessages() {
        User user1 = new User(1, "ett", "123", "bengt", "testsson", StatusType.Available, true);
        User user2 = new User(2, "två", "123", "bengt2", "testsson2", StatusType.Available, true);
        HashMap<Integer, User> userMap = new HashMap<>();
        userMap.put(1, user1);
        userMap.put(2, user2);
        ArrayList<User> userList = new ArrayList<>();
        userList.addAll(userMap.values());
        Conversation c = new Conversation(1, "", userList);
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();


        MainModel model = new MainModel(userMap, conversationMap);
        model.addConversation(c);
        model.setActiveUser(user1);
        model.setActiveConversation(1);

        int oldSize = model.loadConversation(1).getMessages().size();

        model.sendMessage("hejsan");

        Conversation c1 = model.loadConversation(1);
        assertEquals(c1.getMessages().size() - oldSize, 1);
        assertEquals(c1.getMessages().get(c1.getMessages().size() - 1).getText(), "hejsan");
        assertTrue(model.login("ett","123"));
        assertFalse(model.login("Hackerman","gEt_HacKeD"));
    }

    //The test should try to load conversation number 2 from the dataHandlerDummy, which only returns null
    @Test
    public void loadConversation() {
        ArrayList<User> users = new ArrayList<>();
        Conversation c = new Conversation(1, "", users);

        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();

        conversationMap.put(0,c);

        MainModel model = new MainModel(userMap,conversationMap);
        model.addConversation(c);

        assertNotNull(model.loadConversation(1));
        assertNull(model.loadConversation(76876));
    }

    //This test asserts that getContacts returns the users contacts
    @Test
    public void getContacts() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();

        MainModel model = new MainModel(userMap, conversationMap);

        //User activeUser = new User(1, "admin", "123", "eva", "olsson", StatusType.Available, true);
        //User contactUser = new User(2, "contact", "222", "olle", "innebandysson", StatusType.Available, true);

        model.createUser("admin", "123", "eva", "olsson", true);

        //model.setActiveUser(activeUser);
        assertFalse(model.getContacts().hasNext());
        model.createUser("contact", "222", "olle", "innebandysson", false);
        assertNotNull(model.getContacts().next());

    }
    @Test
    public void loadMessageInConversation(){
        User user1 = new User(1, "hej", "123", "bengt", "testsson", StatusType.Available, true);
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        Conversation c = new Conversation(1, "", users);

        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();

        MainModel model = new MainModel(userMap, conversationMap);
        model.addConversation(c);
        model.setActiveConversation(1);
        model.setActiveUser(user1);
        String testmessage = "If this works I'll have a beer";
        model.sendMessage(testmessage);
        Iterator<Message> itr = model.loadMessagesInConversation();
        String result;
        result=itr.next().getText();
        assertEquals(testmessage, result);
        testmessage="blip";
        assertNotEquals(testmessage, result);
    }

    @Test
    public void createConversation() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        ArrayList<User> userList = new ArrayList<>();

        MainModel model = new MainModel(userMap, conversationMap);

        assertTrue(model.getConversations().isEmpty());
        model.createConversation(userList, "testNamn");
        assertFalse(model.getConversations().isEmpty());
    }

    @Test
    public void getUsersConversations() {
        MainModel model = new MainModel(getFillerUsers(8), new HashMap<>());
        List<User> participants = new ArrayList<>();
        participants.add(model.getUser(1));
        participants.add(model.getUser(2));
        participants.add(model.getUser(3));
        model.createConversation(participants, "chat1");

        List<User> participants2 = new ArrayList<>();
        participants2.add(model.getUser(1));
        participants2.add(model.getUser(4));
        participants2.add(model.getUser(5));
        model.createConversation(participants2, "chat2");

        List<User> participants3 = new ArrayList<>();
        participants3.add(model.getUser(1));
        participants3.add(model.getUser(2));
        participants3.add(model.getUser(3));
        model.createConversation(participants3, "chat3");

        model.setActiveUser(1);
        Iterator<Conversation> itr = model.getUsersConversations();

        StringBuilder s = new StringBuilder();
        while(itr.hasNext()) {
            s.append(itr.next().getName());
        }
        assertEquals("chat1chat2chat3", s.toString());

        model.setActiveUser(2);
        itr = model.getUsersConversations();
        StringBuilder s2 = new StringBuilder();
        while(itr.hasNext()) {
            s2.append(itr.next().getName());
        }
        assertEquals("chat1chat3", s2.toString());


        assertFalse(itr.hasNext());





    }

    @Test
    public void generatePlaceholderName() {

        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();

        MainModel model = new MainModel(userMap, conversationMap);
        ArrayList<User> participants = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            model.createUser( "" + i, "123", "person" + i, "", false);
            participants.add(model.getUser(i));
        }
        model.createConversation(participants, "");
        //User admin = new User(500, "", "", "admin", "", StatusType.Busy, false);
        model.createUser("", "", "admin", "", false);
        model.setActiveUser(20);
        participants.add(model.getUser(20));

        //only 19 people, because it does not include yourself
        assertEquals(model.generatePlaceholderName(model.getActiveConversation()), "person19, person18, person17 + 16 more.");


        participants.clear();
        for(int i = 21; i < 23; i++) {
            //User u = new User(i, "" + i, "123", "person" + i, "", StatusType.Available, false);
            model.createUser("" + i, "123", "person" + i, "",false);
            participants.add(model.getUser(i));
        }
        model.createConversation(participants, "");
        assertEquals(model.generatePlaceholderName(model.getActiveConversation()), "person22, person21");
        participants.clear();

        participants.add(model.getUser(20));
        model.createConversation(participants, "");

        assertEquals(model.generatePlaceholderName(model.getActiveConversation()), model.getUser(20).getFirstName());
    }

    @Test
    public void setConversationName() {
        MainModel model = new MainModel(new HashMap<Integer, User>(), new HashMap<Integer, Conversation>());

        model.addConversation(new Conversation(0, "test", null));
        model.setActiveConversation(0);

        //30 characters
        model.setConversationName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        assertEquals(model.getActiveConversation().getName(), "");

        //29 characters
        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        model.setConversationName(name);

        assertEquals(model.getActiveConversation().getName(), name);

    }

    private Map<Integer, User> getFillerUsers(int amount) {

        Map<Integer, User> userMap = new HashMap();
        userMap.put(1, new User(1, "admin", "123", "Eva", "Dickinsson", StatusType.Available, true));
        userMap.put(2, new User(2, "Big beast 12", "aj58dhjj", "Kalle", "Johnson", StatusType.Available, true));
        userMap.put(3, new User(3, "Mr cool", "kh9845jnd", "Johan", "Petterson", StatusType.Available, true));
        userMap.put(4, new User(4, "Dinkerwoltz", "kfg984jhgf", "Mustafa", "Köre", StatusType.Available, true));
        userMap.put(5, new User(5, "TheTaboToast", "jkg84jf", "Karin", "Lidman", StatusType.Available, true));
        userMap.put(6, new User(6, "Heyman12", "jf672jfnm", "Carline", "Mandala", StatusType.Available, true));
        userMap.put(7, new User(7, "Jamiecoo00l", "mbkmGGF", "Bango", "Rickson", StatusType.Available, true));
        userMap.put(8, new User(8, "Diddelydoo", "lhjie34", "Olof", "Klickson", StatusType.Available, true));

        Map<Integer, User> toReturn = new HashMap<>();
        for(int i = 1; i <= amount; i++){
            toReturn.put(i, userMap.get(i));
        }
        return toReturn;
    }

    @Test
    public void createUserForController() {
    }

    @Test
    public void searchContacts() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        MainModel mainModel = new MainModel(userMap, conversationMap);

        Map<Integer, User> users = getFillerUsers(8);
        User active = new User(mainModel.getNewUserId(), "Doofenshmirtz", "perry", "Doktor", "Doofus", StatusType.Available, true);
        userMap.put(mainModel.getNewUserId(), active);
        userMap.putAll(users);

        mainModel.setActiveUser(active);
        for (User u : users.values()){
            mainModel.addContact(u.getId());
        }

        Iterator<User> iterator = mainModel.searchContacts("son");

        assertEquals(iterator.next().getId(), 1);   //Eva Dickinsson
        assertEquals(iterator.next().getId(), 2);   //Kalle Johnson
        assertEquals(iterator.next().getId(), 3);   //Johan Petterson
        assertEquals(iterator.next().getId(), 7);   //Bango Rickson
        assertEquals(iterator.next().getId(), 8);   //Olof Klickson
        assertEquals(iterator.hasNext(), false);
    }

    @Test
    public void searchConversations() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        MainModel mainModel = new MainModel(userMap, conversationMap);

        Map<Integer, User> users1m = getFillerUsers(8);
        ArrayList<User> users1 = new ArrayList<User>();
        users1.addAll(users1m.values());

        ArrayList<User> users2 = new ArrayList<User>();
        User u = new User(2, "Big beast 12", "aj58dhjj", "Kalle", "Johnson", StatusType.Available, true);
        users2.add(u);
        userMap.putAll(users1m);
        userMap.put(u.getId(), u);

        Conversation c1 = new Conversation(1, "conv1", users1);
        conversationMap.put(c1.getId(), c1);
        Conversation c2 = new Conversation(2, "BANGO", users2);
        conversationMap.put(c2.getId(), c2);
        Conversation c3 = new Conversation(3, "AXAXAXA", users2);
        conversationMap.put(c3.getId(), c3);

        Iterator<Conversation> iterator = mainModel.searchConversations("bAnGo");
        assertEquals(iterator.next(), c1);  //c1 has a participant with the first name Bango, should therefore match the search.
        assertEquals(iterator.next(), c2);  //c2's name is bango, should therefore match
        assertEquals(iterator.hasNext(), false);    //c3 is neither named bango or contains a user by the name bango, the search should therefore
                                                           // only return 2 conversations.

    }

    @Test
    public void getNewUserId() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        MainModel mainModel = new MainModel(userMap, conversationMap);
        User u1 = new User(mainModel.getNewUserId(), "t", "t", "t", "t", StatusType.Available, false);
        userMap.put(mainModel.getNewUserId(), u1);
        User u2 = new User(mainModel.getNewUserId(), "h", "h", "h", "h", StatusType.Available, false);
        userMap.put(mainModel.getNewUserId(), u2);
        User u3 = new User(mainModel.getNewUserId(), "i", "i", "i", "i", StatusType.Available, false);
        userMap.put(mainModel.getNewUserId(), u3);
        User u4 = new User(mainModel.getNewUserId(), "c", "c", "c", "c", StatusType.Available, false);
        userMap.put(mainModel.getNewUserId(), u4);
        User u5 = new User(mainModel.getNewUserId(), "c", "c", "c", "c", StatusType.Available, false);
        userMap.put(mainModel.getNewUserId(), u5);

        assertEquals(0, u1.getId());
        assertEquals(1, u2.getId());
        assertEquals(2, u3.getId());
        assertEquals(3, u4.getId());
    }

    @Test
    public void getParticipants() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        MainModel mainModel = new MainModel(userMap, conversationMap);
        ArrayList<User> participants = new ArrayList<>();
        userMap.putAll(getFillerUsers(3));
        participants.addAll(userMap.values());

        Conversation conversation = new Conversation(1, "t", participants);
        conversationMap.put(1, conversation);
        mainModel.setActiveConversation(conversation.getId());
        mainModel.setActiveUser(participants.get(2)); //3rd user of the conversation is the active user

        Iterator<User> returnedParticipants = mainModel.getParticipants(conversation);

        assertEquals(returnedParticipants.next(), participants.get(0));
        assertEquals(returnedParticipants.next(), participants.get(1));
        assertEquals(returnedParticipants.hasNext(), false);
    }

    @Test
    public void getNonParticipants() {
        HashMap<Integer, User> userMap = new HashMap<>();
        HashMap<Integer, Conversation> conversationMap = new HashMap<>();
        MainModel mainModel = new MainModel(userMap, conversationMap);
        userMap.putAll(getFillerUsers(3));
        ArrayList<User> participants = new ArrayList<>(userMap.values());
        User u1 = new User(mainModel.getNewUserId(), "aa", "bb", "cc", "bb", StatusType.Available, false);
        User u2 = new User(mainModel.getNewUserId(), "aa", "bb", "cc", "bb", StatusType.Available, false);
        userMap.put(u1.getId(), u1);

        Conversation conversation = new Conversation(1, "t", participants);
        conversationMap.put(1, conversation);
        mainModel.setActiveConversation(conversation.getId());
        mainModel.setActiveUser(u2.getId());
        mainModel.addContact(u1.getId());

        Iterator<User> iterator = mainModel.getNonParticipants(conversation);

        assertEquals(iterator.next(), u1);
        assertEquals(iterator.hasNext(), false);
    }
}