import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CRUD {
    private final static String URL = "https://gorest.co.in/public/v2/users";
    private final static String HEADER = "Authorization";
    private final static String TOKEN = "d6df748819f287d9a743077f587c26d8612a060313ff0ca54502601c56a2f202";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //GetAll------------------------------------------------------
    public static String getAllUser() {
        HttpRequest getAllRequest;
        try {
            getAllRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header(HEADER, "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


        HttpResponse<String> getAllResponse;
        try {
            getAllResponse = httpClient.send(getAllRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<User> listOfUsers;
        String result;

        try {
            listOfUsers = objectMapper.readValue(getAllResponse.body(), new TypeReference<List<User>>() {
            });
            result = objectMapper.writeValueAsString(listOfUsers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(getAllResponse);
//        System.out.println(getAllResponse.body());
        return result;
    }

    //GetById-----------------------------------------------------------

    public static String getUserById(int id) {
        HttpRequest getRequest;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL + "/" + id))
                    .header(HEADER, "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> getResponse;
        try {
            getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(getRequest.uri() + " " + getResponse.statusCode());
        if (getResponse.statusCode() == 404) {
            return "USER NOT FOUND";
        }
        String result;
        try {
            User user = objectMapper.readValue(getResponse.body(), User.class);
            System.out.println(user.getEmail());
            result = objectMapper.writeValueAsString(user);
//            objectMapper.writeValue(file,user);
//            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(new File("/home/surjith-pt7589/Documents/CRUD using Http JSON/JsonFile.json"), user);
            FILEutil.writeFile(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return result;
    }

    //Post------------------------------------------------------------------

    public static String createUser(User user) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpRequest createRequest = null;
        try {
            createRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header(HEADER, "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpResponse<String> createResponse;
        try {
            createResponse = httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String result;
        try {
            User readValue = objectMapper.readValue(createResponse.body(), User.class);
            result = objectMapper.writeValueAsString(readValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //Put or Patch-----------------------------------------------------------
    public static String updateUser(int id, User user) {
        HttpRequest updateRequest;
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            updateRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL + "/" + id))
                    .header(HEADER, "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpResponse<String> updateResponse;
        try {
            updateResponse = httpClient.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (updateResponse.statusCode() >= 400) {
            return "Error...!!!!!!!!!!";
        }
//        System.out.println(updateResponse.body());
        String result;
        try {
            User readValue = objectMapper.readValue(updateResponse.body(), User.class);
            result = objectMapper.writeValueAsString(readValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //Delete-----------------------------------------------------------

    public static String deleteUser(int id) {
        HttpRequest deleteRequest = null;

        try {
            deleteRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .header(HEADER, "Bearer " + TOKEN)
                    .DELETE()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> deleteResponse;
        try {
            deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(deleteResponse.statusCode());
        System.out.println(deleteResponse.body());
        String result = null;
        try {
            User user = objectMapper.readValue(deleteResponse.body(), User.class);
            result = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
        return result;
    }

    public static void autoUpdate(int limit) {
        List<String> list = FILEutil.readFile(limit);
        System.out.println(list);
        for (String value : list) {
            System.out.println(value);
            try {
                User user = objectMapper.readValue(value, User.class);
                System.out.println(updateUser(user.getId(), new User(user.getName() + "!!", user.getEmail(), user.getGender(), user.getStatus())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
