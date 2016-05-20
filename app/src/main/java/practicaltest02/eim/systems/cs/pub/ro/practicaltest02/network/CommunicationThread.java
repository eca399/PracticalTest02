package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Utilities;


public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i("wainting", "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String clientString = bufferedReader.readLine();
                    HashMap<String, String> data = serverThread.getData();
                    String timeInformation = null;
                    if (clientString != null && !clientString.isEmpty()) {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet timeGet = new HttpGet("http://www.timeapi.org/utc/now");
                        ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();
                        String time = null;

                        try {
                            time = httpClient.execute(timeGet, responseHandlerGet);
                        } catch (ClientProtocolException clientProtocolException) {
                            Log.e("EIM", clientProtocolException.getMessage());
                        } catch (IOException ioException) {
                        }
                        if (time != null) {

                            if (!data.containsKey(socket.getInetAddress().toString())) {
                                serverThread.setData(socket.getInetAddress().toString(), time);
                            } else {
                                Log.i("[PracticalTest02]", "[COMMUNICATION THREAD] Getting the information from the cache...");
                                timeInformation = data.get(socket.getInetAddress().toString());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
