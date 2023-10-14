package com.soeguet.util.interfaces;

import java.util.List;

public interface ByteArrayHandlerInterface {

    /**
     * Convert a List of Strings to a byte array.
     *
     * @param selectedClients The List of Strings to be converted.
     * @return The byte array representation of the List of Strings.
     * @throws NullPointerException If the selectedClients list is null.
     */
    byte[] convertListToByteArray(List<String> selectedClients);
}