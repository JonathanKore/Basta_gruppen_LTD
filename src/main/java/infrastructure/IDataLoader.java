/**
 * @author          Gustav Hager
 */
package infrastructure;

import model.Conversation;
import model.User;
import java.util.Map;

public interface IDataLoader {
    Map<Integer, User> loadUsers(String path);
    Map<Integer, Conversation> loadConversations(String path);
}
