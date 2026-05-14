package services;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.InputStream;
import java.util.HashMap;
import org.eclipse.parsson.JsonProviderImpl;

public class LanguageService {
    private static String ruJson = "ru.json";
    private static String enJson = "en.json";
    public static HashMap<String, String> Chats = new HashMap<>();

    public static void SetCurrentLang(String chatId, String code) {
         if (code != "en" && code != "ru")
            return;
        else if (Chats.containsKey(chatId))
            Chats.replace(chatId, code);
    }

    public static String Get(String chatId, String key) {
        String currentJson = enJson;
        String value = null;

        if (!Chats.containsKey(chatId))
            Chats.put(chatId, "en");

        if (Chats.get(chatId) == "ru") 
            currentJson = ruJson;
        
        try {
            InputStream is = LanguageService.class.getClassLoader().getResource(currentJson).openStream();
            JsonReader reader = Json.createReader(is);
            JsonObject obj = reader.readObject();
            value = obj.get(key).toString().replace("\"", "");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return value;
    }
}