package tablas;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
import javax.swing.*;
import java.awt.*;

public class CloseableTab extends JPanel {
    private final JTabbedPane tabbedPane;
    private final String title;

    public CloseableTab(String title, JTabbedPane tabbedPane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.title = title;
        this.tabbedPane = tabbedPane;
        setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        add(titleLabel);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });
        add(closeButton);
    }
}

