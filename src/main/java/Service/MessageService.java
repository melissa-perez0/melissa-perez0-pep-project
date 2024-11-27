package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    /**
     * No-args constructor for an messageService instantiates a plain messageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }  
    
    /**
     * Constructor for an messageService when a messageDAO is provided.
     * This is used for when a mock messageDAO that exhibits mock behavior is used
     * in the test cases.
     * This would allow the testing of MessageService independently of MessagDAO.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Use the MessageDAO to add a new message to the database.
     *
     * @param message an object representing a new Message.
     * @return the newly added message if the add operation was successful, including
     *         the message_id.
     */
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

     /**
     * Use the MessageDAO to retrieve a List containing all messages.
     *
     * @return all messages in the database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

}
