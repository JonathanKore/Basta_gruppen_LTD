/**
 * @author Gustav Häger
 */
package infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Conversation;
import model.StatusType;
import model.User;
import org.junit.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class JsonLoaderTest {
    @Test
    public void loadUsers() throws Exception {
        //Create users
        User user1 = new User(1, "ett", "123", "bengt", "testsson", StatusType.Available, false);
        User user2 = new User(2, "två", "123", "bengt2", "testsson2", StatusType.Available, false);
        //Add users to list for writing to JSON
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        //Add users to Map for easier checking of keySet later
        HashMap<Integer,User> usersMap = new HashMap<Integer, User>();
        usersMap.put(user1.getId(),user1);
        usersMap.put(user2.getId(),user2);
        //serialize users
        try (Writer writer = new FileWriter("src/test/java/infrastructure/users.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(users, writer);
            writer.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        JsonLoader jsonLoader = new JsonLoader("src/test/java/infrastructure/users.json", "src/test/java/infrastructure/conversations.json");

        assertEquals(user1, jsonLoader.loadUsers().get(1));
        assertEquals(user2, jsonLoader.loadUsers().get(2));
        assertNotEquals(user1,jsonLoader.loadUsers().get(2));
        assertEquals(usersMap.keySet(),jsonLoader.loadUsers().keySet());

    }

    @Test
    public void loadConversations() throws Exception {
        //Create users for the conversation
        User user1 = new User(1, "ett", "123", "bengt", "testsson", StatusType.Available, true);
        User user2 = new User(2, "två", "123", "bengt2", "testsson2", StatusType.Available, true);
        //Add users to a list
        ArrayList<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        //Create conversations
        Conversation conversation1 = new Conversation(1,"Convo1", users);
        conversation1.setName("business_convo");
        Conversation conversation2= new Conversation(2,"Convo2", users);
        conversation2.setName("business_convo_2");
        //Add conversations to Map
        HashMap<Integer,Conversation> conversationsMap = new HashMap<Integer, Conversation>();
        conversationsMap.put(conversation1.getId(),conversation1);
        conversationsMap.put(conversation2.getId(),conversation2);
        //Add conversations to list for saving
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        conversations.add(conversation1);
        conversations.add(conversation2);
        //Serialize conversations
        try (Writer writer = new FileWriter("src/test/java/infrastructure/conversations.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(conversations, writer);
            writer.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        JsonLoader jsonLoader = new JsonLoader("src/test/java/infrastructure/users.json","src/test/java/infrastructure/conversations.json");

        assertEquals(conversation1, jsonLoader.loadConversations().get(1));
        assertEquals(conversation2, jsonLoader.loadConversations().get(2));
        assertEquals(conversationsMap.keySet(),jsonLoader.loadConversations().keySet());
        assertNotEquals(user1,jsonLoader.loadConversations().get(2));
        jsonLoader = new JsonLoader("src/test/java/infrastructure/users.json","src/test/java/infrastructure/conversations/");
        assertNotEquals(user1,jsonLoader.loadConversations().get(2));
    }

}