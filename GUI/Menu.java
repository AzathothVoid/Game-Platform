import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JFrame{
    private JButton playButton;
    private JButton createButton;
    private JButton STOREButton;
    private JLabel WELCOMELabel;
    private JPanel menuPANEL;
    private JButton dataBUTTON;
    private JButton backBUTTON;
    private JButton categoryBUTTON;
    private JFrame menuFrame;

    public Menu()
    {
        menuFrame = Util.setupFrame(menuFrame, menuPANEL, "GameSystem");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getActionCommand().equals("STORE"))
                {
                    menuFrame.dispose();
                    Store store = new Store();
                }
                else if(e.getActionCommand().equals("PLAY"))
                {
                    Connection connection = new Connection();
                    MongoCollection players = connection.db.getCollection("Players");

                    Bson projection = Projections.fields(Projections.include("R_ID"));
                    FindIterable result = players.find().projection(projection);

                    MongoCursor cursor = result.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        if(row.getInteger("R_ID") == Util.Registrant_ID)
                        {
                            Util.Player_ID = row.getInteger("_id");

                            menuFrame.dispose();
                            Play play = new Play();
                            return;
                        }
                    }
                    createPlayer();
                    menuFrame.dispose();
                    Play play = new Play();
                }
                else if(e.getActionCommand().equals("CREATE"))
                {
                    Connection connection = new Connection();
                    MongoCollection creators = connection.db.getCollection("Creators");

                    Bson projection = Projections.fields(Projections.include("R_ID"));
                    FindIterable result = creators.find().projection(projection);

                    MongoCursor cursor = result.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        if(row.getInteger("R_ID") == Util.Registrant_ID)
                        {
                            Util.Creator_ID = row.getInteger("_id");

                            menuFrame.dispose();
                            Create creaate = new Create();
                            return;
                        }
                    }
                    createCreator();
                    menuFrame.dispose();
                    Create create = new Create();
                }
                else if(e.getActionCommand().equals("DATA"))
                {
                    menuFrame.dispose();
                    Data data = new Data();
                }
                else if(e.getActionCommand().equals("BACK"))
                {
                    menuFrame.dispose();
                    Login loginFrame = new Login();
                }
                else if(e.getActionCommand().equals("CATEGORY"))
                {
                    menuFrame.dispose();
                    Categories categories = new Categories();
                }
            }
        };

        STOREButton.addActionListener(listener);
        playButton.addActionListener(listener);
        createButton.addActionListener(listener);
        dataBUTTON.addActionListener(listener);
        categoryBUTTON.addActionListener(listener);
        backBUTTON.addActionListener(listener);
    }

    private void createPlayer()
    {
        Connection connection = new Connection();
        MongoCollection players = connection.db.getCollection("Players");

        int id = -1;

        Bson projection = Projections.fields(Projections.include("_id"));
        FindIterable<Document> Lastdoc = players.find().sort(new Document("_id", -1)).limit(1).projection(projection);

        if (Lastdoc.first() != null) {
            id = Lastdoc.first().getInteger("_id") + 1;

        } else {
            id = 1;
        }

        ArrayList list1 = new ArrayList(0);
        ArrayList list2 = new ArrayList(0);
        ArrayList list3 = new ArrayList(0);
        ArrayList list4 = new ArrayList(0);
        ArrayList list5 = new ArrayList(0);
        Document player = new Document("_id", id).append("NOGP", 0).append("NO_OF_LIKES", 0).append("NO_OF_FAVORITES", 0).append("NO_OF_DISLIKES", 0).append("R_ID", Util.Registrant_ID).append("GAMESPLAYED", list1).append("GAMESLIKED", list2).append("GAMESFAVORITED", list3).append("GAMESDISLIKED", list4).append("CATEGORIESPLAYED", list5);

        players.insertOne(player);

        Util.Player_ID = id;
    }


    private void createCreator()
    {
        Connection connection = new Connection();
        MongoCollection creators = connection.db.getCollection("Creators");

        int id = -1;

        Bson projection = Projections.fields(Projections.include("_id"));
        FindIterable<Document> Lastdoc = creators.find().sort(new Document("_id", -1)).limit(1).projection(projection);

        if (Lastdoc.first() != null) {
            id = Lastdoc.first().getInteger("_id") + 1;

        } else {
            id = 1;
        }

        ArrayList list1 = new ArrayList(0);

        Document player = new Document("_id", id).append("NOGC", 0).append("CREATEDINCATEGORIES", list1).append("R_ID", Util.Registrant_ID);

        creators.insertOne(player);

        Util.Creator_ID = id;
    }

}
