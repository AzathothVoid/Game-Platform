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

public class Categories {
    private JTextField categoryTEXTFIELD;
    private JLabel promptLABEL;
    private JButton createBUTTON;
    private JButton backBUTTON;
    private JLabel categoryLABEL;
    private JPanel categoryPANEL;
    private JFrame categoryFRAME;

    public Categories()
    {
        categoryFRAME = Util.setupFrame(categoryFRAME, categoryPANEL, "Category");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("CREATE"))
                {
                    Connection connection = new Connection();

                    String categoryName = categoryTEXTFIELD.getText();
                    int GAMES_AMOUNT = 0;
                    int id = -1;

                    MongoCollection category = connection.db.getCollection("Category");

                    Bson projection = Projections.fields(Projections.include("_id"));
                    FindIterable<Document> Lastdoc = category.find().sort(new Document("_id", -1)).limit(1).projection(projection);
                    FindIterable data = category.find();

                    MongoCursor cursor = data.cursor();

                    while(cursor.hasNext())
                    {
                        Document row = (Document) cursor.next();

                        if(categoryName.equals(row.get("CATEGORY_NAME")))
                            return;
                    }

                    if (Lastdoc.first() != null) {
                        id = Lastdoc.first().getInteger("_id") + 1;

                    } else {
                        id = 1;
                    }

                    ArrayList list1 = new ArrayList(0);
                    ArrayList list2 = new ArrayList(0);
                    ArrayList list3 = new ArrayList(0);

                    Document row = new Document("_id", id).append("CATEGORY_NAME", categoryName).append("GAMES_AMOUNT",GAMES_AMOUNT).append("CREATEDINBY", list1).append("PLAYEDINBY", list2).append("GAMES", list3);

                    category.insertOne(row);

                    categoryFRAME.dispose();
                    Menu menuFrame = new Menu();
                }
                else if (e.getActionCommand().equals("BACK"))
                {
                    categoryFRAME.dispose();
                    Menu menuFrame = new Menu();
                }

            }
        };
        createBUTTON.addActionListener(listener);
        backBUTTON.addActionListener(listener);
    }



}
