package main.java;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class DBcreate {

    public static void main(String[] args) {
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("REST2");
        db.createCollection("users", null);
        db.createCollection("auth", null);
        db.createCollection("friendlist", null);
    }

}