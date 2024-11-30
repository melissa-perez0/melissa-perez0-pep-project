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
     * @return the newly added message if the add operation was successful,
     *         including
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

    /**
     * Use the MessageDAO to retrieve a Message from an id.
     *
     * @return a message with id in the database.
     */
    public Message getMessage(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Use the MessageDAO to delete a Message from an id.
     *
     * @return a message with id in the database.
     */
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }


    /**
     * Use the MessageDAO to update a Message.
     *
     * @return a message with id in the database.
     */
    public Message updateMessage(int message_id, String text) {
        return messageDAO.updateMessage(message_id, text);
    }
}
