package de.cuioss.test.valueobjects.testbeans.serializable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Oliver Wolff
 */
public class SerializationWriteFailure implements Serializable {

    private static final long serialVersionUID = 1986011974054022215L;

    @SuppressWarnings({ "unused" })
    private void writeObject(final ObjectOutputStream aOutputStream) throws IOException {
        throw new IOException("boom");
    }

}
