package simple.flow.tests;

import org.junit.Test;
import simple.flow.EndSubscriber;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class SubscriberTests {

    @Test
    public void whenSubscribedTo_thenShouldConsumeAll() {

        EndSubscriber<String> subscriber = new EndSubscriber<>();
        List<String> items = List.of("1", "x", "2", "x");

        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(subscriber);

            assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
            items.forEach(publisher::submit);
        }

        await().atMost(1000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedElements()).containsOnlyElementsOf(items)
                );

    }
}
