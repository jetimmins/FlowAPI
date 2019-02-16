package consumerExample;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Getter
public class Server implements Flow.Subscriber<Integer> {

    private Flow.Subscription subscription;
    private List<Integer> consumedItems;

    public Server() {
        consumedItems = new LinkedList<>();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.printf("Subscribed to {}", subscription);
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("Got " + item);
        consumedItems.add(item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("I'm done!");
    }
}
