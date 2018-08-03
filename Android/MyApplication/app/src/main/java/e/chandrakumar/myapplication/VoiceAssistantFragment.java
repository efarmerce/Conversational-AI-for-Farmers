package e.chandrakumar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VoiceAssistantFragment extends Fragment  {

    ImageView connectedIV,NotConnctedIV;
    Context context;
    TextView key;
    Button connect;
    String unzippedLocation;
    DatabaseReference answeredMonitering;
    public VoiceAssistantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the device has bluetooth
        context=this.getContext().getApplicationContext();

    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUI(false);
        answeredMonitering.addValueEventListener(valueEventListener);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        reference.child("secret_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key.setText(dataSnapshot.getValue().toString());
                connect.setVisibility(View.VISIBLE);
//                updateUI(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("phone_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Integer.parseInt(dataSnapshot.getValue().toString())==2){
                    connect.setText("Connected");
                    key.setText("Connected");
                    connectedIV.setImageResource(R.drawable.connected);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("answered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bluetoothScanning();
    }
    void bluetoothScanning(){
        BluetoothAdapter  mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    Log.d("Device",device.getName() + "\n" + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
    }

    private void updateUI(Boolean connected){
        if(connected){
            connectedIV.setVisibility(View.VISIBLE);
            NotConnctedIV.setVisibility(View.GONE);
        }
        else {
            connectedIV.setVisibility(View.GONE);
            NotConnctedIV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voice_assistant, container, false);

        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        connectedIV = (ImageView)rootView.findViewById(R.id.connected);
        NotConnctedIV= (ImageView)rootView.findViewById(R.id.notConncted);
        connectedIV.setImageResource(R.drawable.notconnected);
        key=rootView.findViewById(R.id.key);
        connect=rootView.findViewById(R.id.connect);
        connect.setVisibility(View.GONE);
        connect.setOnClickListener(clickListener);
        unzippedLocation="";
        assert getArguments() != null;
        unzippedLocation = getArguments().getString("unziped");
        Helper helper=new Helper(unzippedLocation);
        getStationDetails(helper);
        fedHistoricalData(helper);
        getPredictResults(helper);
        getWeatherDetails(helper);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        answeredMonitering=database.getReference().child("answered");
        answeredMonitering.addValueEventListener(valueEventListener);
    }
    List<StationDetails> stationDetails=new ArrayList<>();
    List<PredictionResults> predictionResults=new ArrayList<>();
    List<LatLng> historicalDataLatLng=new ArrayList<>();
    List<WeatherDetails> weatherDetails=new ArrayList<>();
    private void fedHistoricalData(Helper helper) {
        historicalDataLatLng=helper.historicalDataLatLng;
    }

    private void getStationDetails(Helper helper) {
        stationDetails=helper.stationDetails;
    }

    private void getPredictResults(Helper helper) {
        predictionResults=helper.predictionResults;
    }
    private void getWeatherDetails(Helper helper){
        weatherDetails=helper.weatherDetails;
    }
    Button.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference();
            reference.child("phone_status").setValue(1).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            connect.setEnabled(false);
                            connect.setText("Connecting");
                            connectedIV.setImageResource(R.drawable.connecting);
                            key.setText("Press the button on Voice kit");
                        }
                    }
            );
        }
    };
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue()!=null) {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra("station", stationDetails.get(Integer.parseInt(dataSnapshot.getValue().toString())));
                intent.putExtra("weather", weatherDetails.get(Integer.parseInt(dataSnapshot.getValue().toString())));
                intent.putExtra("prediction", predictionResults.get(Integer.parseInt(dataSnapshot.getValue().toString())));
                context.startActivity(intent);
                answeredMonitering.removeValue();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(context,"Connection Error",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        answeredMonitering.removeEventListener(valueEventListener);
    }

}
