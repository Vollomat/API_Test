import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
        HttpContext httpContext = httpsServer.createContext("/ae/api/v1/executions");
        httpContext.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                String thisworkflow = exchange.getRequestHeaders().getFirst("workflowName");
                String authentifizierung = exchange.getRequestHeaders().getFirst("Authorization");
//*************************************************POST***************************************************************
                if (authentifizierung.equals("Basic YWRtaW46YWRtaW4=")) {
                    if (exchange.getRequestMethod().equals("POST")) {
                        String ausgabe;
                        boolean gefunden = false;
                        Workflow gefundenerWorkflow = null;
                        for (int i = 0; i < meineWorkflows.size(); i++) {
                            String actualWorkflow = meineWorkflows.get(i).getWorkflowname();
                            if (thisworkflow.equals(actualWorkflow)) {
                                meineWorkflows.get(i).setActive(true);
                                gefunden = true;
                                gefundenerWorkflow = meineWorkflows.get(i);
                            }
                        }
                        if (gefunden) {
                            ausgabe = new AusgabeObjektPOST(gefundenerWorkflow).jsonErzeugen();
                            byte[] antwort = ausgabe.getBytes();
                            exchange.getResponseHeaders().add("Content-Type", "application/json");
                            exchange.sendResponseHeaders(200, antwort.length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(antwort);
                            outputStream.close();
                        } else {
                            ausgabe = new AusgabeObjektPOST(null).jsonErzeugen();
                            byte[] antwort = ausgabe.getBytes();
                            exchange.getResponseHeaders().add("Content-Type", "application/json");
                            exchange.sendResponseHeaders(404, antwort.length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(antwort);
                            outputStream.close();
                        }
                    }
//*************************************************GET****************************************************************
                    if (exchange.getRequestMethod().equals("GET")) {
                        String ausgabe = "";
                        for (int i = 0; i < meineWorkflows.size(); i++) {
                            new AusgabeObjektGET(meineWorkflows.get(i)).jsonErzeugen();
                            ausgabe = ausgabe + new AusgabeObjektGET(meineWorkflows.get(i)).jsonErzeugen();
                        }
                        byte[] antwort = ausgabe.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, antwort.length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(antwort);
                        outputStream.close();
                    }
                } else {
                    String ausgabe = "Falsche Einloggdaten!";
                    byte[] antwort = ausgabe.getBytes();
                    exchange.sendResponseHeaders(403, antwort.length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(antwort);
                    outputStream.close();
                }
            }
        });
        httpsServer.start();
        System.err.println("Server gestartet!");
    }
}

