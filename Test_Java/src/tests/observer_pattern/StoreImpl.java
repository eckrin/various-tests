package tests.observer_pattern;

import java.util.LinkedList;
import java.util.List;

public class StoreImpl implements Store {

    private List<Client> subscribers = new LinkedList<>();

    @Override
    public void registerClient(Client client) {
        subscribers.add(client);
    }

    @Override
    public void removeClient(Client client) {
        subscribers.remove(client);
    }

    @Override
    public void informClients() {
        for (Client subscriber : subscribers) {
            subscriber.inform();
        }
    }
}
