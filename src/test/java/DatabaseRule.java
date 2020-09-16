import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

    @Override
    protected void before() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtual_pets_test", "moringa", "moringa");  //Those with linux or windows use two strings for username and password
    }

//    @Override
//    protected void after() {
//        try(Connection con = DB.sql2o.open()) {
//            String deletePersonsQuery = "DELETE FROM persons *;";
//            con.createQuery(deletePersonsQuery).executeUpdate();
//        }
//    }
    @Override       //to clear persons table and monsters table after each spec/test
    protected void after() {
        try(Connection con = DB.sql2o.open()) {
            String deletePersonsQuery = "DELETE FROM persons *;";
            String deleteMonstersQuery = "DELETE FROM monsters *;";
            con.createQuery(deletePersonsQuery).executeUpdate();
            con.createQuery(deleteMonstersQuery).executeUpdate();
        }
    }

}