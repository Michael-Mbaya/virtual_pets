import org.sql2o.Connection;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Monster {
    private String name;
    private int personId;
    private int id;
    private int foodLevel;
    private int sleepLevel;
    private int playLevel;

    private Timestamp birthday;
    private Timestamp lastSlept;
    private Timestamp lastAte;
    private Timestamp lastPlayed;
    private Timer timer;

    public static final int MAX_FOOD_LEVEL = 3;
    public static final int MAX_SLEEP_LEVEL = 8;
    public static final int MAX_PLAY_LEVEL = 12;
    public static final int MIN_ALL_LEVELS = 0;


    public Monster(String name, int personId) {
        this.name = name;
        this.personId = personId;
        playLevel = MAX_PLAY_LEVEL / 2;
        sleepLevel = MAX_SLEEP_LEVEL / 2;
        foodLevel = MAX_FOOD_LEVEL / 2;

        //Let's integrate a basic Timer into our application. We'll begin with a test:
        timer = new Timer();

    }

    public String getName(){
        return name;
    }

    public int getPersonId(){
        return personId;
    }

    @Override
    public boolean equals(Object otherMonster){
        if (!(otherMonster instanceof Monster)) {
            return false;
        } else {
            Monster newMonster = (Monster) otherMonster;
            return this.getName().equals(newMonster.getName()) &&
                    this.getPersonId() == newMonster.getPersonId();
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO monsters (name, personid, birthday) VALUES (:name, :personId, now())";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("personId", this.personId)
                    .executeUpdate()
                    .getKey();
        }
    }

    public int getId(){
        return id;
    }

    public static List<Monster> all() {
        String sql = "SELECT * FROM monsters";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Monster.class);
        }
    }

    public static Monster find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM monsters where id=:id";
            Monster monster = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Monster.class);
            return monster;
        }
    }

    public int getPlayLevel(){
        return playLevel;
    }

    public int getSleepLevel(){
        return sleepLevel;
    }

    public int getFoodLevel(){
        return foodLevel;
    }

    public boolean isAlive() {
        if (foodLevel <= MIN_ALL_LEVELS ||
                playLevel <= MIN_ALL_LEVELS ||
                sleepLevel <= MIN_ALL_LEVELS) {
            return false;
        }
        return true;
    }

    public void depleteLevels(){
    //alter your depleteLevels() method to prevent it from lowering levels after the Monster has died
        if (isAlive()){
            playLevel--;
            foodLevel--;
            sleepLevel--;
        }
    }

    public void play(){
        if (playLevel >= MAX_PLAY_LEVEL){
            throw new UnsupportedOperationException("You cannot play with monster anymore!");
        }
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE monsters SET lastplayed = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        playLevel++;
    }

    public void sleep(){
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
        }
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE monsters SET lastslept = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        sleepLevel++;
    }

    public void feed(){
        if (foodLevel >= MAX_FOOD_LEVEL){
            throw new UnsupportedOperationException("You cannot feed your monster anymore!");
        }
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE monsters SET lastate = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        foodLevel++;
    }

    public Timestamp getBirthday(){
        return birthday;
    }

    public Timestamp getLastSlept(){
        return lastSlept;
    }

    public Timestamp getLastAte(){
        return lastAte;
    }

    public Timestamp getLastPlayed(){
        return lastPlayed;
    }

    public void startTimer(){       //write our startTimer() method now:
        Monster currentMonster = this;
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                if (!currentMonster.isAlive()){
                    cancel();
                }
                depleteLevels();
            }
        };
        this.timer.schedule(timerTask, 0, 600);
    }

}


//import org.sql2o.*;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.ArrayList;
//
//public class Monster {
//
//    private String name;
//    private int personId;
//    private int id;
//
//    private int foodLevel;
//    private int sleepLevel;
//    private int playLevel;
//
//    private Timestamp birthday;
//    private Timestamp lastSlept;
//    private Timestamp lastAte;
//    private Timestamp lastPlayed;
//
//    public static final int MAX_FOOD_LEVEL = 3;
//    public static final int MAX_SLEEP_LEVEL = 8;
//    public static final int MAX_PLAY_LEVEL = 12;
//    public static final int MIN_ALL_LEVELS = 0;
//
//    public Monster(String name, int personId) {
//        this.name = name;
//        this.personId = personId;
//        this.playLevel=MAX_PLAY_LEVEL/2;
//        this.sleepLevel = MAX_SLEEP_LEVEL/2;
//        this.foodLevel = MAX_FOOD_LEVEL/2;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getPersonId() {
//        return personId;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public int getPlayLevel() {
//        return playLevel;
//    }
//
//    public int getFoodLevel() {
//        return foodLevel;
//    }
//
//    public int getSleepLevel() {
//        return sleepLevel;
//    }
//
//    @Override
//    public boolean equals(Object otherMonster){
//        if (!(otherMonster instanceof Monster)) {
//            return false;
//        } else {
//            Monster newMonster = (Monster) otherMonster;
//            return this.getName().equals(newMonster.getName()) &&
//                    this.getPersonId() == newMonster.getPersonId();
//        }
//    }
//
////    public void save() {
////        try(Connection con = DB.sql2o.open()) {
////            String sql = "INSERT INTO monsters (name, personid) VALUES (:name, :personId)";
////            this.id = (int) con.createQuery(sql, true)
////                    .addParameter("name", this.name)
////                    .addParameter("personId", this.personId)
////                    .executeUpdate()
////                    .getKey();
////        }
////    }     //updated to include birthday in code lines 141-150
//
//    public static List<Monster> all() {
//        String sql = "SELECT * FROM monsters";
//        try(Connection con = DB.sql2o.open()) {
//            return con.createQuery(sql).executeAndFetch(Monster.class);
//        }
//    }
//
//    public static Monster find(int id) {
//        try(Connection con = DB.sql2o.open()) {
//            String sql = "SELECT * FROM monsters where id=:id";
//            Monster monster = con.createQuery(sql)
//                    .addParameter("id", id)
//                    .executeAndFetchFirst(Monster.class);
//            return monster;
//        }
//    }
//
//    //periodically check the status of our pet. If any of its levels
//    // reach zero, this method will alert us it has died, and our
//    // front end will break the news to our user:
//    public boolean isAlive() {
//        if (foodLevel <= MIN_ALL_LEVELS ||
//                playLevel <= MIN_ALL_LEVELS ||
//                sleepLevel <= MIN_ALL_LEVELS) {
//            return false;
//        }
//        return true;
//    }
//
//    public void depleteLevels(){   //method that decreases all values by 1.
//        playLevel--;
//        foodLevel--;
//        sleepLevel--;
//    }
//
//    public void play(){
//        if (playLevel >= MAX_PLAY_LEVEL){
//            throw new UnsupportedOperationException("You cannot play with monster anymore!");
//        }
//        playLevel++;
//        //method that allow users to interact with their pet to
//        // increase their levels
//    }
//
//    public void sleep(){
//        if (sleepLevel >= MAX_SLEEP_LEVEL){
//            throw new UnsupportedOperationException("You cannot play with monster anymore!");
//        }
//        sleepLevel++;
//        // method for monster sleep and to increase sleepLevel
//    }
//
//    public void feed(){
//        if (foodLevel >= MAX_FOOD_LEVEL){
//            throw new UnsupportedOperationException("You cannot feed your monster anymore!");
//        }   //here, throw this exception if the user attempts to raise a pet's foodLevel above the upper limit:
//        foodLevel++;
//        //method for monster feeding to increase foodLevel
//    }
//
//    public void save() {
//        try(Connection con = DB.sql2o.open()) {
//            String sql = "INSERT INTO monsters (name, personid, birthday) VALUES (:name, :personId, now())";
//            this.id = (int) con.createQuery(sql, true)
//                    .addParameter("name", this.name)
//                    .addParameter("personId", this.personId)
//                    .executeUpdate()
//                    .getKey();
//        }
//    }
//
//    public Timestamp getBirthday(){
//        return birthday;
//    }
//
//}
