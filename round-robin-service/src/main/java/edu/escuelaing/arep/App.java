package edu.escuelaing.arep;
import static spark.Spark.*;
/**
 * Hello world!
 *
 */
public class App{
    private static RoundRobinService service = new RoundRobinService();

    public static void main( String[] args ){
        staticFiles.location("/public");
        port(getPort());
        post("create-string", (request, response) -> {
            return service.connectWithLogService(request.body());
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000;
    }
}
