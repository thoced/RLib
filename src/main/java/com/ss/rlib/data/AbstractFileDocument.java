package com.ss.rlib.data;

import static java.nio.file.Files.newInputStream;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * The file implementation of the parser of xml documents.
 *
 * @param <C> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractFileDocument<C> extends AbstractStreamDocument<C> {

    /**
     * The file path.
     */
    @NotNull
    protected final String filePath;

    public AbstractFileDocument(@NotNull final File file) {
        this.filePath = file.getAbsolutePath();
        try {
            setStream(new FileInputStream(file));
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractFileDocument(@NotNull final Path path) {
        this.filePath = path.toAbsolutePath().toString();
        try {
            setStream(newInputStream(path, StandardOpenOption.READ));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the file path.
     *
     * @return the file path.
     */
    protected @NotNull String getFilePath() {
        return filePath;
    }
}
