package infrastructure;

import model.data.Conversation;

import java.io.*;
import java.util.Scanner;

public class FileHandler {
    private String baseDirectory = System.getProperty("user.dir")+"\\conversation\\";
    public File read(String fileName) {
        return new File(baseDirectory+fileName);

    }
    public void write(String fileName, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(baseDirectory+fileName, true));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Conversation loadConversation(int conversationId) {
        File file = read(Integer.toString(conversationId));
        Conversation c = new Conversation(conversationId);

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return c;
        }

        while(scanner.hasNext()) {
            String s = scanner.nextLine();
            String[] parts = s.split(";");
            //Message m = new Message(new User(Integer.parseInt(parts[0])), parts[1]);
            //c.addMessage(m);
        }
        return c;
    }
}
