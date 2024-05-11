package tests.observer_pattern;

public interface Store {
    void registerClient(Client client);
    void removeClient(Client client);
    void informClients();
}
