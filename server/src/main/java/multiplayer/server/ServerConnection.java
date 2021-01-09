package multiplayer.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ServerConnection implements Runnable {
    private final ServerApplication server;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private int playerID;

    public ServerConnection(ServerApplication server, Socket s, int id) {
        this.server = server;
        socket = s;
        playerID = id;

        try {
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException exception) {
            System.out.println("IOException from ServerConnection Constructor!");
        }
    }

    public void run() {
        try {
            dataOutput.writeInt(playerID);
            dataOutput.flush();

            while (true) {

                System.out.println("Server connection is up!");

                if (playerID == 1) {

                    if (server.user2Ready == 0) {
                        server.user2Ready = dataInput.readInt();
                        System.out.println("User 1 ready!");
                        server.player2.sendUserReady();
                    }

                    if (server.player1CharacterId == 0) {
                        server.player1CharacterId = dataInput.readInt();
                        server.player2.sendCharacter(server.player1CharacterId);
                    }

                    server.p1ButtonNum = dataInput.readInt();
                    server.player2.sendButtonNum(server.p1ButtonNum);
                } else {

                    if (server.user1Ready == 0) {
                        server.user1Ready = dataInput.readInt();
                        System.out.println("User 2 ready!");
                        server.player1.sendUserReady();
                    }
                    if (server.player2CharacterId == 0) {
                        server.player2CharacterId = dataInput.readInt();
                        server.player1.sendCharacter(server.player2CharacterId);
                    }

                    server.p2ButtonNum = dataInput.readInt();
                    server.player1.sendButtonNum(server.p2ButtonNum);
                }
            }
        } catch (IOException exception) {
            System.out.println("IOException from run() ServerConnection!");
        }
    }

    public void sendButtonNum(int buttonNum) {
        try {
            dataOutput.writeInt(buttonNum);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendButtonNum() ServerConnection");
        }
    }

    public void sendCharacter (int characterId) {
        try {
            dataOutput.writeInt(characterId);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendButtonNum() ServerConnection");
        }
    }

    public void sendUserReady () {
        try {
            dataOutput.writeInt(1);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendUserReady() ServerConnection");
        }
    }
}