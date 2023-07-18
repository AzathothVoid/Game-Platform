import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class Register {
    private JTextField nameTextField;
    private JTextField PassTextField;
    private JTextField UnameTextField;
    private JTextField emailTextField;
    private JTextField AgeTextField;
    private JTextField CountryTextField;
    private JButton REGISTERButton;
    private JPanel RegisterPanel;
    private JButton Back;
    private JFrame regFrame = null;
    /*private JFrame prompt = null;*/

    public Register() {
        regFrame = Util.setupFrame(regFrame, RegisterPanel, "Register");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("REGISTER")) {
                    String name = nameTextField.getText();
                    String userName = UnameTextField.getText();
                    String password = PassTextField.getText();
                    String email = emailTextField.getText();
                    int age = Integer.parseInt(AgeTextField.getText());
                    String country = CountryTextField.getText();

                    int id = -1;

                    Connection connection = new Connection();

                    MongoCollection<Document> registrant = connection.db.getCollection("Registrant");

                    Bson projection = Projections.fields(Projections.include("_id", "userName"));

                    FindIterable<Document> data = registrant.find().projection(projection);

                    MongoCursor cursor = data.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        System.out.println(row);
                        System.out.println("Username is: " + userName);
                        System.out.println("Database Username is: " + row.getString("userName"));

                        if(Objects.equals(userName, row.getString("userName")))
                        {
                            return;
                        }
                    }
                    FindIterable<Document> Lastdoc = registrant.find().sort(new Document("_id", -1)).limit(1).projection(projection);


                    if (Lastdoc.first() != null) {
                        id = Lastdoc.first().getInteger("_id") + 1;

                    } else {
                        id = 1;

                    }

                    Document player = new Document("_id", id).append("name", name).append("userName", userName).append("password", password).append("email", email).append("age", age).append("country", country);
                    registrant.insertOne(player);
                    regFrame.dispose();
                    Main mainFrame = new Main();
                } else if (e.getActionCommand().equals("Back"))
                {
                    regFrame.dispose();
                    Main mainFrame = new Main();
                };
            }

        };
        REGISTERButton.addActionListener(listener);
        Back.addActionListener(listener);
    }

  /*  void promptSetup(JFrame frame)
    {
        JLabel label = new JLabel("USERNAME ALREADY CHOSEN!");
        JPanel panel = new JPanel();

        frame.setContentPane(prompt);
        frame.setTitle("Prompt");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        label.setBounds(450 / 2, 300 / 2, 100, 100);

        panel.add(label);
        frame.add(panel);

    }*/
}
