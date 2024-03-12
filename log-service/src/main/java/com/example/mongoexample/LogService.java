package com.example.mongoexample;

import com.mongodb.client.MongoDatabase;
import static spark.Spark.*;
import org.bson.Document;

public class LogService {
    public static void main(String[] args) {
        MongoDatabase database = MongoUtil.getDB();
        StringsDAO stringsDAO = new StringsDAO(database);
        port(getPort());
        post("new-string", (request, response) -> {
            String ans = "[";
            // Definir variable temporal
            String temp = "";
            // Se eliminan las llaves del JSON
            temp = request.body();
            temp = temp.replace("{", "");
            temp = temp.replace("}", "");
            // Se extrae el payload que va despues de los dos puntos :
            temp = temp.split(":")[1];
            // Se eliminan carecteres especiales
            temp = temp.replaceAll("\\\"", "");

            stringsDAO.addString(temp);
            for(Document doc: stringsDAO.listStrings()){
                ans += doc.toJson();
                ans += ",";
            }
            // Se elimina la comilla sobrante
            ans = ans.substring(0, ans.length() - 1);
            ans += "]"; 
            return ans;
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }
}

// "{string:asdasdas}" --> "string:asdasdas" --> "string", "asdasdas" --> "asdasdas"
