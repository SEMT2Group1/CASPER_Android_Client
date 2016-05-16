package group1.com.casper_android_client;

import java.net.Socket;

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


    private DriveSocket driveSocket;

    public void setDriveSocket(DriveSocket driveSocket){
        this.driveSocket = driveSocket;
    }

    public DriveSocket getDriveSocket(){
        return this.driveSocket;
    }

    private byte[] drivePackage = {0x44, (byte) 'I', (byte) 'I', (byte) 0, (byte) 0, 0x0d, 0x0a, 0x04};

    public void setDrivePackage(byte[] tcpPacket){
        this.drivePackage = tcpPacket;
    }

    public byte[] getDrivePackage(){
        return this.drivePackage;
    }


}
