import tests.observer_pattern.Client;
import tests.observer_pattern.Store;
import tests.observer_pattern.StoreImpl;

public class Main {
    public static void main(String[] args) {
        Store store = new StoreImpl();
        Client client1 = new Client("client 1");
        Client client2 = new Client("client 2");
        Client clientNotSubscribe = new Client("client 3");

        store.registerClient(client1);
        store.registerClient(client2);

        store.informClients();

        store.removeClient(client2);

        store.informClients();
    }
}