package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network.ClientThread;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private Button getTimeButton = null;
    private TextView timeTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e("error", "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private GetTimeButtonClickListener getTimetButtonClickListener = new GetTimeButtonClickListener();
    private class GetTimeButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e("error", "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            timeTextView.setText("");

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    "string",
                    timeTextView);
            clientThread.start();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        getTimeButton = (Button)findViewById(R.id.get_time_button);
        getTimeButton.setOnClickListener(getTimetButtonClickListener);
        timeTextView = (TextView)findViewById(R.id.time_text_view);

    }


}
