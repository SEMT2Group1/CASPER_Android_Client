package group1.com.casper_android_client;

import java.net.Socket;
import group1.com.casper_android_client.TCPsocket;

/**
 * Created by Andreas Fransson
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();

    /**
     *
     * @return
     */
    public static Singleton getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param ourInstance
     */
    public static void setOurInstance(Singleton ourInstance) {
        Singleton.ourInstance = ourInstance;
    }

    private Singleton() {

    }



    private String socketData;

    public String getSocketData() {
        return socketData;
    }

    public void setSocketData(String socketData) {
        this.socketData = socketData;
    }



    // User instance
    private User loggedInUser;

    /**
     * Get the user
     * @return
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Set the User
     * @param loggedInUser
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    /**
     *
     */
    private TCPsocket TCPsocket;

    public TCPsocket getTCPsocket() {
        return TCPsocket;
    }

    public void setTCPsocket(TCPsocket TCPsocket) {
        this.TCPsocket = TCPsocket;
    }

    // TCP Socket connection
    private Socket socket = new Socket();


    /**
     * Get the current TCP Socket connection
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set the current TCP socket connection
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private byte[] tcpPackage = {0x44, (byte) 'I', (byte) 'I', (byte) 0, (byte) 0, 0x0d, 0x0a, 0x04};



    public void setTcpPackage(byte[] tcpPacket){
        this.tcpPackage = tcpPacket;
    }

    public byte[] getTcpPackage(){
        return this.tcpPackage;
    }


}
