package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.gui.comments.generic_comment.gui_elements.menu_items.CopyTextMenuItem;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.model.jackson.LinkModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class CustomLinkTextPane extends JTextPane implements MouseListener {

    // variables -- start
    private final LinkModel linkModel;

    // variables -- end

    // constructors -- start
    public CustomLinkTextPane(LinkModel linkModel) {

        this.linkModel = linkModel;

        super.setOpaque(false);
        super.setEditable(false);
        super.setBackground(null);
        super.setEditorKit(new LinkWrapEditorKit());

        super.addMouseListener(this);
    }

    // constructors -- end

    public void create() {

        String hyperlinkHtml;

        if (linkModel.getComment().isEmpty()) {

            hyperlinkHtml =
                    """
                    <a href="%s"
                    style="text-decoration:underline; color:blue; font-size:15;">
                    %s
                    </a>
                    """
                            .formatted(linkModel.getLink(), linkModel.getLink());

        } else {

            // contains additional <p></p> tags
            hyperlinkHtml =
                    """
                    <a href="%s"
                    style="text-decoration:underline; color:blue; font-size:15;">
                    %s
                    </a>
                    <p>%s</p>
                    """
                            .formatted(
                                    linkModel.getLink(),
                                    linkModel.getLink(),
                                    linkModel.getComment());
        }

        super.setText(hyperlinkHtml);
    }

    private void prepareProcessBuilder(final ProcessBuilder processBuilder) {

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {

            processBuilder.command(List.of("xdg-open", linkModel.getLink()));

        } else {

            processBuilder.command("cmd", "/c", "start", linkModel.getLink());
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e)) {

            // LEFT CLICK -> OPEN LINK IN BROWSER
            ProcessBuilder processBuilder = new ProcessBuilder();
            prepareProcessBuilder(processBuilder);

            try {

                processBuilder.start();

            } catch (IOException exception) {

                throw new RuntimeException(exception);
            }

        } else {

            // RIGHT CLICK -> COPY LINK
            JPopupMenu popupMenu = new JPopupMenu();
            CopyTextMenuItem copy = new CopyTextMenuItem(this, "Copy");
            popupMenu.add(copy);
            popupMenu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {}

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}
