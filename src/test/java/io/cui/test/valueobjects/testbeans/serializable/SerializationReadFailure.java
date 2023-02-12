package io.cui.test.valueobjects.testbeans.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * @author Oliver Wolff
 */
public class SerializationReadFailure implements Serializable {

    private static final long serialVersionUID = 1986011974054022215L;

    @SuppressWarnings({ "unused" })
    private void readObject(final ObjectInputStream aInputStream)
        throws IOException {
        throw new IOException("boom");
    }

}
