package consumerExampleTests;

import consumerExample.Consumer;
import consumerExample.Server;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

public class consumerTest {

    @Test
    public void whenSubscribedAndSubmitted_ShouldConsumeAll() {
        Server server = new Server();
        Consumer consumer = new Consumer();
        consumer.subscribe(server);
        Assertions.assertThat(consumer.getNumberOfSubscribers()).isEqualTo(1);

        ExecutorService consumerThread = Executors.newSingleThreadExecutor();
        consumerThread.execute(consumer);

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(server.getConsumedItems().size())
                        .isEqualTo(10000));
    }
}
