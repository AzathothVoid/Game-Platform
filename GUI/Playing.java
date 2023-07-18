import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Playing {

    private JLabel gameLABEL;
    private JButton LIKEButton;
    private JButton FAVORITEButton;
    private JButton DISLIKEButton;
    private JButton BACKButton;
    private JPanel playingPANEL;
    private JLabel rateBUTTON;
    private JTextField textField1;
    private JButton RATEButton;
    private JComboBox comboBox1;
    private JFrame playingFRAME;

    public Playing(String gameName, int gameID)
    {
        playingFRAME = Util.setupFrame(playingFRAME, playingPANEL, gameName);
        setupLabels(gameName, gameID);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("DISLIKE"))
                {
                    setData("DISLIKE", gameID);
                }
                else if (e.getActionCommand().equals("LIKE"))
                {
                    setData("LIKE", gameID);
                }
                else if (e.getActionCommand().equals("FAVORITE"))
                {
                    setData("FAVORITE", gameID);
                }
                else if (e.getActionCommand().equals("FAVORITED!"))
                {
                    setData("FAVORITED!", gameID);
                }
                else if (e.getActionCommand().equals("LIKED!"))
                {
                    setData("LIKED!", gameID);
                }
                else if (e.getActionCommand().equals("DISLIKED!"))
                {
                    setData("DISLIKED!", gameID);
                }
                else if (e.getActionCommand().equals("BACK"))
                {
                    playingFRAME.dispose();
                    Play playFrame = new Play();
                }
                setupLabels(gameName, gameID);
            }
        };
        LIKEButton.addActionListener(listener);
        FAVORITEButton.addActionListener(listener);
        DISLIKEButton.addActionListener(listener);
        BACKButton.addActionListener(listener);
    }

    public void setData(String choice, int gameID)
    {
        Connection connection = new Connection();

        MongoCollection players = connection.db.getCollection("Players");
        MongoCollection game = connection.db.getCollection("Game");

        FindIterable result = players.find(new Document("_id", Util.Player_ID));
        FindIterable gameResult = game.find(new Document("_id", gameID));

        System.out.println("GAME ID: " + gameID);

        MongoCursor cursor = result.cursor();
        MongoCursor gameCursor = gameResult.cursor();

        ArrayList likeList = null;
        ArrayList dislikeList = null;
        ArrayList favoriteList = null;

        ArrayList likedBYList = null;
        ArrayList dislikedBYList = null;
        ArrayList favoriteBYList = null;

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();

            likeList = (ArrayList) row.get("GAMESLIKED");
            dislikeList = (ArrayList) row.get("GAMESDISLIKED");
            favoriteList = (ArrayList) row.get("GAMESFAVORITED");
        }

        while(gameCursor.hasNext())
        {
            Document row = (Document) gameCursor.next();

            likedBYList = (ArrayList) row.get("LIKEDBY");
            dislikedBYList = (ArrayList) row.get("DISLIKEDBY");
            favoriteBYList = (ArrayList) row.get("FAVORITEBY");
        }


        if(choice == "DISLIKE")
        {
            if (likeList.contains(gameID))
            {
                likeList.remove(likeList.indexOf(Integer.valueOf(gameID)));
                likedBYList.remove(likedBYList.indexOf(Integer.valueOf(Util.Player_ID)));
            }
            dislikeList.add(gameID);
            if(!dislikedBYList.contains(Util.Player_ID))
                dislikedBYList.add(Util.Player_ID);
        }
        else if(choice == "LIKE")
        {
            if (dislikeList.contains(gameID))
            {
                dislikeList.remove(dislikeList.indexOf(Integer.valueOf(gameID)));
                dislikedBYList.remove(dislikedBYList.indexOf(Integer.valueOf(Util.Player_ID)));
            }
            likeList.add(gameID);
            if(!likedBYList.contains(Util.Player_ID))
                likedBYList.add(Util.Player_ID);
        }
        else if(choice == "FAVORITE")
        {
            favoriteList.add(gameID);
            if(!favoriteBYList.contains(Util.Player_ID))
                favoriteBYList.add(Util.Player_ID);
        }
        else if (choice == "FAVORITED!")
        {
            if (favoriteList.contains(gameID)) {
                favoriteList.remove(favoriteList.indexOf(gameID));
                favoriteBYList.remove(favoriteBYList.indexOf(Util.Player_ID));
            }
        }
        else if (choice == "LIKED!")
        {
            if (likeList.contains(gameID)) {
                likeList.remove(likeList.indexOf(gameID));
                likedBYList.remove(likedBYList.indexOf(Util.Player_ID));
            }
        }
        else if (choice == "DISLIKED!")
        {
            if (dislikeList.contains(gameID)) {
                dislikeList.remove(dislikeList.indexOf(gameID));
                dislikedBYList.remove(dislikedBYList.indexOf(Util.Player_ID));
            }
        }

        players.updateOne(new Document("_id", Util.Player_ID), Updates.set("GAMESLIKED", likeList));
        players.updateOne(new Document("_id", Util.Player_ID),Updates.set("GAMESDISLIKED", dislikeList));
        players.updateOne(new Document("_id", Util.Player_ID), Updates.set("GAMESFAVORITED", favoriteList));

        game.updateOne(new Document("_id", gameID), Updates.set("LIKEDBY", likedBYList));
        game.updateOne(new Document("_id", gameID), Updates.set("DISLIKEDBY", dislikedBYList));
        game.updateOne(new Document("_id", gameID), Updates.set("FAVORITEBY", favoriteBYList));
    }

    public void setupLabels(String gameName, int gameID)
    {
        gameLABEL.setText(gameName);

        Connection connection = new Connection();

        MongoCollection players = connection.db.getCollection("Players");

        FindIterable data = players.find(new Document("_id", Util.Player_ID));

        MongoCursor cursor = data.cursor();

        while (cursor.hasNext())
        {
            Document row = (Document) cursor.next();


            ArrayList likedlist = (ArrayList) row.get("GAMESLIKED");
            ArrayList dislikedlist = (ArrayList) row.get("GAMESDISLIKED");
            ArrayList favoritelist = (ArrayList) row.get("GAMESFAVORITED");

            if(likedlist.contains(gameID))
                LIKEButton.setText("LIKED!");
            else
                LIKEButton.setText("LIKE");

            if(dislikedlist.contains(gameID))
                DISLIKEButton.setText("DISLIKED!");
            else
                DISLIKEButton.setText("DISLIKE");

            if(favoritelist.contains(gameID))
                FAVORITEButton.setText("FAVORITED!");
            else
                FAVORITEButton.setText("FAVORITE");
        }
    }
}
