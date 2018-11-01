package main.java;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

//import java.security.Timestamp;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args){
        // staticFiles.externalLocation("public");
        // http://sparkjava.com/documentation
        port(1234);
        // calling get will make your app start listening for the GET path with the /hello endpoint
        get("/hello", (req, res) -> "Hello World");

        //run mongodb:
        //              ./bin/mongod --dbpath ./db

        mongodbFunc mg = new mongodbFunc();

        get("/newuser", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            boolean check = false;

            if(username != null && password != null) {
                check = mg.create_new_user(username, password);
            }

            if (check == true)
                return "ok";
            else
                return "login fail";

        });

        get("/login",(request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            boolean check = mg.login_check(username, password);

            if (check == true){
                String token = mg.get_token(username);
                mg.add_token(username, token);
                return token;
            }
            else
                return "login_fail";
        });

        ///addfriend?token=<token>&friend=<freindsuserid>
        get("/addfriend", ((request, response) -> {
            String token = request.queryParams("token");
            String friend = request.queryParams("friend");
            boolean check_token = false;

            check_token = mg.addfriend_check(token);

            if(check_token == false){
                return "failed_authentication";
            }else{
                return "ok";
            }

        }));


        get("/showdb", ((request, response) -> {

            return mg.show_db();
        }));
        
    }
}
