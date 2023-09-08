package spring.copyFile;

import java.io.File;
import java.io.IOException;

public class CopyFileTime implements CopyProcessor {
    private final CopyProcessor processor;
    private double time;
    private final boolean enabledMultiThread;

    public CopyFileTime(CopyProcessor processor) { this(processor, false); }
    public CopyFileTime(CopyProcessor processor, boolean enabledMultiThread) {
        this.processor = processor;
        this.enabledMultiThread = enabledMultiThread;
    }

    @Override public void process(File source, File target) {
        time += getTaskConsumptionSeconds(() -> {
            try {
                processor.process(source, target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getTime() {
        double result = time;
        if (enabledMultiThread) result = getTaskConsumptionSeconds(() -> ((MultiThreadCopyProcessor) processor).await());
        return String.format("耗时：%ss", result);
    }

    private static long getCurrentTimeMillis() { return System.currentTimeMillis(); }
    private static double convertSeconds(double startTime) { return (getCurrentTimeMillis() - startTime) / 1000; }

    private static double getTaskConsumptionSeconds(Runnable runnable) {
        long startTime = getCurrentTimeMillis();
        runnable.run();
        return convertSeconds(startTime);
    }
}
