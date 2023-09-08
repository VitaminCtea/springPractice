package spring.copyFile;

import java.io.File;
import java.io.IOException;

public interface CopyProcessor {
    void process(File source, File target) throws IOException;
}
