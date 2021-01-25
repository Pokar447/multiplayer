package multiplayer.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Client Connection to the server
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
class ClientConnection {

    private Client client;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;

    /**
     * Client Connection constructor
     */
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

    /**
     * Sends the button number that the user clicked to the other client
     *
     * @param number Button number that the user clicked
     */
    public void sendButtonNum(int number) {
        try {
            dataOutput.writeInt(number);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendButtonNum() ClientConnection");
        }
    }

    /**
     * Receives the button number which the enemy clicked
     *
     * @return ButtonNumber Button number that the enemy clicked
     */
    public int receiveButtonNum() {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveButtonNum() ClientConnection");
        }
        return n;
    }

    /**
     * Sends the character id that the user chose to the other client
     *
     * @param characterId Character id that the user has chosen
     */
    public void sendCharacter (int characterId) {
        try {
            dataOutput.writeInt(characterId);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendCharacter() ClientConnection");
        }
    }

    /**
     * Sends the id of the user to the other client
     *
     * @param userId User id of the user
     */
    public void sendUserId (int userId) {
        try {
            dataOutput.writeInt(userId);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendUserId() ClientConnection");
        }
    }

    /**
     * Receives the character id that the other client has chosen
     *
     * @return CharacterId Character id the enemy has chosen
     */
    public int receiveCharacter() {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveCharacter() ClientConnection");
        }
        return n;
    }

    /**
     * Receives the user id that the other client has chosen
     *
     * @return UserId User id the enemy has chosen
     */
    public int receiveUserId() {
        int n = -1;
        try {
            n = dataInput.readInt();
        } catch (IOException exception) {
            System.out.println("IOException from receiveUserId() ClientConnection");
        }
        return n;
    }

    /**
     * Sends the ready state to the other client
     */
    public void sendUserReady () {
        System.out.println("User is ready!");

        try {
            dataOutput.writeInt(1);
            dataOutput.flush();
        } catch (IOException exception) {
            System.out.println("IOException from sendUserReady() ClientConnection");
        }
    }

    /**
     * Receives the ready state from the other client
     *
     * @return ReadyState Information if the other client is ready
     */
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
