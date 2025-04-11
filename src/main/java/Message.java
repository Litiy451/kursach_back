public class Message {
    private int id;
    private String sender;
    private String content;
    private String timestamp;

    public Message(int id, String sender, String content, String timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Геттеры
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
}