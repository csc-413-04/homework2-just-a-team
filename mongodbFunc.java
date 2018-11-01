package main.java;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import java.util.ArrayList;
import java.util.List;


import java.sql.Timestamp;

public class mongodbFunc {

    Mongo mongo = new Mongo("localhost", 27017);
    DB db = mongo.getDB("REST2");
    DBCollection users = db.getCollection("users");
    DBCollection auth = db.getCollection("auth");

    public boolean create_new_user(String username, String password) {
        System.out.println("input: " + username + " " + password);

        boolean check_create_new_user = false;

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("user", username);
        DBCursor cursor = users.find(whereQuery);

        boolean check_dublicate_username = false;
        while (cursor.hasNext()) {
            //System.out.println(cursor.next());
            check_dublicate_username = true;
        }

        if (check_dublicate_username == false) {
            List<DBObject> list = new ArrayList<DBObject>();
            BasicDBObject data = new BasicDBObject();
            data.append("user", username);
            data.append("pwd", password);
            list.add(data);
            users.insert(list);
            check_create_new_user = true;
            System.out.println("new user created");

            //get new token for new user
            List<DBObject> list2 = new ArrayList<DBObject>();
            BasicDBObject data2 = new BasicDBObject();
            data2.append("user", username);
            String token = get_token(username);
            data2.append("token", token);
            list2.add(data2);
            auth.insert(list2);
            System.out.println(token);
        } else {
            System.out.println("dublicate user");
        }

        //System.out.println("check_dublicate_username: " + check_dublicate_username );
        //System.out.println("check_create_new_user: " + check_create_new_user);
        return check_create_new_user;
    }

    public boolean login_check(String username, String password) {
        System.out.println("input: " + username + " " + password);
        boolean check_login = false;
        // get a single collection
        BasicDBObject andQuery = new BasicDBObject();

        List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
        obj.add(new BasicDBObject("user", username));
        obj.add(new BasicDBObject("pwd", password));
        andQuery.put("$and", obj);

        //System.out.println(andQuery.toString());

        DBCursor cursor = users.find(andQuery);
        while (cursor.hasNext()) {
            //System.out.println(cursor7.next());
            check_login = true;
            break;
        }


        return check_login;
    }

    public String get_token(String username) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String new_token = username + timestamp.getTime();
        return new_token;
    }

    public void add_token(String username, String token) {
        BasicDBObject data = new BasicDBObject();
        BasicDBObject searchQueryUser = new BasicDBObject().append("user", username);
        auth.update(searchQueryUser, data);
        System.out.println("updated new token");
    }

    public String show_db() {

        String allDB = "";

        DBCursor cursor = users.find();
        while (cursor.hasNext()) {
            //System.out.println(cursor.next());
            allDB = allDB + cursor.next();
        }
        return allDB;
    }

}
