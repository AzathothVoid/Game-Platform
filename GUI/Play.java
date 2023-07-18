import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Play {
    private JComboBox gamesLIST;
    private JButton playButton;
    private JLabel FunTextField;
    private JPanel playPANEL;
    private JLabel gamesLABEL;
    private JButton backBUTTON;
    private JFrame playFrame;

    int gameID = -1;
    int categoryID = -1;

    public Play()
    {
        playFrame = Util.setupFrame(playFrame, playPANEL, "Play Game");

        setupGamesList();
        findGameID();

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Play"))
                {
                    update();

                    String gameName = (String) gamesLIST.getSelectedItem();

                    playFrame.dispose();
                    Playing playing = new Playing(gameName, gameID);
                }
                else if (e.getActionCommand().equals("Back"))
                {
                    playFrame.dispose();
                    Menu menuFrame = new Menu();
                }
            }
        };
        playButton.addActionListener(listener);
        backBUTTON.addActionListener(listener);
        gamesLIST.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource().equals(gamesLIST))
                {
                    findGameID();
                }
            }
        });
    }

    private void setupGamesList()
    {
        Connection connection = new Connection();

        MongoCollection game = connection.db.getCollection("Game");

        Bson projection = Projections.fields(Projections.include("_id", "G_NAME", "CATEGORY_ID"));
        FindIterable result = game.find().projection(projection);

        MongoCursor cursor = result.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();

            gamesLIST.addItem(row.getString("G_NAME"));
        }
    }

    private void findGameID()
    {
        String gameName = (String) gamesLIST.getSelectedItem();

        Connection connection = new Connection();
        MongoCollection game = connection.db.getCollection("Game");

        Bson projection = Projections.fields(Projections.include("_id", "G_NAME", "CATEGORY_ID", "CREATORS_C_ID"));
        FindIterable result = game.find(new Document("G_NAME", gameName)).projection(projection);

        MongoCursor cursor = result.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();

            gameID = row.getInteger("_id");
            categoryID = row.getInteger("CATEGORY_ID");
        }

    }

    private void update()
    {
        Connection connection = new Connection();

        MongoCollection players = connection.db.getCollection("Players");
        MongoCollection game = connection.db.getCollection("Game");
        MongoCollection category = connection.db.getCollection("Category");

        FindIterable result = players.find(new Document("_id", Util.Player_ID));
        FindIterable gameResult = game.find(new Document("_id", gameID));
        FindIterable categoryResult = category.find(new Document("_id", categoryID));

        MongoCursor cursor = result.cursor();
        MongoCursor gameCursor = gameResult.cursor();
        MongoCursor categoryCursor = categoryResult.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();

            ArrayList gamesPlayedList = (ArrayList) row.get("GAMESPLAYED");
            ArrayList categoriesPlayedList = (ArrayList) row.get("CATEGORIESPLAYED");

            int NOGP = row.getInteger("NOGP");

            if(!gamesPlayedList.contains(gameID))
            {
                gamesPlayedList.add(gameID);
                NOGP += 1;

                players.updateOne(new Document("_id", Util.Player_ID), Updates.set("NOGP", NOGP));
                players.updateOne(new Document("_id", Util.Player_ID), Updates.set("GAMESPLAYED", gamesPlayedList));
            }
            if(!categoriesPlayedList.contains(categoryID))
            {
                categoriesPlayedList.add(categoryID);
                players.updateOne(new Document("_id", Util.Player_ID), Updates.set("CATEGORIESPLAYED", categoriesPlayedList));
            }
        }

        while(gameCursor.hasNext())
        {
            Document row = (Document) gameCursor.next();

            ArrayList playedBY = (ArrayList) row.get("PLAYEDBY");

            if(!playedBY.contains(Util.Player_ID))
            {
                playedBY.add(Util.Player_ID);
                game.updateOne(new Document("_id", gameID), Updates.set("PLAYEDBY", playedBY));
            }
        }

        while(categoryCursor.hasNext()) {
            Document row = (Document) categoryCursor.next();

            ArrayList playedinBY = (ArrayList) row.get("PLAYEDINBY");

            if (!playedinBY.contains(Util.Player_ID)) {
                playedinBY.add(Util.Player_ID);
                category.updateOne(new Document("_id", categoryID), Updates.set("PLAYEDINBY", playedinBY));
            }
        }

    }
}
