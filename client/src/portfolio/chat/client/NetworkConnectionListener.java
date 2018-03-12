package portfolio.chat.client;

public interface NetworkConnectionListener {
    void onConnectionReady(NetworkConnection connection);
    void onRecieveData(NetworkConnection connection, String value);
    void onDisconnect(NetworkConnection connection);
    void onException(NetworkConnection connection, Exception e);
}
