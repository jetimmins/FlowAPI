package simple.flow;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class EndSubscriber<T> implements Flow.Subscriber<T> {
    private AtomicInteger messageCapacity;
    private Flow.Subscription subscription;
    private List<T> consumedElements = new LinkedList<>();

    public EndSubscriber(Integer messageCapacity) {
        this.messageCapacity = new AtomicInteger(messageCapacity);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        messageCapacity.decrementAndGet();
        System.out.println("Got: " + item);
        consumedElements.add(item);
        if(messageCapacity.get() > 0) {
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}
