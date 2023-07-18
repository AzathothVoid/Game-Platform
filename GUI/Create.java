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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Create
{
    private JPanel createPANEL;
    private JTextField gameTEXTFIELD;
    private JComboBox categoryLIST;
    private JLabel promptLABEL;
    private JLabel categoryLABEL;
    private JLabel gameLABEL;
    private JLabel dateLABEL;
    private JLabel idLABEL;
    private JButton backBUTTON;
    private JButton createBUTTON;
    private JComboBox dayLIST;
    private JComboBox monthLIST;
    private JComboBox yearLIST;
    private JFrame createFRAME;

    int category_id = -1;
    Object day;
    Object month;
    Object year;

    public Create()
    {
        createFRAME = Util.setupFrame(createFRAME, createPANEL, "Create Game");

        setComboBox();
        setDate();
        category_id = Integer.parseInt(idLABEL.getText());

        categoryLIST.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == categoryLIST)
                {
                    Connection connection = new Connection();
                    MongoCollection category = connection.db.getCollection("Category");

                    String categoryName = (String) e.getItem();

                    FindIterable result = category.find(new Document("CATEGORY_NAME", categoryName));

                    MongoCursor cursor = result.cursor();

                    int categoryID = -1;

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        categoryID = row.getInteger("_id");
                    }

                    idLABEL.setText("ID: " + categoryID);
                    category_id = categoryID;
                }
            }
        });
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("CREATE"))
                {
                    String gameNAME = gameTEXTFIELD.getText();
                    int categoryID = category_id;
                    String date = day + "/" + month + "/" + year;
                    int creatorsID = Util.Creator_ID;

                    Connection connection = new Connection();

                    MongoCollection game = connection.db.getCollection("Game");

                    Bson projection = Projections.fields(Projections.include("G_NAME"));
                    FindIterable result = game.find().projection(projection);

                    MongoCursor cursor = result.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        if(Objects.equals(gameNAME, row.getString("G_NAME")))
                        {
                            return;
                        }
                    }

                    Bson projection2 = Projections.fields(Projections.include("_id"));
                    FindIterable<Document> Lastdoc = game.find().sort(new Document("_id", -1)).limit(1).projection(projection2);
                    int id = -1;

                    if(Lastdoc.first() != null)
                    {
                        id = Lastdoc.first().getInteger("_id") + 1;
                    }
                    else
                    {
                        id = 1;
                    }

                    ArrayList list1 = new ArrayList(0);
                    ArrayList list2 = new ArrayList(0);
                    ArrayList list3 = new ArrayList(0);
                    ArrayList list4 = new ArrayList(0);

                    Document gamedoc = new Document("_id", id).append("G_NAME", gameNAME).append("LIKES", 0).append("DISLIKES", 0).append("FAVORITES", 0).append("RATING", 0).append("CREATORS_C_ID",creatorsID).append("CREATION_DATE", date).append("CATEGORY_ID", categoryID).append("PLAYEDBY", list1).append("LIKEDBY", list2).append("DISLIKEDBY", list3).append("FAVORITEBY", list4);

                    game.insertOne(gamedoc);

                    update(categoryID, id);

                    createFRAME.dispose();
                    Menu menuFrame = new Menu();
                }
                else if(e.getActionCommand().equals("BACK"))
                {
                    createFRAME.dispose();
                    Menu menuFrame = new Menu();
                }

            }
        };
        backBUTTON.addActionListener(listener);
        createBUTTON.addActionListener(listener);
        ItemListener listener1 = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == dayLIST)
                {
                    day = e.getItem();
                }
                if (e.getSource() == monthLIST)
                {
                    month = e.getItem();
                }
                if(e.getSource() == yearLIST)
                {
                    year = e.getItem();
                }
            }
        };
        dayLIST.addItemListener(listener1);
        yearLIST.addItemListener(listener1);
        monthLIST.addItemListener(listener1);
    }

    public void setDate()
    {
        for(int i = 1; i <= 30; i++)
        {
            if(i <=12)
            {
                monthLIST.addItem(i);
            }
            if(i <= 30)
            {
                dayLIST.addItem(i);
            }
            yearLIST.addItem(2000+i);
        }
    }
    public void setComboBox()
    {
        Connection connection = new Connection();
        MongoCollection category = connection.db.getCollection("Category");


        FindIterable categoryResult = category.find();


        MongoCursor categoryCursor = categoryResult.cursor();


        while(categoryCursor.hasNext())
        {
            Document row = (Document) categoryCursor.next();

            categoryLIST.addItem(row.getString("CATEGORY_NAME"));
        }

    }
    public void update(int CategoryID, int gameID)
    {
        Connection connection = new Connection();

        MongoCollection category = connection.db.getCollection("Category");
        MongoCollection creator = connection.db.getCollection("Creators");

        FindIterable result = category.find(new Document("_id", CategoryID));
        FindIterable creatorResult = creator.find(new Document("_id", Util.Creator_ID));

        MongoCursor cursor = result.cursor();
        MongoCursor creatorCursor = creatorResult.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();

            int GAMES_AMOUNT = row.getInteger("GAMES_AMOUNT");

            ArrayList games = (ArrayList) row.get("GAMES");
            ArrayList createdinBY = (ArrayList) row.get("CREATEDINBY");

            if(!createdinBY.contains(Util.Creator_ID))
            {
                createdinBY.add(Util.Creator_ID);

                category.updateOne(new Document("_id", row.getInteger("_id")), Updates.set("CREATEDINBY", createdinBY));
            }
            games.add(gameID);
            GAMES_AMOUNT += 1;

            category.updateOne(new Document("_id", row.getInteger("_id")), Updates.set("GAMES_AMOUNT", GAMES_AMOUNT));
            category.updateOne(new Document("_id", row.getInteger("_id")), Updates.set("GAMES", games));
        }


        while(creatorCursor.hasNext())
        {
            Document row = (Document) creatorCursor.next();

            int NOGC = row.getInteger("NOGC");
            ArrayList CREATEDINCATEGORIES = (ArrayList) row.get("CREATEDINCATEGORIES");

            if(!CREATEDINCATEGORIES.contains(CategoryID))
            {
                CREATEDINCATEGORIES.add(CategoryID);
            }
            NOGC += 1;

            creator.updateOne(new Document("_id", row.getInteger("_id")), Updates.set("NOGC", NOGC));
            creator.updateOne(new Document("_id", row.getInteger("_id")), Updates.set("CREATEDINCATEGORIES", CREATEDINCATEGORIES));
        }
    }
}
