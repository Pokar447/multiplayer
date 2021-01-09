package multiplayer.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientConnection {

    private Client client;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;

    public ClientConnection() {
        System.out.println("-----Client-----");
        try {
            socket = new Socket("localhost", 8888);
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
            Client.playerID = dataInput.readInt();
            if (Client.playerID == 1) {
                Client.otherID = 2;
            } else {
                Client.otherID = 1;
            }
            Client.connectionStatus = 1;
            System.out.println("Connected to Server as Player #" + Client.playerID + "!");
        } catch (IOException exception) {
            Client.connectionStatus = 0;
            System.out.println("IOException from ClientConnection Constructor!");
        }
    }

    public void sendButtonNum(int number) {
        try {
            dataOutput.writeInt(number);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendButtonNum() ClientConnection");
        }
    }

    public int receiveButtonNum() {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveButtonNum() ClientConnection");
        }
        return n;
    }

    public void sendCharacter (int characterId) {
        System.out.println("sendCharacter: " + characterId);
        try {
            dataOutput.writeInt(characterId);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendButtonNum() ClientConnection");
        }
    }

    public int receiveCharacter() {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveButtonNum() ClientConnection");
        }
        return n;
    }

    public void sendUserReady () {
        System.out.println("User is ready!");

        try {
            dataOutput.writeInt(1);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendUserReady() ClientConnection");
        }
    }

    public int receiveUserReady () {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveUserReady() ClientConnection");
        }
        return n;
    }
}
