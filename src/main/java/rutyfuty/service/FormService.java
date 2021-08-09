package rutyfuty.service;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
public class FormService {

    public String getForm() {
        return "form";
    }

    public void handleForm(String formParams, HttpServletResponse response) {
        String fileName = formParams.split("\njsonText=")[0].replace("fileName=", "");
        String json = formParams.split("jsonText=")[1];

        StringBuilder stringBuilder = new StringBuilder();

        try (JsonReader jsonReader = new JsonReader(new StringReader(json))) {
            boolean isReading = true;

            //It is not possible to use jsonReader.hasNext() to check if there is anything left in the stream,
            // because it is false triggering on BEGIN_ARRAY and END_OBJECT,
            // therefore END_DOCUMENT is used.

            while (isReading) {
                JsonToken nextToken = jsonReader.peek();
                switch (nextToken) {
                    case BEGIN_OBJECT:
                        jsonReader.beginObject();
                        break;
                    case NAME:
                        jsonReader.nextName();
                        break;
                    case STRING:
                        String str = jsonReader.nextString();
                        stringBuilder.append(str).append(", ");
                        break;
                    case NUMBER:
                        double doubleValue = jsonReader.nextDouble();
                        stringBuilder.append(doubleValue).append(", ");
                        break;
                    case NULL:
                        jsonReader.nextNull();
                        stringBuilder.append("null").append(", ");
                        break;
                    case BOOLEAN:
                        boolean boolValue = jsonReader.nextBoolean();
                        stringBuilder.append(boolValue).append(", ");
                        break;
                    case BEGIN_ARRAY:
                        jsonReader.beginArray();
                        break;
                    case END_ARRAY:
                        jsonReader.endArray();
                        break;
                    case END_OBJECT:
                        jsonReader.endObject();
                        break;
                    default:
                        isReading = false;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String resultString = stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "").toString();

        try {
            File tempFile = File.createTempFile("tempFile", null);
            try (PrintWriter writer = new PrintWriter(tempFile, "UTF-8")) {
                writer.print(resultString);
                writer.flush();
            }

            try (InputStream inputStream = new FileInputStream(tempFile)) {
                response.setContentType("application/txt");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");
                while (inputStream.available() > 0) {
                    response.getWriter().write(inputStream.read());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
