import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MapServlet extends HttpServlet {

    public MapServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        StringBuilder str = new StringBuilder();

        str.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Crowdest streets</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\n" +
                "       <link href=\"style.css\" rel=\"stylesheet\">\n" +
                "       <link rel=\"stylesheet\" href=\"leaflet/leaflet.css\" />\n" +
                "       <script src=\"leaflet/leaflet.js\"></script>" +
                "       <style>\n" +
                "           #map {width: 900px; height: 600px; left: 300px; top: 10px;}\n" +
                "           .result{border: none; outline: none;}\n" +
                "           .input {font-size: 18px;}\n" +
                "           h1{font-family: \"Courier New\"; font-size: 4em; height: 100px;}\n" +
                "       </style>" +
                "    </head>\n" +
                "\n" +
                "    <body>\n" +
                "        <h1>Crowdest Map</h1>\n" +
                "        <button onclick=ret() class=\"but\">Вернуться на первую страницу</button>" +
                "        <div id=\"map\"></div>\n" +
                //"        <p><input type=\"text\" id=\"res\" class=\"result\" size=\"50\"></p> " +
                "        <script src=\"mymap.js\"></script>" +
                "        <script src=\"mapaction.js\"></script> " +
                "    </body>\n" +
                "</html>");

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(str.toString());
        out.close();
    }
}
