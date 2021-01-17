package multiplayer.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class ServerApplication {

    ServerSocket serverSocket;
    multiplayer.server.ServerConnection player1;
    multiplayer.server.ServerConnection player2;
    private int playerCount;

    int player1CharacterId = 0;
    int player2CharacterId = 0;

    int user1Ready = 0;
    int user2Ready = 0;

    int p1ButtonNum;
    int p2ButtonNum;

    // Server constructor
    public ServerApplication() {
        try {
            serverSocket = new ServerSocket(8888);
        } catch(IOException exception) {
            System.out.println("IOException from Game Server Constructor!");
        }

        // Hierauf wartet Tomcat
        //acceptConnections();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void acceptConnections() {
        try {
            System.out.println("-----Game Server-----");
            System.out.println("Waiting for Connections...");

            while(playerCount < 2) {
                Socket socket = serverSocket.accept();
                playerCount++;
                System.out.println("Player #" + playerCount + " has connected!");
                multiplayer.server.ServerConnection serverConnection = new multiplayer.server.ServerConnection(this, socket, playerCount);
                if(playerCount == 1) {
                    player1 = serverConnection;
                } else {
                    player2 = serverConnection;
                }
                Thread thread = new Thread(serverConnection);
                thread.start();
            }
        } catch(IOException exception) {
            System.out.println("IOException from acceptConnections() Server!");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
