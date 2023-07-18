import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login {
    private JTextField userTextField;
    private JTextField passTextField;
    private JLabel Username;
    private JLabel Password;
    private JButton loginButton;
    private JButton backButton;
    private JPanel loginPanel;
    private JFrame loginFrame;

    public Login()
    {
        loginFrame = Util.setupFrame(loginFrame, loginPanel, "Login");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Login"))
                {
                    Connection connection = new Connection();
                    MongoCollection<Document> registrant = connection.db.getCollection("Registrant");

                    String username = userTextField.getText();
                    String password = passTextField.getText();

                    Bson projection = Projections.fields(Projections.include( "userName", "password"));
                    FindIterable<Document> data = registrant.find().projection(projection);

                    MongoCursor cursor = data.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        if (Objects.equals(username, row.getString("userName")))
                        {
                            if(Objects.equals(password, row.getString("password")))
                            {
                                System.out.println(row.getInteger("_id"));
                                Util.Registrant_ID = row.getInteger("_id");

                                loginFrame.dispose();
                                Menu menu = new Menu();
                            }
                        }
                    }
                }
                else if (e.getActionCommand().equals("Back"))
                {
                    loginFrame.dispose();
                    Main mainFrame = new Main();
                }
            }
        };
        loginButton.addActionListener(listener);
        backButton.addActionListener(listener);
    }
}
