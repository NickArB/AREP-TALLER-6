package com.example.mongoexample;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringsDAO {

    private final MongoCollection<Document> stringsCollection;
    private static int documentsIndex = 0;

    public StringsDAO(MongoDatabase database) {
        this.stringsCollection = database.getCollection("strings");
    }

    public void addString(String nString) {
        Document newString = new Document("string", nString)
                .append("date", new Date().toString());
        stringsCollection.insertOne(newString);
    }

    public List<Document> listStrings() {
        List<Document> docList = new ArrayList<>();
        FindIterable<Document> strings = stringsCollection.find();
        documentsIndex = ((stringsCollection.countDocuments() - 10) < 1) ? 0 : 
                                Integer.parseInt("" + (stringsCollection.countDocuments() - 10));
        strings = strings.skip(documentsIndex);
        strings.into(docList);
        return docList;
    }

    // public void updateUser(String name, int newAge) {
    //     usersCollection.updateOne(eq("name", name), set("age", newAge));
    // }

    // public void deleteUser(String name) {
    //     usersCollection.deleteOne(eq("name", name));
    // }
}
