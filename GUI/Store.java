import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Store extends JFrame{
    private JButton BUYButton;
    private JPanel StorePanel;
    private JLabel MLabel;
    private JLabel ItemsLabel;
    private JLabel AmountsLabel;
    private JComboBox Items;
    private JButton BACKButton;
    private JFrame storeFrame;

    public Store()
    {
        storeFrame = Util.setupFrame(storeFrame, StorePanel, "Store");

        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeFrame.dispose();
                Menu menuFrame = new Menu();
            }
        });
    }

    public void update()
    {
    }
}
