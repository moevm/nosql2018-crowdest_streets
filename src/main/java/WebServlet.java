import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class WebServlet extends HttpServlet {

    private HW hw;
    String result;
    public WebServlet() {}

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
                "      <link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\"img/favicon2.ico\"> " +
                "       <link href=\"style.css\" rel=\"stylesheet\">\n" +
                "       <link rel=\"stylesheet\" href=\"leaflet/leaflet.css\" />\n" +
                "       <script src=\"leaflet/leaflet.js\"></script>" +
                "       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js\"></script>" +
                "       <style>\n" +
                "           #map {width: 600px; height: 400px; left: 300px; top: 10px;}\n" +
                "           .result{border: none; outline: none;}\n" +
                "           .input {font-size: 18px;}\n" +
                "           h1{font-family: \"Courier New\"; font-size: 4em; height: 100px;}\n" +
                "       </style>" +
                "    </head>\n" +
                "\n" +
                "    <body>\n" +
                "        <h1>Crowdest streets</h1>\n" +
                "        <p>" +
                "           <b class=\"input\">Все улицы на карте сразу:</b>\n" +
                "           <button id=\"allBtn\" class=\"but\" onclick='btnAll()'> Все улицы </button>\n" +
                "       </p>" +
                "        <p><b class=\"input\">Введите название улицы или выберите его из таблицы:</b><br>\n" +
                "           <input id=\"strInput\" type=\"text\" size=\"40\">\n" +
                "           <button id=\"strBtn\" class=\"but\" onclick='show1()'> Найти </button>\n" +
                "        </p>\n" +
                //"        <p><b class=\"input\">:</b></p>\n" +
                "        <p><b id = \"bdWork\" class=\"input\">Работа с БД </b></p>\n" +
                "        <p><b id = \"bdWorkImp\" class=\"input\">Для импорта нажмите: </b></p>\n" +
                "        <p><b id = \"bdWorkExp\" class=\"input\">Для экспорта нажмите: </b></p>\n" +
                "        <div id=\"rectangle\"></div>" +
                "        <button class=\"but\" id=\"expBtn\" onclick='db_export()'> Экспорт </button>\n" +
                "        <form id=\"impButton\" action=\"http://localhost:8080/Servlet?method=import\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "           <input class=\"custom-file-input\" name=\"data\" type=\"file\"><br>\n" +
                "           <input id = \"impBtn\" class=\"but\" type=\"submit\" value=\"Импорт\"><br>\n" +
                "        </form>" +
                "        <table id=\"myTable\" cellspacing=\"0\" border=\"1\">\n" +
                "           <tbody>\n" +
                "              <tr>\n" +
                "                 <td id=\"str_name_cl\" onclick='sort(0)'>Название улицы</td><td id=\"publ_count_cl\" onclick='sort(1)'>Количество публикаций</td>\n" +
                "             </tr>\n" +
                "           </tbody>\n" +
                "        </table>" +
                "        <script src=\"action.js\"></script> " +
                "     </body>\n" +
                "</html>");
        str.append("<script>");
        //str.append("document.getElementById('res').style.display = 'none'");
        str.append("</script>");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(str.toString());
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + request + " method: " + request.getParameter("method"));
        //result = "None";
        System.out.println("request "+ request.getHeader("street"));

        hw = new HW();

        if (request.getParameter("method").equals("find")) {
            System.out.println(">>>>>>>>>>>> Accept: " + request.getParameter("street"));
            result = hw.find(request.getParameter("street"));

        } else if(request.getParameter("method").equals("import")) {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("isMultipart: " + isMultipart);

            //максимальный размер данных который разрешено загружать в байтах
            //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт.
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(5*1024*1024);

            File tempDir = new File ("C:\\Users\\Alyona\\IdeaProjects\\HelloWorld\\tmpdir");
            factory.setRepository(tempDir);

            //Создаём сам загрузчик
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List items = upload.parseRequest(request);
                Iterator iter = items.iterator();
                String importFileName="";
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    if (item.isFormField()) {
                        //если принимаемая часть данных является полем формы
                        //processFormField(item);
                        System.out.println(item.getFieldName() + "=" + item.getString());

                    } else {
                        //в противном случае рассматриваем как файл
                        importFileName = item.getName();
                        processUploadedFile(item);
                    }
                }
                System.out.println("filename"+ importFileName);
                hw.importData("C:\\\\Users\\\\Alyona\\\\IdeaProjects\\\\HelloWorld\\\\" + importFileName);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

        } else if(request.getParameter("method").equals("getTable")) {
            result = hw.getTable();
        } else if (request.getParameter("method").equals("streetList")) {
            result = hw.getList();
        } else if (request.getParameter("method").equals("loaddb")) {   //export
            System.out.println("????????????я тут");
            hw.loadFile();
            response.setHeader("Content-Disposition", "filename=\"pgsdb.graphml\"");
            File srcFile = new File("D:\\pgsdb.graphml");
            FileUtils.copyFile(srcFile, response.getOutputStream());
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }

    private void processUploadedFile(FileItem item) throws Exception {
        File uploadetFile = null;
        //выбираем файлу имя пока не найдём свободное
        do{
            String path = item.getName();
            System.out.println("mb filename: " + item.getName());
            uploadetFile = new File(path);
        }while(uploadetFile.exists());

        //создаём файл
        uploadetFile.createNewFile();
        //записываем в него данные
        item.write(uploadetFile);
    }
}
