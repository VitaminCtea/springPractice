package spring.copyFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class LargeFileNIOCopy implements CopyProcessor {
    @Override public void process(File source, File target) {
        try (
                FileChannel inChannel = FileChannel.open(source.toPath(), StandardOpenOption.READ);
                FileChannel outChannel = FileChannel.open(target.toPath(), StandardOpenOption.WRITE);
        ) {
            ByteBuffer cache = ByteBuffer.allocate(1024 * 1024 * 10);
            while (inChannel.read(cache) != -1) {
                cache.flip();
                outChannel.write(cache);
                cache.clear();
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}
