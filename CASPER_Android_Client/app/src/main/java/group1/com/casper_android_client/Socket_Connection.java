package group1.com.casper_android_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Socket_Connection extends AppCompatActivity {

    // Input and output text fields
    private TextView response;
    private TextView address;
    private TextView port;
    private TextView message;
    private TextView error;

    // Buttons
    private Button connectButton;
    private Button clearButton;
    private Button sendButton;

    // View handleing to access when in asynctask
    private View errorView;

    // Test for video streaming
    private ImageView testImage;
    Bitmap bitmap;

    // Progress Bar
    private ProgressBar loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket__connection);

        testImage = (ImageView)findViewById(R.id.bild);

        // Progressbar
        loading = (ProgressBar)findViewById(R.id.loading);

        // user login inputfields
        address = (TextView)findViewById(R.id.address);
        port = (TextView)findViewById(R.id.port);
        message = (TextView)findViewById(R.id.message);

        // Display for server messages
        response = (TextView)findViewById(R.id.response);
        error = (TextView)findViewById(R.id.errorMsg);

        // Activity buttons
        connectButton = (Button)findViewById(R.id.connectButton);
        clearButton = (Button)findViewById(R.id.clearButton);
        sendButton = (Button)findViewById(R.id.sendButton);

        // Set listener to connect Button
        connectButton.setOnClickListener(buttonConnectOnClickListener);

        // Clear server messages listener
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Singleton.getInstance().getSocket().isBound()) {
                    response.setText("");
                    try {
                        error.setVisibility(v.VISIBLE);
                        error.setTextColor(Color.rgb(255, 0, 0));
                        error.setText("Logged out");
                        Singleton.getInstance().getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    error.setVisibility(v.VISIBLE);
                    error.setTextColor(Color.rgb(255, 0, 0));
                    error.setText("Can't Log Out Becouse you are not Logged in");
                }
            }
        });
    }


    /**
     * Create the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    /**
     * Menu button actions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Settings:
                System.out.println("Going to settings");
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                System.out.println("logging out");
                logout();
                return true;
            default:
                return false;
        }
    }

    /**
     * Logout method closing all Socket connections.
     */
    public void logout(){
        // Debug
        System.out.println("--------------------->Bound? " + Singleton.getInstance().getSocket().isBound());
        System.out.println("---------------------->closed? " + Singleton.getInstance().getSocket().isClosed());
        if(Singleton.getInstance().getSocket().isBound()) {
            // Close Socket
            try {
                finish();
                Singleton.getInstance().getSocket().close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // Transfer to main
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            error.setText("You are not logged in!");
        }

    }

    /**
     * Progressbar method for visability
     * @param v
     */
    public void setVisable(View v){
         error.setVisibility(v.VISIBLE);
    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    loading.setVisibility(v.VISIBLE);
                    // Save our view localy
                    errorView = v;
                    // Debug
                    System.out.println(address.getText().toString());
                    // If the fields are empty show an error
                    if(address.getText().toString().equals("") || port.getText().toString().equals("")){

                        // Address or Port fields are empty, show user error
                        error.setText("Please fill in the field's");
                        error.setVisibility(v.VISIBLE);
                        loading.setVisibility(v.INVISIBLE);

                    }else {
                        // convertion task
                        MyClientTask myClientTask = new MyClientTask(
                                address.getText().toString(),
                                Integer.parseInt(port.getText().toString()));
                        myClientTask.execute();
                    }
                    }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        // Convesion variables
        String dstAddress;
        int dstPort;
        String responsemsg = "";
        byte[] messages = new byte[60000];
        String text;


        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... v) {


            /**
             *
             * TEST UDP CONNECTION
             *
             * TODO Make functional implementation
             * A no need to write in connection details
             * Set fixed Ports UDP 9998 % TCP 9999
             *
             */

            try {
                // Start a TCP socket connection
                Singleton.getInstance().setSocket(new Socket(dstAddress, dstPort));

//                String messageStr="Request!";
//                // Socket Port
//                int udpSocketPort = 9998;
//                // New Socket
//                DatagramSocket UDPsocket = new DatagramSocket();
//                // Create an InetAddress
//                InetAddress casper = InetAddress.getByName("192.168.10.1");
//
//                // Output msg lenght
//                int msg_length= messageStr.length();
//                // Msg to bytes convertion
//                byte[] message = messageStr.getBytes();
//                // Package the message
//                DatagramPacket msgPacket = new DatagramPacket(message, msg_length,casper,udpSocketPort);
//                // Send Message over UDP
//                UDPsocket.send(msgPacket);
//
//                // Incomming message
//                byte[] incImage = new byte[60000];
//                // Pacckage incomming message
//                DatagramPacket imagePacket = new DatagramPacket(incImage, incImage.length,casper,udpSocketPort);
//
//                // Get data from incomming buffer
//                UDPsocket.receive(imagePacket);
//
//                // Debug
//                System.out.println(">>>>>>:" + imagePacket.getData().length);
//
//                // convert .JPG image into Bitmap
//                final Bitmap bMap = BitmapFactory.decodeByteArray(imagePacket.getData(), 0, imagePacket.getData().length);
//
//                // Debug
//                System.out.println(">>>>>>"+bMap.getHeight());
//                System.out.println(">>>>>"+bMap.getWidth());
//
//                // Set imageview to incomming bitmap
//                runOnUiThread(new Runnable() {
//                                  @Override
//                                  // UI element handling has to be run in UI thread
//                                  public void run() {
//                                      testImage.setImageBitmap(bMap);
//                                      testImage.invalidate();
//                                  }
//                              }
//
//                );



                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      loading.setVisibility(errorView.INVISIBLE);
                                          setVisable(errorView);
                                          error.setTextColor(Color.rgb(0, 255, 0));
                                          error.setText("server is reacheble");
                                      }
                                  }

                    );



                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = Singleton.getInstance().getSocket().getInputStream();


                // Get server address as message
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      loading.setVisibility(errorView.INVISIBLE);
                                      response.setText("Server respond -> " + Singleton.getInstance().getSocket().getInetAddress().toString());
                                  }
                              }

                );

                /*
                 * notice:
                 * inputStream.read() will block if no data return
                */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    responsemsg += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                responsemsg = "UnknownHostException: " + e.toString();
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      loading.setVisibility(errorView.INVISIBLE);
                                      setVisable(errorView);
                                      error.setTextColor(Color.rgb(255,0,0));
                                      error.setText("server is unreacheble!");
                                  }
                              }

                );

            } catch (IOException e) {
                // TODO Auto-generated catch block
                // Show user there was a problem
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      loading.setVisibility(errorView.INVISIBLE);
                                      setVisable(errorView);
                                      error.setTextColor(Color.rgb(255,0,0));
                                      error.setText("server is unreacheble!");
                                  }
                              }

                );
                e.printStackTrace();
                responsemsg = "IOException: " + e.toString();
            } finally {

                if (Singleton.getInstance().getSocket() != null) {
                    try {
                        // Try closeing the socket connection.
                        Singleton.getInstance().getSocket().close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    /**
     * Send a message to the socket server and print it.
     * @param v
     */
    public void sendMessageToServer(View v) {
        // If no msg was inputted into the message field
        // and user presses the send button show Error
        if (message.getText().toString().equals("")) {

            error.setVisibility(v.VISIBLE);
            error.setTextColor(Color.rgb(255, 0, 0));
            error.setText("No Message to send");

        } else {
            // Grab the text in the msg field adn send to server
            error.setVisibility(v.INVISIBLE);
            String str = message.getText().toString();
            try {
                byte[] msg = new byte[str.length()+2];
                for(int i=0;i<str.length();i++){
                    msg[i] = (byte)str.charAt(i);
                    System.out.println(msg[i]);
                }
                msg[str.length()] = 0xd;
                msg[str.length()+1] = 0xa;
                sendBytes(msg);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Bytearray socket function
     * @param myByteArray
     * @throws IOException
     */
    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

    /**
     * Method for sending Bytearrays over TCP socket connection
     * @param myByteArray
     * @param start
     * @param len
     * @throws IOException
     */
    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);

        OutputStream out = Singleton.getInstance().getSocket().getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        if (len > 0) {
            dos.write(myByteArray);
        }
    }



}