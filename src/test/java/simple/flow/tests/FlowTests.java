package simple.flow.tests;

import org.junit.Test;
import simple.flow.EndSubscriber;
import simple.flow.TransformProcessor;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class FlowTests {

    @Test
    public void whenSubscribedTo_thenShouldConsumeAll() {

        EndSubscriber<String> subscriber = new EndSubscriber<>(3);
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

    @Test
    public void whenSubscribedAndTransform_thenShouldConsumeAll() {
        EndSubscriber<Integer> subscriber = new EndSubscriber<>(3);
        List<String> items = List.of("1", "2", "3");
        List<Integer> expectedResult = List.of(1, 2, 3);

        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            TransformProcessor<String, Integer> processor = new TransformProcessor<>(Integer::parseInt);

            publisher.subscribe(processor);
            processor.subscribe(subscriber);
            items.forEach(publisher::submit);
        }

        await().atMost(1000, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> assertThat(subscriber.getConsumedElements())
                        .containsOnlyElementsOf(expectedResult));
    }

    @Test
    public void whenCapacityIsOne_thenShouldConsumeOne() {
        EndSubscriber<String> subscriber = new EndSubscriber<>(1);
        List<String> items = List.of("1", "x", "2", "x");
        List<String> expectedResult = List.of("1");

        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(subscriber);
            assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
            items.forEach(publisher::submit);
        }

        await().atMost(1000, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> assertThat(subscriber.getConsumedElements())
                        .containsOnlyElementsOf(expectedResult));
    }
}
