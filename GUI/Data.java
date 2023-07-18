import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Data {
    private JLabel promotLABEL;
    private JButton playerBUTTON;
    private JButton creatorButton;
    private JButton categoryBUTTON;
    private JButton stockBUTTON;
    private JButton storeBUTTON;
    private JButton gameDATA;
    private JPanel dataPANEL;
    private JButton backBUTTON;
    private JButton registrantBUTTON;
    private JFrame dataFrame;
    private JFrame tableFrame;
    private JPanel tablePANEL;

    public Data() {

        dataFrame = Util.setupFrame(dataFrame, dataPANEL, "Data");


        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection = new Connection();

                if(e.getActionCommand().equals("PLAYER DATA"))
                {
                    MongoCollection<Document> players = connection.db.getCollection("Players");

                    FindIterable<Document> playerDATA = players.find();

                    String[] fields = {"P ID", "NOGP", "NO OF LIKES", "NO OF FAVORITES", "NO OF DISLIKES", "R ID", "GAMES PLAYED","GAMES LIKED", "FAVORITE GAMES", "GAMES DISLIKED", "CATEGORIES PLAYED"};

                    showTables(fields, playerDATA);

                }
                else if(e.getActionCommand().equals("CREATOR DATA"))
                {
                    MongoCollection<Document> creators = connection.db.getCollection("Creators");

                    FindIterable<Document> creatorDATA = creators.find();
                    String[] fields = {"C ID", "NOGC", "CREATED IN CATEGORIES", "R ID"};
                    showTables(fields, creatorDATA);
                }
                else if(e.getActionCommand().equals("CATEGORY DATA"))
                {
                    MongoCollection<Document> category = connection.db.getCollection("Category");

                    FindIterable<Document> categoryDATA = category.find();
                    String[] fields = {"CATEGORY ID", "CATEGORY NAME", "GAMES AMOUNT", "CREATED IN BY", "PLAYED IN BY", "GAMES"};
                    showTables(fields, categoryDATA);
                }
                else if(e.getActionCommand().equals("GAME DATA"))
                {
                    MongoCollection<Document> game = connection.db.getCollection("Game");

                    FindIterable<Document> GameDATA = game.find();
                    String[] fields = {"GAME ID", "G_NAME", "LIKES", "DISLIKES", "FAVORITES", "RATING", "CREATORS ID", "CREATION DATE", "CATEGORY ID", "PLAYED BY", "DISLIKED BY", "FAVORITE BY", "LIKED BY"};
                    showTables(fields, GameDATA);
                }
                else if(e.getActionCommand().equals("STORE DATA"))
                {
                    MongoCollection<Document> store = connection.db.getCollection("Store");

                    FindIterable<Document> storeDATA = store.find();
                    String[] fields = {"ITEM_ID", "NAME", "PRICE", "IN_STOCK", "AMOUNT"};
                    showTables(fields, storeDATA);
                }
                else if(e.getActionCommand().equals("STOCK DATA"))
                {
                    MongoCollection<Document> stock = connection.db.getCollection("Stock");
                    FindIterable<Document> stockDATA = stock.find();
                    String[] fields = {"PRODUCT_ID", "STORE_ITEM_ID"};
                    showTables(fields, stockDATA);
                }
                else if(e.getActionCommand().equals("REGISTRANT DATA"))
                {
                    MongoCollection<Document> registrant = connection.db.getCollection("Registrant");
                    FindIterable<Document> registrantdata = registrant.find();
                    String[] fields = {"ID", "NAME", "USERNAME", "PASSWORD", "EMAIL", "AGE", "COUNTRY" };
                    showTables(fields, registrantdata);
                }
                else if(e.getActionCommand().equals("BACK"))
                {
                    dataFrame.dispose();
                    Menu menuFrame = new Menu();
                }
            }
        };
        playerBUTTON.addActionListener(listener);
        creatorButton.addActionListener(listener);
        categoryBUTTON.addActionListener(listener);
        stockBUTTON.addActionListener(listener);
        storeBUTTON.addActionListener(listener);
        gameDATA.addActionListener(listener);
        backBUTTON.addActionListener(listener);
        registrantBUTTON.addActionListener(listener);
    }

    private void testing(String[] fields, AggregateIterable data, String[] foreignCollection)
    {
        tableFrame = new JFrame();
        tablePANEL = new JPanel();

        tableFrame.setContentPane(tablePANEL);
        tableFrame.setTitle("GameSystem");
        tableFrame.setSize(1024, 768);
        tableFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        tableFrame.setVisible(true);
        tablePANEL.setSize(1024, 768);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setSize(1024, 768);
        scrollPane.setSize(1024,768);

        tablePANEL.add(scrollPane);


        List<ArrayList> answer = (List<ArrayList>) data;

        model.setColumnIdentifiers(fields);

        System.out.println(answer);
        for(int i = 0;i < answer.size(); i++)
        {

        }

        /*while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();
            Document temp = (Document) cursor.next();
            List<ArrayList> answer = (List<ArrayList>) row.get("result");

            System.out.println("TOTAL AGGREGATE: " + row);

            temp.remove("result");

            for(int i = 0; i < answer.size(); i++)
            {
                Document doc =  answer.get(i);
                doc.remove("_id");
                System.out.println("Foregin Document : " + doc);
                for(int j = 0; j < foreignCollection.length; j++)
                {
                    System.out.println(foreignCollection[j] + " : " + doc.get(foreignCollection[j]));
                    temp.append(foreignCollection[j], doc.get(foreignCollection[j]));
                }

            }

            ArrayList list1 = (ArrayList) temp.get("GAMESPLAYED");
            ArrayList list2 = (ArrayList) temp.get("CATEGORIESPLAYED");

            temp.replace("GAMESPLAYED", list1);
            temp.replace("CATEGORIESPLAYED", list2);

            model.addRow(temp.values().toArray());*/
    /*        row.remove("result");

            Object[] finall = new Object[10000];*/

         /*   for(int i = 0; i < answer.length; i++)
            {
                System.out.println(answer[i]);
            }
            finall = row.values().toArray();
            model.addRow(finall);*/



    }
    private void showTables(String[] fieldNames, FindIterable data)
    {

        Tables table = new Tables(fieldNames,data);

    }
    /*private void showTables(String[] fieldNames, FindIterable data)
    {

        tableFrame = Util.setupFrame(tableFrame, tablePANEL = new JPanel(), "Tables");

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        tablePANEL.add(scrollPane);

        model.setColumnIdentifiers(fieldNames);

        MongoCursor cursor = data.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();
            List<ArrayList> answer = (List<ArrayList>) row.get("result");

            model.addRow(row.values().toArray());
        }

    }*/

}
