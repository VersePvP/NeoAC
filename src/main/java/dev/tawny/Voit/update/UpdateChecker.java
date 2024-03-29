

package dev.tawny.Voit.update;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
public final class UpdateChecker {

    private URL apiUrl;
    private String currentVersion = "V1.7";
    private String latestVersion;

    public UpdateChecker() {
        try {
            apiUrl = new URL("https://pastebin.com/cT0HfD6C");
        } catch (final Exception ignored) {
        }

        try {
            if(isUpdateAvailable()) {
                Bukkit.getConsoleSender().sendMessage("VoitAC UPDATE IS AVAILABLE! NEWEST VERSION: " + latestVersion);
            }
        } catch(IOException e) {
            return;
        }
    }

    public boolean isUpdateAvailable() throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setConnectTimeout(5000);
        connection.connect();

        latestVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

        return !latestVersion.equalsIgnoreCase(currentVersion);
    }
}
