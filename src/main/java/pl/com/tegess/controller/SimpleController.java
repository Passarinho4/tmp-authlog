package pl.com.tegess.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class SimpleController {

    @Autowired
    MongoClient mongoClient;

    @RequestMapping(value = "/api/hello", method = RequestMethod.GET)
    public Hello hello() {

        MongoDatabase szymek = mongoClient.getDatabase("szymek");
        szymek.getCollection("szymek");
        MongoCollection<Document> collection = szymek.getCollection("szymek");
        collection.insertOne(new Document("authlog-example", "success"));
        return new Hello("Hello, In Authlog!");

    }

}
