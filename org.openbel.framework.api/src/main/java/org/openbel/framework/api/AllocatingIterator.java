package org.openbel.framework.api;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Specialization of an {@link Iterator iterator} that allows resources to be
 * allocated during the iteration process and freed once their use is satisfied.
 * <p>
 * Failure to call {@link #close()} will likely result in lost resources. What
 * these resources are is specific to implementations.
 * </p
 */
public interface AllocatingIterator<E> extends Iterator<E>, Closeable {

    /**
     * Terminates element iteration allowing resources to be freed. Subsequent
     * calls to {@link Iterator} will not be allowed.
     */
    @Override
    public void close();

}
