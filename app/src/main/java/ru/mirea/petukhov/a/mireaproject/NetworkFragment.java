package ru.mirea.petukhov.a.mireaproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ru.mirea.petukhov.a.mireaproject.ui.SocketUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkFragment newInstance(String param1, String param2) {
        NetworkFragment fragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_network, container, false);
        Button button = root.findViewById(R.id.getInfoButton);
        button.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        new GetTimeTask().execute();
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params){
            String timeResult = "";
            try{
                String host = "time.nist.gov";
                int port = 13;
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine();
                timeResult = reader.readLine();
                Log.d(MainActivity.class.getSimpleName(), timeResult);
                socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return timeResult;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            //LocalDate date = LocalDate.parse(result);
            String[] subStrings = result.split(" ");
            Toast.makeText(getContext(), subStrings[1] + " - " + subStrings[2], Toast.LENGTH_LONG).show();
        }
    }
}