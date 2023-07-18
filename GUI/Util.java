import javax.swing.*;

public class Util {

    public static int Creator_ID;
    public static int Player_ID;
    public static int Registrant_ID;

    public static JFrame setupFrame(JFrame frame, JPanel panel, String title)
    {
        frame = new JFrame();;

        frame.setContentPane(panel);
        frame.setTitle(title);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        return frame;
    }

    public static JFrame setupFrame(JFrame frame, int width, int height, JPanel panel, String title)
    {
        frame = new JFrame();;

        frame.setContentPane(panel);
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        return frame;
    }
    public void centralize()
    {

    }
}
