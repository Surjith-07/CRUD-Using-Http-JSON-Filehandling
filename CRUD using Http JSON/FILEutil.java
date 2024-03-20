import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FILEutil {
    static File file = new File("/home/surjith-pt7589/Documents/CRUD using Http JSON/InputFile.json");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeFile(User user) {
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
            bufferedWriter.write(objectMapper.writeValueAsString(user));
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readFile(int limit) {
        List<String> list = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            result = bufferedReader.readLine();
            while (result != null && limit-- > 0) {
                list.add(result);
                result = bufferedReader.readLine();
            }
            if (result == null && limit != 0) {
                System.out.println("File Doesn't Contain Enough Data....!!!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
