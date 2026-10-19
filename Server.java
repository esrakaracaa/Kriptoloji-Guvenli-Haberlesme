import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Sunucu başlatıldı. İstemciler bekleniyor...");

            // 1. İstemci bağlantısını kabul et
            Socket client1 = serverSocket.accept();
            System.out.println("1. İstemci bağlandı!");

            // 2. İstemci bağlantısını kabul et
            Socket client2 = serverSocket.accept();
            System.out.println("2. İstemci bağlandı!");

            System.out.println("Her iki istemci de bağlandı. Mesajlaşma başlatılıyor (Güvensiz Mod - Düz Metin)...");

            // Client 1'den gelenleri Client 2'ye yönlendiren thread
            new Thread(new ClientHandler(client1, client2)).start();
            
            // Client 2'den gelenleri Client 1'e yönlendiren thread
            new Thread(new ClientHandler(client2, client1)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// İstemciler arası mesaj yönlendirmesini sağlayan sınıf
class ClientHandler implements Runnable {
    private Socket sender;
    private Socket receiver;

    public ClientHandler(Socket sender, Socket receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
            PrintWriter out = new PrintWriter(receiver.getOutputStream(), true);
            
            String message;
            // Gönderenden mesaj geldikçe alıcıya ilet
            while ((message = in.readLine()) != null) {
                System.out.println("Sunucu üzerinden geçen mesaj: " + message); // Hoca bunu terminalde görmek isteyecektir
                out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Bağlantı koptu.");
        }
    }
}