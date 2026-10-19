import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Sunucuya bağlanıldı! Mesaj yazmaya başlayabilirsiniz.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Sunucudan (diğer istemciden) gelen mesajları dinleyen thread
            new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("Karşı taraf: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("Sunucu bağlantısı koptu.");
                }
            }).start();

            // Klavyeden girilen mesajları sunucuya gönderen döngü
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                out.println(userInput);
            }

        } catch (IOException e) {
            System.out.println("Sunucuya bağlanılamadı. Sunucunun çalıştığından emin olun.");
        }
    }
}