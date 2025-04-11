import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/messenger_db";
    private static final String USER = "postgres"; // Замени на своего пользователя
    private static final String PASSWORD = "твой_пароль"; // Замени на свой пароль

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Получение всех сообщений
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT m.id, m.content, m.timestamp, u.username AS sender " +
                      "FROM messages m JOIN users u ON m.sender_id = u.id " +
                      "ORDER BY m.timestamp ASC";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("id"),
                    rs.getString("sender"),
                    rs.getString("content"),
                    rs.getTimestamp("timestamp").toString()
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Отправка сообщения
    public void sendMessage(int senderId, String content) {
        String query = "INSERT INTO messages (sender_id, content, timestamp) VALUES (?, ?, NOW())";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, senderId);
            stmt.setString(2, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}