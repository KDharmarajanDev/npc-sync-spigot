package Mathematician.NPCSync.NPC;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class PlayerSkin {

    private String[] info;

    public PlayerSkin (String playerName){
        info = getFromName(playerName);
    }

    public String[] getFromName(String name) {
        int counter = 0;
        while(counter < 5) {
            try {
                URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
                InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
                String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();

                return new String[]{texture, signature};
            } catch (IOException e) {
                counter++;
            }
        }
        return null;
    }

    public String[] getInfo(){
        return info;
    }

}
