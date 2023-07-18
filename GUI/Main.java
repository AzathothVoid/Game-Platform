import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame{
    private JButton REGISTERButton;
    private JButton LOGINButton;
    private JPanel MainPanel;
    private JLabel WELCOMELabel;
    private JFrame mainFrame;

    public Main()
    {

        mainFrame = Util.setupFrame(mainFrame, MainPanel, "GameSystem");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("REGISTER"))
                {
                    mainFrame.dispose();
                    Register reg = new Register();
                }
                else if(e.getActionCommand().equals("LOGIN"))
                {
                    mainFrame.dispose();
                    Login login = new Login();
                }
            }
        };
        REGISTERButton.addActionListener(listener);
        LOGINButton.addActionListener(listener);
    }

    public static void main(String[] args)
    {
        try
        {
            for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels())
            {
                if ("Windows".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        Main main = new Main();

    }
}

