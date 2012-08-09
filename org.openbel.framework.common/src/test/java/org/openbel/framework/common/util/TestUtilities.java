package org.openbel.framework.common.util;

import static org.openbel.framework.common.BELUtilities.noItems;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class TestUtilities {

    public static <E> E randomElement(final Collection<E> c) {
        if (noItems(c)) {
            return null;
        }

        final int size =c.size();
        final int randomIndex = new Random().nextInt(size);
        final Iterator<E> it = c.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (i == randomIndex) {
                return it.next();
            }
            i++;
        }

        return null;
    }

    /**
     * Private constructor preventing instantiation.
     */
    private TestUtilities() {

    }
}
