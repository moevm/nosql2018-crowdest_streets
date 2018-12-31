import org.neo4j.driver.v1.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.neo4j.driver.v1.Values.parameters;

public class HW implements AutoCloseable
{
    private Driver driver;

    public HW() {
       driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic("neo4j", "password") );
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    private String convertRecordListToString(List<Record> list) {
        String result = "", tmp;
        for (Record aList : list) {
            for (Value value : aList.values()) {
                tmp = value.toString();
                result += tmp + " ";
            }
        }
        System.out.println("!!!!" + result);
        return result;
    }


    public String find(String streetName) {
        return  requestToNeo4j("MATCH (:Way {name_street:\"" + streetName + "\"})-[:consist]->(m:Node) " +
                               "RETURN m.lat, m.lon");
    }

    public String requestToNeo4j(String request) {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(request);
                    return convertRecordListToString(result.list());
                }
            } );
            return greeting;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getTableItems() {
        return requestToNeo4j("MATCH (n:Way)-[r:consist]->(m:Node)\n" +
                "RETURN n.name_street, COUNT(r) AS countAll\n" +
                "ORDER BY countAll DESC;");
    }

    public String getTable() {
        String res = getTableItems();
        System.out.println(res);
        return res;
    }

    private String getStreetsItems() {
        return requestToNeo4j("MATCH (n:Way)\n" +
                              "RETURN DISTINCT n.name_street\n");
    }

    public String getList() {
        String res = getStreetsItems();
        System.out.println(res);
        return res;
    }

    public void loadFile(){
        requestToNeo4j("CALL apoc.export.graphml.all('D://pgsdb.graphml', {useType:true, storeNodeIds:false})");
    }

    public void importData(String filePath) {
        System.out.println("importData filePath: " + filePath);
        System.out.println("requestToNeo4j response: " + requestToNeo4j("CALL apoc.import.graphml('" + filePath + "', " +
                "{readLabels: true, storeNodeIds: false, defaultRelationshipType:\"RELATED\"})"));
    }
}
