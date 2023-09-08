package spring.copyFile;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadCopy implements MultiThreadCopyProcessor {
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final CopyProcessor largeFileProcessor = new LargeFileNIOCopy();
    private final CopyProcessor processor = new JDKStandardCopy();

    @Override public void process(File source, File target) { pool.execute(new ProcessThread(source, target)); }

    private class ProcessThread implements Runnable {
        private final File source;
        private final File target;
        public ProcessThread(File source, File target) {
            this.source = source;
            this.target = target;
        }

        @Override public void run() {
            try {
                if (checkFileIfLargeFile(source)) largeFileProcessor.process(source, target);
                else processor.process(source, target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void shutdown() { pool.shutdown(); }

    public void await() { while (!pool.isTerminated()); }

    private boolean checkFileIfLargeFile(File file) { return file.length() > 1024 * 1024 * 100; }
}
