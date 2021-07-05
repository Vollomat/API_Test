import com.sun.net.httpserver.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class HTTPServerTest {

    public static ArrayList<Workflow> meineWorkflows = new ArrayList<Workflow>();
    public static ArrayList<Einloggdaten> alleEinloggdaten = new ArrayList<Einloggdaten>();

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

                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();

                String bodyInput = buf.toString();

                String thisworkflow = exchange.getRequestHeaders().getFirst("test");
// The resulting string is: buf.toString()
// and the number of BYTES (not utf-8 characters) from the body is: buf.length()

//*************************************************POST***************************************************************
                if (exchange.getRequestMethod().equals("POST")) {
                    String ausgabe;
                    System.out.println(exchange.getRequestHeaders().getFirst("test"));
                    boolean gefunden = false;
                    for (int i = 0; i < meineWorkflows.size(); i++) {
                        String actualWorkflow = meineWorkflows.get(i).getWorkflowname();
                        if (thisworkflow.equals(actualWorkflow)) {
                            meineWorkflows.get(i).setActive(true);
                            gefunden = true;
                        }
                    }
                    if (gefunden) {
                        ausgabe = "Der Workflow mit dem Namen " + thisworkflow + " wurde gestartet!";
                        byte[] antwort = ausgabe.getBytes();
                        exchange.setAttribute("workflow", ausgabe);
                        exchange.sendResponseHeaders(200, antwort.length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(antwort);
                        outputStream.close();
                        //System.out.println("Gefunden ist " + gefunden);
                    } else {
                        ausgabe = "Der gewünschte Workflow existiert nicht. Haben Sie sich vielleicht vertippt?";
                        byte[] antwort = ausgabe.getBytes();
                        System.out.println(ausgabe.toString());
                        exchange.sendResponseHeaders(404, antwort.length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(antwort);
                        outputStream.close();
                    }
                    System.out.println(ausgabe);


//*************************************************GET****************************************************************
                    if (exchange.getRequestMethod().equals("GET")) {
                        ausgabe = "Im Moment werden folgende Workflows ausgeführt: ";
                        for (int i = 0; i < meineWorkflows.size(); i++) {
                            if (meineWorkflows.get(i).isActive()) {
                                ausgabe = ausgabe + meineWorkflows.get(i).getWorkflowname() + ", ";
                            }
                        }
                        exchange.sendResponseHeaders(200, ausgabe.length());
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(ausgabe.getBytes());
                        outputStream.close();
                    }
                }
//*************************************************STANDARD***********************************************************
            }
        });
        httpsServer.start();
    }
}
