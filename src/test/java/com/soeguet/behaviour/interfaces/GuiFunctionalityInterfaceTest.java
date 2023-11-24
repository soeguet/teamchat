package com.soeguet.behaviour.interfaces;

import com.soeguet.behaviour.GuiFunctionalityImpl;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class GuiFunctionalityInterfaceTest {

    @Test
    void getTextFromInput() {

        MainFrameGuiInterface mainFrame = Mockito.mock(ChatMainFrameImpl.class);
        JTextPane textEditorPane = new JTextPane();
        Mockito.when(mainFrame.getTextEditorPane()).thenReturn(textEditorPane);

        textEditorPane.setText("test");

        GuiFunctionalityInterface guiFunctionality = new GuiFunctionalityImpl(mainFrame);
        assertEquals("test", guiFunctionality.getTextFromInput());
    }

    @Test
    void clearTextPane() {

        MainFrameGuiInterface mainFrame = Mockito.mock(ChatMainFrameImpl.class);
        JTextPane textEditorPane = new JTextPane();
        Mockito.when(mainFrame.getTextEditorPane()).thenReturn(textEditorPane);

        textEditorPane.setText("test");

        GuiFunctionalityInterface guiFunctionality = new GuiFunctionalityImpl(mainFrame);
        guiFunctionality.clearTextPane();
        assertEquals("", guiFunctionality.getTextFromInput());
    }
}