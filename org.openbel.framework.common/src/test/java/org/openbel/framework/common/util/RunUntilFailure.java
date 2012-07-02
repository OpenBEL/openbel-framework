package org.openbel.framework.common.util;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * JUnit {@link Runner test runner} that can be used to run your test case
 * continually until a failure is encountered.
 *
 * <p>
 * To apply this {@link Runner test runner} to your test case add the following
 * annotation to your class definition:
 *
 * <pre>
 * {@literal @}RunWith(RunUntilFailure.class)
 * public class TestCase {
 * }
 * </pre>
 * </p>
 *
 * <p>
 * This {@link Runner test runner} is <strong><em>provided only to exercise
 * variability</em></strong> in a test and <strong><em>should not be required
 * </em></strong> for this test to run.
 * </p>
 */
public class RunUntilFailure extends Runner {

    private BlockJUnit4ClassRunner runner;

    public RunUntilFailure(Class<?> c) throws InitializationError {
        this.runner = new BlockJUnit4ClassRunner(c);
    }

    @Override
    public Description getDescription() {
        Description description = Description
                .createSuiteDescription("Run until failure");
        description.addChild(runner.getDescription());
        return description;
    }

    /**
     * Continuously runs the {@link Class class} under test until a failure
     * occurs.
     */
    @Override
    public void run(RunNotifier notifier) {
        class L extends RunListener {
            boolean fail = false;

            @Override
            public void testFailure(Failure failure) throws Exception {
                fail = true;
            }
        }
        L listener = new L();
        notifier.addListener(listener);
        while (!listener.fail)
            runner.run(notifier);
    }
}
