package com.soeguet.gui.properties.interfaces;

public interface PropertiesInterface {

    /**
     Sets the position of the object.
     */
    void setPosition();

    /**
     Sets the visibility of the component.

     @param status true to make the component visible, false to make it invisible
     */
    void setVisible(boolean status);

    /**
     Sets up a custom tabbed pane with a specific UI and configuration for this client.
     */
    void setupOwnTabbedPane();

    /**
     Sets up the clients tabbed pane with a specific UI and configuration for other clients.
     */
    void setupClientsTabbedPane();
}