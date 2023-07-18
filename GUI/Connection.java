import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Connection
{
    public String connectionString = "";
    public MongoClient mongoClient = null;
    public MongoDatabase db = null;

    Connection()
    {
        try
        {
            ConnectionString url = new ConnectionString("mongodb://localhost:27017/");
            mongoClient = MongoClients.create(url);

            if (mongoClient != null) {
                System.out.println("Conncetion Established");
            }

            db = mongoClient.getDatabase("Dummy");
        }
        catch(Exception e)
        {
            e.getMessage();
        }
    }
    public static void main(String[] args)
    {

    }
}
