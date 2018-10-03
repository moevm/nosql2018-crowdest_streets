import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import static org.neo4j.driver.v1.Values.parameters;

public class HW implements AutoCloseable
{
    private Driver driver;
    private String massage;

    public HW() {
        driver = null;
    }

    public HW( String uri, String user, String password ) {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public String printGreeting( final String message ) {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get(0).asString();
                }
            } );
            return greeting;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void gen() {
        this.driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic("neo4j", "obesad90") );
        massage = printGreeting( "hello, world" );
    }

    public String getMassage() {
        return massage;
    }

}
