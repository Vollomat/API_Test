import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HTTPServerTest {

    public static ArrayList<Workflow> meineWorkflows = new ArrayList<Workflow>();

    public static void addWorkflows() {
        meineWorkflows.add(new Workflow("patrick.test"));
        meineWorkflows.add(new Workflow("musterfrau.test"));
        meineWorkflows.add(new Workflow("mustermann.test"));
        meineWorkflows.add(new Workflow("musterdivers.test"));
    }


    public static void main(String[] args) throws IOException {
        //Workflows werden angelegt (Testdaten)
        addWorkflows();

        //Ein HTTPServer wird initialisiert mit dem Port 8080
        HttpServer httpsServer = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext httpContext = httpsServer.createContext("/");
        httpContext.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                //Über die URI gibt der Anwender seine gewünschte Methode an
                String eingabe = exchange.getRequestURI().toString();

                //Die URI wird in ein Array anhand der einzelnen Informationen aufgeteilt
                String[] eingaben = eingabe.split("/");
                if (eingaben.length > 0) {

//*************************************************POST***************************************************************
                    if (eingaben[1].equals("post")) {
                        if (eingaben.length == 2) {
                            String ausgabe = "Sie haben leider keinen Workflownamen mitgegeben.";
                            System.out.println(ausgabe);
                            exchange.sendResponseHeaders(200, ausgabe.length());
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(ausgabe.getBytes(StandardCharsets.UTF_8));
                            outputStream.close();
                        }
                        if (eingaben.length > 2) {

                            //Angabe ob ein Workflow exisitiert mit dem angegebenen Namen, wenn ja wird dieser auf aktiv gesetzt (gestartet)
                            boolean gefunden = false;
                            for (int j = 0; j < meineWorkflows.size(); j++) {
                                if (meineWorkflows.get(j).getWorkflowname().equals(eingaben[2])) {
                                    meineWorkflows.get(j).setActive(true);
                                    gefunden = true;
                                    String ausgabe = "Der Worklow mit dem Workflownamen " + eingaben[2] + " wurde gestartet!";
                                    System.out.println(ausgabe);
                                    exchange.sendResponseHeaders(200, ausgabe.length());
                                    OutputStream outputStream = exchange.getResponseBody();
                                    outputStream.write(ausgabe.getBytes(StandardCharsets.UTF_8));
                                    outputStream.close();
                                    return;
                                }
                            }
                            //Der angegebene Workflowname existiert nicht
                            if (gefunden == false) {
                                String ausgabe = "Es wurde kein passender Workflow mit dem Namen " + eingaben[2] + " gefunden!";
                                System.out.println(ausgabe);
                                exchange.sendResponseHeaders(200, ausgabe.length());
                                OutputStream outputStream = exchange.getResponseBody();
                                outputStream.write(ausgabe.getBytes(StandardCharsets.UTF_8));
                                outputStream.close();
                            }

                        }
                    }

//*************************************************GET****************************************************************
                    //Falls der Anwender Daten der aktiven Workflows haben möchte:
                    if (eingaben[1].equals("get") && eingaben.length < 2) {
                        String total = "Folgende Workflows werden gerade ausgeführt: ";
                        for (int i = 0; i < meineWorkflows.size(); i++) {
                            if (meineWorkflows.get(i).isActive()) {
                                total = total + meineWorkflows.get(i).getWorkflowname() + ", ";
                            }
                        }
                        System.out.println(total);
                    }

                }
//*************************************************STANDARD***********************************************************
                String s = "Hallo";
                exchange.sendResponseHeaders(200, s.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
                outputStream.close();
            }
        });
        httpsServer.start();
    }
}
