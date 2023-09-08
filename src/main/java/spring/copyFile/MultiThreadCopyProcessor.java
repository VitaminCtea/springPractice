package spring.copyFile;

public interface MultiThreadCopyProcessor extends CopyProcessor {
    void await();
    void shutdown();
}
