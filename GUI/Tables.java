import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Tables {
    private JTable Table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JPanel tablePANEL;
    private JFrame tableFRAME;

    public Tables(String[] fieldNames, FindIterable data)
    {
        model = new DefaultTableModel();
        Table = new JTable(model);
        scrollPane = new JScrollPane(Table);

        model.setColumnIdentifiers(fieldNames);
        scrollPane.setPreferredSize(new Dimension(700, 600));

        tableFRAME = Util.setupFrame(tableFRAME, 800, 600,  tablePANEL = new JPanel(), "GameSystem");

        tableFRAME.add(scrollPane);

        addData(fieldNames, data);

    }

    public void addData(String[] fieldnames, FindIterable data)
    {
        MongoCursor cursor = data.cursor();

        while(cursor.hasNext())
        {
            Document row = (Document) cursor.next();
            List<ArrayList> answer = (List<ArrayList>) row.get("result");

            model.addRow(row.values().toArray());
        }
    }
}
