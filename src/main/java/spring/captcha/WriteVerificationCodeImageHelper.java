package spring.captcha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class WriteVerificationCodeImageHelper {
    private final Captcha captcha;
    private final String path;
    private int maxFilesSavedNumber = 5;
    private ExecutorService pool;
    private volatile File directory;
    private final PriorityQueue<FileRecordInfo> priorityFiles =
            new PriorityQueue<>(Comparator.comparingLong(fileRecordInfo -> fileRecordInfo.fileAttributes.creationTime().toMillis()));
    private static final Logger logger = Logger.getLogger("spring.captcha.WriteVerificationCodeImageHelper");
    static {
        logger.addHandler(new ConsoleHandler());
        logger.setUseParentHandlers(false);
    }

    public WriteVerificationCodeImageHelper(Captcha captcha, String path) {
        this.captcha = captcha;
        this.path = path;
    }

    private static class FileRecordInfo {
        private final Path filePath;
        private final BasicFileAttributes fileAttributes;
        public FileRecordInfo(Path filePath, BasicFileAttributes fileAttributes) {
            this.filePath = filePath;
            this.fileAttributes = fileAttributes;
        }
    }

    public void toFile(byte[] bytes) {
        try {
            makeImageWriteToFile(bytes);
        } catch (IOException e) {
            recordError(e);
        }
    }

    public void toOutStream(byte[] bytes, OutputStream out) {
        try {
            makeImageWriteToOutputStream(bytes, out);
        } catch (IOException e) {
            recordError(e);
        }
    }

    private void makeImageWriteToFile(byte[] bytes) throws IOException {
        makeImageWriteToFile(bytes, captcha.getVerificationCode(), false);
    }
    private void makeImageWriteToFile(byte[] bytes, String verificationCode, boolean enabledBatchWrite) throws IOException {
        String normalize = normalizePath(path, captcha.getFileType(), verificationCode);
        File directory = putVerificationCodeImageIntoAppointFile(normalize);
        if (this.directory == null) this.directory = directory;
        if (!enabledBatchWrite) cleanFile(directory, maxFilesSavedNumber);
        makeImageWriteToOutputStream(bytes, Files.newOutputStream(Paths.get(normalize)));
    }

    private String normalizePath(String path, String fileType, String verificationCode) {
        int findSeparatorPosition = path.indexOf("/");
        if (findSeparatorPosition == -1) {
            String filename =
                    new StringJoiner("_")
                            .add(path)
                            .add(String.valueOf(Math.abs(captcha.getRand().nextInt())))
                            .add(verificationCode)
                            .add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + "." + fileType.toLowerCase())
                            .toString();
            path = new StringJoiner(File.separator)
                    .add(System.getProperty("user.dir"))
                    .add("verificationCodeImages")
                    .add(filename)
                    .toString();
        }

        return path;
    }

    private File putVerificationCodeImageIntoAppointFile(String path) throws IOException {
        File file = new File(path);
        File parentFile = file.getParentFile();

        if (!parentFile.exists()) parentFile.mkdirs();
        file.createNewFile();

        return parentFile;
    }

    public void cleanFile(File directory, int fileNumber) throws IOException {
        if (Objects.requireNonNull(directory.listFiles()).length > fileNumber) {
            priorityQueueOfferFiles(directory);
            deleteEarliestCreationDateFile();
        }
    }

    private void priorityQueueOfferFiles(File parentFile) throws IOException {
        File[] files = parentFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            Path filePath = files[i].toPath();
            priorityFiles.offer(new FileRecordInfo(filePath, Files.readAttributes(filePath, BasicFileAttributes.class)));
        }
    }

    private void deleteEarliestCreationDateFile() throws IOException {
        while (priorityFiles.size() > maxFilesSavedNumber)
            Files.delete(Objects.requireNonNull(priorityFiles.poll()).filePath);
    }

    protected void makeImageWriteToOutputStream(byte[] bytes, OutputStream out) throws IOException {
        if (out instanceof FileOutputStream) {
            // 使用FileChannel来更快的写入文件，但似乎对于小的文件来说效果不明显
            FileChannel channel = ((FileOutputStream) out).getChannel();
            channel.write(ByteBuffer.wrap(bytes));
            channel.close();
            out.close();
        } else {
            out.write(bytes);
            out.close();
        }
    }

    public String getImageBase64Data(byte[] imageBytes) { return Base64.getEncoder().encodeToString(imageBytes); }

    private byte[] generateVerificationCodeImage(
            String fileType,
            boolean useDefaultVerificationCode,
            StringBuilder verificationCode,
            String code
    ) {
        return ((Captcha.ILineCaptcha) captcha).generateVerificationCodeImage(fileType, useDefaultVerificationCode, verificationCode, code);
    }

    public void batchWriteVerificationCodeImagesToFile(int writeNumber) {
        batchWriteVerificationCodeImagesToFile(writeNumber, null);
    }

    public void batchWriteVerificationCodeImagesToFile(int writeNumber, String[] codes) {
        pool = pool == null ? Executors.newCachedThreadPool() : pool;
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(writeNumber);

        class BatchWrite implements Runnable {
            private final int index;
            public BatchWrite(int index) { this.index = index; }
            @Override public void run() {
                boolean hasCodeAndWriteCompleted = codes != null && codes.length > 0 && index < codes.length;
                String code = hasCodeAndWriteCompleted ? codes[index] : captcha.getCode();
                StringBuilder verificationCode = new StringBuilder();
                try {
                    makeImageWriteToFile(
                            generateVerificationCodeImage(captcha.getFileType(), !hasCodeAndWriteCompleted, verificationCode, code),
                            verificationCode.toString(),
                            true
                    );
                } catch (IOException e) {
                    recordError(e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        }

        for (int i = 0; i < writeNumber; i++) pool.execute(new BatchWrite(i));
        pool.shutdown();
        try {
            countDownLatch.await();
            cleanFile(directory, writeNumber);
            logger.info("用时：" + (System.currentTimeMillis() - startTime) + "ms");
        } catch (InterruptedException | IOException e) {
            recordError(e);
        }
    }

    public void setMaxFilesSavedNumber(int maxFilesSavedNumber) { this.maxFilesSavedNumber = maxFilesSavedNumber; }
    private void recordError(Exception e) { logger.severe(e.getMessage()); }
}
