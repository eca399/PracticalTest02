package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network;

/**
 * Created by Eca on 5/20/2016.
 */
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String text;
    private TextView timeTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String text,
            TextView timeTextView) {
        this.address = address;
        this.port = port;
        this.text = this.text;
        this.timeTextView = timeTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                //Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(text);
                printWriter.flush();
                String timeInformation;
                while ((timeInformation = bufferedReader.readLine()) != null) {
                    final String finalizedWeatherInformation = timeInformation;
                    timeTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            timeTextView.append(finalizedWeatherInformation + "\n");
                        }
                    });
                }
            } else {
                //Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            //Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
           /* if (Constants.DEBUG) {
                ioException.printStackTrace();
            }*/
        }
    }

}
