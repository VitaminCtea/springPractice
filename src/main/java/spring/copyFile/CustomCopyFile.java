package spring.copyFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;

final public class CustomCopyFile {
    private final File source;
    private final File target;
    private final CopyProcessor processor;

    public CustomCopyFile(File source, File target, CopyProcessor processor) {
        this.source = source;
        this.target = target;
        this.processor = processor;
    }

    public void copy() throws NoSuchFileException {
        checkIfSourceFileNotExist(source);
        copy(source, target);
    }

    private void copy(File source, File target) {
        File nextTargetPath = generatorTargetPath(source, target).toFile();

        if (source.isDirectory()) {
            File[] files = source.listFiles();
            if (files != null) {
                if (files.length > 0) for (File directory: files) copy(directory, nextTargetPath);
                else {
                    if (shouldFileNotExist(target)) {
                        try {
                            Files.createDirectory(nextTargetPath.toPath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else {
            if (shouldFileNotExist(target)) {
                Path path = nextTargetPath.toPath();
                try {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                    processor.process(source, nextTargetPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private Path generatorTargetPath(File source, File target) {
        Path targetPath = target.toPath();
        Path sourcePath = source.toPath();

        Path nextTargetPath = sourcePath.equals(targetPath) ? targetPath.getParent() : targetPath.resolve(sourcePath.getFileName());

        if (source.toPath().equals(targetPath) && Objects.equals(source.getName(), target.getName())) {
            String fileName = targetPath.getFileName().toString();
            Path parentPath = targetPath.getParent();
            final String COPY_NAME = " - 副本";
            if (target.isFile()) {
                int delimiterIndex;
                String fileNamePrefix = fileName.substring(0, delimiterIndex = fileName.lastIndexOf("."));
                String fileNameSuffix = fileName.substring(delimiterIndex);
                nextTargetPath = parentPath.resolve(fileNamePrefix + COPY_NAME + fileNameSuffix);
            } else nextTargetPath = parentPath.resolve(fileName + COPY_NAME);
        }
        return nextTargetPath;
    }

    private boolean shouldFileNotExist(File file) { return !file.exists(); }
    private void checkIfSourceFileNotExist(File source) throws NoSuchFileException {
        if (shouldFileNotExist(source))
            throw new NoSuchFileException("The source file to be copied does not exist. Please check the legality of the source file path.");
    }
}
