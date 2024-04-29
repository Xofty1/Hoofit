package com.example.hoofit.dataHandler;
import com.example.hoofit.data.ReserveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonUtils {

    public static <T> String convertObjectToJson(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> T convertJsonToObject(String json, Class<T> type) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, type);
    }

    public static void saveJsonToFile(String json, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}