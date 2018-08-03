package e.chandrakumar.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gov.nasa.worldwind.BasicWorldWindowController;
import gov.nasa.worldwind.PickedObject;
import gov.nasa.worldwind.PickedObjectList;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Offset;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globe.Globe;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.Layer;
import gov.nasa.worldwind.layer.LayerList;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.Color;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.shape.Highlightable;
import gov.nasa.worldwind.shape.Label;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;
import gov.nasa.worldwind.shape.TextAttributes;

import static android.app.Activity.RESULT_OK;


public class UserInputFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private WorldWindow wwd;
    private String unzipedLocation,userLocation;
    private String extraString;
    private Context context;
    Spinner options;
    boolean user;
    private final int PICKFILE_REQUEST_CODE=11;

    public UserInputFragment() {
    }

    /**
     * Creates a new WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow createWorldWindow() {
        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
        this.wwd = new WorldWindow(getContext());
        // Setup the WorldWindow's layers.
        this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
        this.getWorldWindow().setWorldWindowController(new PickNavigateController());
        init();
        return this.wwd;
    }

    /**
     * Gets the WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow getWorldWindow() {
        return this.wwd;
    }

    /**
     * Adds the WorldWindow to this Fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_input, container, false);
        FrameLayout globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);

        assert getArguments() != null;
        unzipedLocation = getArguments().getString("unziped");
        user=false;
        Log.d("Received as ",unzipedLocation);
        if(!user) {
            getAllFilesOfDir(new File(unzipedLocation));
            extraString= new File(unzipedLocation).getName();
        }
        else {
            getAllFilesOfDir(new File(userLocation));
            extraString= new File(userLocation).getName();
        }
        Log.d("Extra String",extraString);
        // Add the WorldWindow view object to the layout that was reserved for the globe.
        globeLayout.addView(this.createWorldWindow());
        context=getActivity().getApplicationContext();
        options=rootView.findViewById(R.id.options);
        options.setOnItemSelectedListener(this);
        return rootView;
    }
    private void getAllFilesOfDir(File directory) {
        Log.d("Application", "Directory: " + directory.getAbsolutePath() + "\n");

        final File[] files = directory.listFiles();

        if ( files != null ) {
            for ( File file : files ) {
                if ( file != null ) {
                    if ( file.isDirectory() ) {  // it is a folder...
                        getAllFilesOfDir(file);
                    }
                    else {  // it is a file...
                        Log.d("Application", "File: " + file.getAbsolutePath() + "\n");
                    }
                }
            }
        }
    }
    /**
     * Resumes the WorldWindow's rendering thread
     */
    @Override
    public void onResume() {
        super.onResume();
        this.wwd.onResume(); // resumes a paused rendering thread
    }

    /**
     * Pauses the WorldWindow's rendering thread
     */
    @Override
    public void onPause() {
        super.onPause();
        this.wwd.onPause(); // pauses the rendering thread
    }
    private void init(){
        //get Station name from index file
        Helper helper;
        if(!user) {
             helper= new Helper(unzipedLocation);
        }
        else {
            helper= new Helper(userLocation);
        }
        getStationDetails(helper);
        fedHistoricalData(helper);
        getPredictResults(helper);
        //get weather details from corresponding file
        getWeatherDetails(helper);
        fedLiveData();
        fedGifData();
        //get predict details from corresponding file
    }
    List<LatLng> humidtyPlacemarker=new ArrayList<>();
    List<LatLng> liveDataLatLng=new ArrayList<>();
    List<Placemark> liveDataPlacemarker=new ArrayList<>();
    void fedLiveData(){
        liveDataLatLng.add(new LatLng(9.729204,77.293778));
        liveDataLatLng.add(new LatLng(9.729204,77.293778));
        liveDataLatLng.add(new LatLng(9.832351,77.390634));
        liveDataLatLng.add(new LatLng(10.018025,77.487703));
        liveDataLatLng.add(new LatLng(10.115655,77.540686));
        liveDataLatLng.add(new LatLng(10.151098,77.646999));
        liveDataLatLng.add(new LatLng(10.157442,77.753895));
    }
    void resetWorldWind(){
        LayerList layers=wwd.getLayers();
        layers.clearLayers();
        this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
    }
    int temp=0;
    TextAttributes tempattr;
    Label tempLable;
    Placemark placemark;
    RenderableLayer placemarksLayer;
    void showTempData(){
        placemarksLayer = new RenderableLayer("Live data");
        Random rand = new Random();
        for (int i=0;i<liveDataLatLng.size();i++){
            temp=i;
            Log.d("Placing lat",liveDataLatLng.get(i).latitude+"");

            placemark = new Placemark(
                    Position.fromDegrees(liveDataLatLng.get(i).getLatitude(), liveDataLatLng.get(i).getLongitude(), 0),
                    PlacemarkAttributes.createWithImage(ImageSource.fromResource(R.drawable.live)).setImageOffset(Offset.center()).setImageScale(4));
            placemark.setEnabled(true);
            TextAttributes attrs = new TextAttributes();
            attrs.setTextColor(new Color(0, 0, 0, 1)); // black text via r,g,b,a
            attrs.setOutlineColor(new Color(1, 1, 1, 1)); // white outline via r,g,b,a
            attrs.setOutlineWidth(5);
            tempattr=attrs;


            RequestQueue queue= Volley.newRequestQueue(context);
            String foo="http://api.openweathermap.org/data/2.5/weather?";
            final String lat="lat="+liveDataLatLng.get(i).latitude;
            String lng="&lon="+liveDataLatLng.get(i).longitude;
            String boo="&APPID=05ce636a5ff38984c6e9c4338375389d";
            String url=foo+lat+lng+boo;

            StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int ti = response.indexOf("\"temp\":");
                            String new_txt = "";
                            Double temp1;
                            for(int i=ti+7;i<ti+13;i++)
                                new_txt = new_txt+response.charAt(i);

                            Log.d("Value is ",new_txt+"");
                            temp1 = Double.parseDouble(new_txt)-273.15;
                            for(int i=0;i<liveDataLatLng.size();i++){
                                LatLng latLng=liveDataLatLng.get(i);
                                DecimalFormat dec = new DecimalFormat("#0.00");
                                String latitude=dec.format(latLng.latitude)+"";
                                String longitude=dec.format(latLng.longitude)+"";
                                Log.d("Response",response);
                                Log.d("Latitude",latitude);
                                Log.d("longitude",longitude);
                                if (response.contains(latitude)&&response.contains(longitude)){
                                    temp=i;
                                    Log.d("Value is ",temp+""+new_txt );
                                    break;
                                }
                            }
                            tempLable = new Label(new Position(liveDataLatLng.get(temp).latitude, liveDataLatLng.get(temp).longitude, 15), temp1+ "", tempattr);
                            placemarksLayer.addRenderable(tempLable);
                            placemark.setHighlighted(false);
                            liveDataPlacemarker.add(placemark);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"Error retriving data",Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);

//            lookAt(wwd,Position.fromDegrees(historicalDataLatLng.get(i).getLatitude(),historicalDataLatLng.get(i).getLongitude(),10000));
            placemarksLayer.addRenderable(placemark);
        }
        wwd.getLayers().addLayer(placemarksLayer);
    }
    private String getTemp(double val,double val1){
        Log.d("Values are",val+" "+val1+" result is"+val+"."+val1);
        DecimalFormat dec = new DecimalFormat("#0.00");
        return dec.format(val+val1);
    }

    void showHumidityData(){
         placemarksLayer= new RenderableLayer("Humidity data");
        Random rand = new Random();
        for (int i=0;i<liveDataLatLng.size();i++){
            temp=i;
            Log.d("Placing lat",liveDataLatLng.get(i).latitude+"");

             placemark = new Placemark(
                    Position.fromDegrees(liveDataLatLng.get(i).getLatitude(), liveDataLatLng.get(i).getLongitude(), 0),
                    PlacemarkAttributes.createWithImage(ImageSource.fromResource(R.drawable.live)).setImageOffset(Offset.center()).setImageScale(4));
            placemark.setEnabled(true);
            TextAttributes attrs = new TextAttributes();
            attrs.setTextColor(new Color(0, 0, 0, 1)); // black text via r,g,b,a
            attrs.setOutlineColor(new Color(1, 1, 1, 1)); // white outline via r,g,b,a
            attrs.setOutlineWidth(5);
            tempattr=attrs;
            RequestQueue queue= Volley.newRequestQueue(context);
            String foo="http://api.openweathermap.org/data/2.5/weather?";
            final String lat="lat="+liveDataLatLng.get(i).latitude;
            String lng="&lon="+liveDataLatLng.get(i).longitude;
            String boo="&APPID=05ce636a5ff38984c6e9c4338375389d";
            String url=foo+lat+lng+boo;

            StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int ti2 = response.indexOf("\"humidity\"");
                            String new_txt = "";
                            Double hum = 0.0;
                            for (int i = ti2 + 11; i < ti2 + 13; i++)
                                new_txt = new_txt + response.charAt(i);

                            hum = Double.parseDouble(new_txt);
                            for(int i=0;i<liveDataLatLng.size();i++){
                                LatLng latLng=liveDataLatLng.get(i);
                                DecimalFormat dec = new DecimalFormat("#0.00");
                                String latitude=dec.format(latLng.latitude)+"";
                                String longitude=dec.format(latLng.longitude)+"";
                                Log.d("Response",response);
                                Log.d("Latitude",latitude);
                                Log.d("longitude",longitude);
                                if (response.contains(latitude)&&response.contains(longitude)){
                                    temp=i;
                                    Log.d("Value is ",temp+""+new_txt );
                                    break;
                                }
                            }
                            tempLable = new Label(new Position(liveDataLatLng.get(temp).latitude, liveDataLatLng.get(temp).longitude, 15), hum + "", tempattr);
                            placemarksLayer.addRenderable(tempLable);
                            placemark.setHighlighted(false);
                            liveDataPlacemarker.add(placemark);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"Error retriving data",Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);

//            lookAt(wwd,Position.fromDegrees(historicalDataLatLng.get(i).getLatitude(),historicalDataLatLng.get(i).getLongitude(),10000));
            placemarksLayer.addRenderable(placemark);
        }
        wwd.getLayers().addLayer(placemarksLayer);
    }
    List<LatLng> gifDataLatLng=new ArrayList<>();
    List<Placemark> gifDataPlacemarker=new ArrayList<>();
    void fedGifData(){
        gifDataLatLng.add(new LatLng(9.799219,77.293790));
    }
    void showGifData(){
        RenderableLayer placemarksLayer = new RenderableLayer("Gif data");
        for (int i=0;i<gifDataLatLng.size();i++){
            Log.d("Placing lat",gifDataLatLng.get(i).latitude+"");

            Placemark placemark = new Placemark(
                    Position.fromDegrees(gifDataLatLng.get(i).getLatitude(), gifDataLatLng.get(i).getLongitude(), 0),
                    PlacemarkAttributes.createWithImage(ImageSource.fromResource(R.drawable.gif)).setImageOffset(Offset.center()).setImageScale(6),
                    "Analysis");
            placemark.setDisplayName("Analysis");
            placemark.setEnabled(true);
            TextAttributes attrs = new TextAttributes();
            attrs.setTextColor(new Color(0, 0, 0, 1)); // black text via r,g,b,a
            attrs.setOutlineColor(new Color(1, 1, 1, 1)); // white outline via r,g,b,a
            attrs.setOutlineWidth(5);
            Label label = new Label(new Position(gifDataLatLng.get(i).latitude, gifDataLatLng.get(i).longitude, 15), "Analysis",attrs);
            placemarksLayer.addRenderable(label);
            placemark.setHighlighted(true);
            final String SELECTABLE = "Selectable";
            placemark.putUserProperty(SELECTABLE,null);
            gifDataPlacemarker.add(placemark);
//            lookAt(wwd,Position.fromDegrees(historicalDataLatLng.get(i).getLatitude(),historicalDataLatLng.get(i).getLongitude(),10000));
            placemarksLayer.addRenderable(placemark);
        }
        wwd.getLayers().addLayer(placemarksLayer);
    }
    List<LatLng> historicalDataLatLng=new ArrayList<>();
    List<Placemark> historicalData=new ArrayList<>();
    private void fedHistoricalData(Helper helper){
        historicalDataLatLng=helper.historicalDataLatLng;
    }
    private void showHistoricalData(){
        RenderableLayer placemarksLayer = new RenderableLayer("Live data");
        for (int i=0;i<historicalDataLatLng.size();i++){
            Log.d("Placing lat",historicalDataLatLng.get(i).latitude+"");

            Placemark placemark = new Placemark(
                    Position.fromDegrees(historicalDataLatLng.get(i).getLatitude(), historicalDataLatLng.get(i).getLongitude(), 0),
                    PlacemarkAttributes.createWithImage(ImageSource.fromResource(R.drawable.historical)).setImageOffset(Offset.center()).setImageScale(4),
                    stationDetails.get(i).getName());
            placemark.setDisplayName(stationDetails.get(i).getName());
            placemark.setEnabled(true);
            TextAttributes attrs = new TextAttributes();
            attrs.setTextColor(new Color(0, 0, 0, 1)); // black text via r,g,b,a
            attrs.setOutlineColor(new Color(1, 1, 1, 1)); // white outline via r,g,b,a
            attrs.setOutlineWidth(5);
            Label label = new Label(new Position(historicalDataLatLng.get(i).latitude, historicalDataLatLng.get(i).longitude, 15), stationDetails.get(i).getName(),attrs);
            placemarksLayer.addRenderable(label);
            placemark.setHighlighted(true);
            final String SELECTABLE = "Selectable";
            placemark.putUserProperty(SELECTABLE,null);
            historicalData.add(placemark);
            lookAt(wwd,Position.fromDegrees(historicalDataLatLng.get(i).getLatitude(),historicalDataLatLng.get(i).getLongitude(),1000));
            placemarksLayer.addRenderable(placemark);
        }
        wwd.getLayers().addLayer(placemarksLayer);
    }
    List<StationDetails> stationDetails=new ArrayList<>();
    List<WeatherDetails> weatherDetails=new ArrayList<>();
    List<PredictionResults> predictionResults = new ArrayList<>();
    private void getStationDetails(Helper helper){
        this.stationDetails=helper.stationDetails;
    }
    private void lookAt(WorldWindow wwd, Position airport){
        Position aircraft = new Position(airport.latitude, airport.longitude, airport.altitude);

        // Compute heading and distance from aircraft to airport
        Globe globe = wwd.getGlobe();
        double heading = aircraft.greatCircleAzimuth(airport);
        double distanceRadians = aircraft.greatCircleDistance(airport);
        double distance = distanceRadians * globe.getRadiusAt(aircraft.latitude, aircraft.longitude);

        // Compute camera settings
        double altitude = airport.altitude;
        double range = Math.sqrt(altitude * altitude + distance * distance);
        double tilt = Math.toDegrees(Math.atan(distance / aircraft.altitude));

        // Apply the new view
        LookAt lookAt = new LookAt();
        lookAt.set(airport.latitude, airport.longitude, airport.altitude, WorldWind.ABSOLUTE, range, heading, tilt, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(globe, lookAt);

    }
    private void getWeatherDetails(Helper helper) {
        weatherDetails=helper.weatherDetails;
    }
    private void getPredictResults(Helper helper){
        predictionResults=helper.predictionResults;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                user=false;
                init();
                resetWorldWind();
                showHistoricalData();
                showGifData();
                break;
            case 1:
                resetWorldWind();
                showHumidityData();
                break;
            case 2:
                resetWorldWind();
                showTempData();
                break;
            case 3:
                user=true;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/zip");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                init();
                resetWorldWind();
                showHistoricalData();
                showGifData();
                break;
        }
    }
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         switch(requestCode) {
             case PICKFILE_REQUEST_CODE:
                 if (resultCode == RESULT_OK) {
                     userLocation = data.getData().getPath();
                     userLocation=Uri.decode(unzipedLocation);
                     Toast.makeText(context,userLocation,Toast.LENGTH_SHORT).show();
                     Log.d("Data", userLocation);
                     Decompress d = new Decompress(userLocation, userLocation);
                     d.unzip();

                 }
                 break;
         }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class PickNavigateController extends BasicWorldWindowController {
        protected Object pickedObject;          // last picked object from onDown events

        protected Object selectedObject;        // last "selected" object from single tap

        /**
         * Assign a subclassed SimpleOnGestureListener to a GestureDetector to handle the "pick" events.
         */
        protected GestureDetector pickGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                Log.d("Motion","Entering on Down Event");
                pick(event);// Pick the object(s) at the tap location
                Log.d("Motion","Exiting on Down Event");
                return false;   // By not consuming this event, we allow it to pass on to the navigation gesture handlers
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("Motion","Entering on single tap Event");
                toggleSelection(e);  // Highlight the picked object
                Log.d("Motion","Extiting on single tap Event");

                // By not consuming this event, we allow the "up" event to pass on to the navigation gestures,
                // which is required for proper zoom gestures.  Consuming this event will cause the first zoom
                // gesture to be ignored.  As an alternative, you can implement onSingleTapConfirmed and consume
                // event as you would expect, with the trade-off being a slight delay tap response.
                return false;
            }
        });

        /**
         * Delegates events to the pick handler or the native WorldWind navigation handlers.
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Allow pick listener to process the event first.
            Log.d("Motion","Entering on touch Event");
            boolean consumed = this.pickGestureDetector.onTouchEvent(event);
            // If event was not consumed by the pick operation, pass it on the globe navigation handlers
            if (!consumed) {
                Log.d("Motion","on touch  Event is not consumed");

                // The super class performs the pan, tilt, rotate and zoom
                return super.onTouchEvent(event);
            }
            else {
                Log.d("Motion","on touch  Event is consumed");
                Toast.makeText(context, "Touched", Toast.LENGTH_SHORT).show();
            }
            Log.d("Motion","Exiting on touch  Event is consumed");
            return consumed;
        }

        /**
         * Performs a pick at the tap location.
         */
        public void pick(MotionEvent event) {
            Log.d("Motion","Entering pick ");
            // Forget our last picked object
            this.pickedObject = null;

            // Perform a new pick at the screen x, y
            PickedObjectList pickList = getWorldWindow().pick(event.getX(), event.getY());

            // Get the top-most object for our new picked object
            PickedObject topPickedObject = pickList.topPickedObject();
            if (topPickedObject != null) {
                Log.d("Motion","Entering top picked object");
                this.pickedObject = topPickedObject.getUserObject();

//                Toast.makeText(context, ((Renderable) pickedObject).getDisplayName(), Toast.LENGTH_SHORT).show();
            }
            Log.d("Motion","Exiting pick ");
        }

        /**
         * Toggles the selected state of a picked object.
         */
        public void toggleSelection(MotionEvent event) {
            Log.d("Motion","Entering toggleSelection");
            // Display the highlight or normal attributes to indicate the
            // selected or unselected state respectively.
            if (pickedObject instanceof Highlightable) {
                if("Analysis".equals(((Placemark) pickedObject).getDisplayName())){
                     Intent intent=new Intent(context,GifActivity.class);
                    context.startActivity(intent);
                }
                Log.d("Motion","Entering toggleSelection instance of highlatable and new selection"+event.getRawY()+"  "+event.getRawY());
                Log.d("Various",event.getXPrecision()+ " "+event.getYPrecision()+" "+event.getX()+" "+event.getY());
                Log.d("Getting Name",((Placemark) pickedObject).getDisplayName());
                StationDetails target=new StationDetails();
                WeatherDetails details=new WeatherDetails();
                PredictionResults results=new PredictionResults();
                Boolean found=false;
                for (int i=0;i<stationDetails.size();i++){
                    if(stationDetails.get(i).getName().equals(((Placemark) pickedObject).getDisplayName())){
                        target=stationDetails.get(i);
                        details=weatherDetails.get(i);
                        results=predictionResults.get(i);
                        found=true;
                    }
                }
                if(found) {
                    Intent intent = new Intent(context, GraphActivity.class);
                    intent.putExtra("station", target);
                    intent.putExtra("weather", details);
                    intent.putExtra("prediction", results);
                    context.startActivity(intent);
                }
                Log.d("Motion","Entering toggleSelection instance of highlatable");
                // Determine if we've picked a "new" object so we know to deselect the previous selection
                boolean isNewSelection = pickedObject != this.selectedObject;

                // Only one object can be selected at time; deselect any previously selected object
                if (isNewSelection && this.selectedObject instanceof Highlightable) {

                    Log.d("Motion","Entering toggleSelection instance of highlatable and new selection"+event.getX()+""+event.getY());

                    ((Highlightable) this.selectedObject).setHighlighted(false);
                }

                // Show the selection by showing its highlight attributes and enunciating the name
                if (isNewSelection && pickedObject instanceof Renderable) {
                    Log.d("Motion","Entering toggleSelection instance of highlatable and new selection and renderable");
                    if(found) {
                        Toast.makeText(context, ((Renderable) pickedObject).getDisplayName(), Toast.LENGTH_SHORT).show();
                    }
                    else if(!("Analysis".equals(((Placemark) pickedObject).getDisplayName()))){
                        Toast.makeText(context,"Live data from OWM", Toast.LENGTH_SHORT).show();

                    }
                }
                ((Highlightable) pickedObject).setHighlighted(isNewSelection);
                this.getWorldWindow().requestRedraw();
                Log.d("Motion","Entering toggleSelection instance of highlatable and requresting for redraw");

                // Track the selected object
                this.selectedObject = isNewSelection ? pickedObject : null;
            }
        }
    }
}
