package rlib.util;

import java.io.Closeable;
import java.io.IOException;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Набор утильных методов по работе с I/O.
 * 
 * @author Ronn
 */
public final class IOUtils {

	public static final void close(final Closeable stream) {
		try {
			stream.close();
		} catch(final IOException e) {
			LOGGER.warning(e);
		}
	}

	private static final Logger LOGGER = LoggerManager.getLogger(IOUtils.class);

	private IOUtils() {
		throw new RuntimeException();
	}
}
