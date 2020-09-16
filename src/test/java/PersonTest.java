import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class PersonTest {

    @Test
    public void person_instantiatesCorrectly_true() {
        Person testPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        assertEquals(true, testPerson instanceof Person);
    }

    @Test
    public void getName_personInstantiatesWithName_Henry() {
        Person testPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        assertEquals("Henry", testPerson.getName());
    }

    @Test
    public void getEmail_personInstantiatesWithEmail_String() {
        Person testPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        assertEquals("[email protected] (Links to an external site.)", testPerson.getEmail());
    }

    @Test
    public void equals_returnsTrueIfNameAndEmailAreSame_true() {
        Person firstPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        Person anotherPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        assertTrue(firstPerson.equals(anotherPerson));
    }

    @Test
    public void save_insertsObjectIntoDatabase_Person() {
        Person testPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        testPerson.save();
        assertTrue(Person.all().get(0).equals(testPerson));
    }
    @Rule
    public DatabaseRule database = new DatabaseRule();

    @Test
    public void all_returnsAllInstancesOfPerson_true() {
        Person firstPerson = new Person("Henry", "henry@henry.com");
        firstPerson.save();
        Person secondPerson = new Person("Harriet", "harriet@harriet.com");
        secondPerson.save();
        assertEquals(true, Person.all().get(0).equals(firstPerson));
        assertEquals(true, Person.all().get(1).equals(secondPerson));
    }

    @Test
    public void save_assignsIdToObject() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Person savedPerson = Person.all().get(0);
        assertEquals(testPerson.getId(), savedPerson.getId());
    }

    @Test
    public void getMonsters_retrievesAllMonstersFromDatabase_monstersList() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Monster firstMonster = new Monster("Bubbles", testPerson.getId());
        firstMonster.save();
        Monster secondMonster = new Monster("Spud", testPerson.getId());
        secondMonster.save();
        Monster[] monsters = new Monster[] { firstMonster, secondMonster };
        assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
    }

}