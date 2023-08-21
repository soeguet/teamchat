package com.soeguet.model;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ClientsList {

    private List<Clients> clientsList = new ArrayList<>();

    public ClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    public ClientsList() {
    }

    public void setClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    @Override
    public String toString() {
        return "ClientList{" + "clientsList=" + clientsList + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsList that = (ClientsList) o;
        return Objects.equals(clientsList, that.clientsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientsList);
    }
}
