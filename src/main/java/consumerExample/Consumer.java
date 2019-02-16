package consumerExample;

import java.util.concurrent.SubmissionPublisher;

public class Consumer extends SubmissionPublisher<Integer> implements Runnable {

    private int pollCount = 10000;

    @Override
    public void run() {
        while(pollCount > 0) {
            submit(pollCount);
            pollCount--;
        }
    }
}
