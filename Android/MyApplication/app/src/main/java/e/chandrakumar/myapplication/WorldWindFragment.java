package e.chandrakumar.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mikhaellopez.lazydatepicker.LazyDatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globe.BasicElevationCoverage;
import gov.nasa.worldwind.globe.ElevationCoverage;
import gov.nasa.worldwind.globe.Globe;
import gov.nasa.worldwind.globe.TiledElevationCoverage;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.ogc.Wcs100ElevationCoverage;
import gov.nasa.worldwind.ogc.Wcs201ElevationCoverage;
import gov.nasa.worldwind.ogc.WmsLayer;
import gov.nasa.worldwind.ogc.WmsLayerConfig;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.SurfaceImage;


public class WorldWindFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private WorldWindow wwd;
    Spinner spinner_layer;
    LazyDatePicker datePicker;
    WmsLayer layer;
    WmsLayerConfig config;
    ImageView legends;
    Button areaOfInterest;
    public WorldWindFragment() {
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
        config = new WmsLayerConfig();
        config.serviceAddress = "https://neowms.sci.gsfc.nasa.gov/wms/wms";
        config.wmsVersion = "1.1.1"; // NEO server works best with WMS 1.1.1

//         Add the WMS layer to the WorldWindow.

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
        View rootView = inflater.inflate(R.layout.fragment_world_wind, container, false);
        FrameLayout globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);


        init(rootView);
        // Add the WorldWindow view object to the layout that was reserved for the globe.
        globeLayout.addView(this.createWorldWindow());

//        createElevation(wwd);

        return rootView;
    }
    private void createElevation(WorldWindow wwd){
        Sector coverageSector = Sector.fromDegrees(9.715846,77.484605,0.001,0.001);
        int numberOfLevels = 2;
        int resourceId = R.drawable.local;
        SurfaceImage surfaceImageResource = new SurfaceImage(coverageSector, ImageSource.fromResource(resourceId));
        RenderableLayer layer = new RenderableLayer("Surface Image");
        layer.addRenderable(surfaceImageResource);
        wwd.getLayers().addLayer(layer);
        Position lookAtPos=new Position(9.715846,77.484605,200);
        lookAt(wwd,lookAtPos);
    }

    private void init(View rootView){
        spinner_layer=rootView.findViewById(R.id.spinner_layer);
        datePicker=rootView.findViewById(R.id.date_for_layer);
        legends=rootView.findViewById(R.id.legends);
        Date maxDate= Calendar.getInstance().getTime();
        areaOfInterest=rootView.findViewById(R.id.area_of_interest);
        areaOfInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookAt(wwd,Position.fromDegrees(9.729204,77.293778,1000));
            }
        });
        datePicker.setMaxDate(maxDate);
        spinner_layer.setOnItemSelectedListener(this);
        datePicker.setOnDatePickListener(new LazyDatePicker.OnDatePickListener() {
            @Override
            public void onDatePick(Date dateSelected) {
                hideKeyboard(getActivity());
                if(layer!=null)
                    wwd.getLayers().removeLayer(layer);
                Log.d("Date picker","A date is selected");
                String dateString=cnvtDateToString(dateSelected.getTime());
                Log.d("Date picked is ","2013-31-01 14:10:00".equals(dateString)+"");
                config.timeString="2013-31-01 14:10:00";
//                config.timeString=dateString;
                layer = new WmsLayer(new Sector().setFullSphere(), 10, config);// 1km resolution
                Log.d("Website",config.toString());
                wwd.getLayers().addLayer(layer);
            }
        });
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
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        datePicker.setVisibility(View.INVISIBLE);
        legends.setVisibility(View.INVISIBLE);
    }

    //LEAF AREA INDEX MOD15A2_M_LAI  18Feb 2000- 22 Mar 2017
    //Rainfall TRMM_3B43M 2-Jan 1998-30-Aug-1-2016
    //Water Vapour MYDAL2_D_SKY_WV 5 JUly-2002 11 July -2018
    //LAND SURFACE TEMPERATURE MOD11C1_D_LSTDA 25-feb-2000 27-Mar-2017
    //VEGETATION INDEX MOD_NDVI_M 18Feb 2000- 11 Jul 2018
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        legends.setVisibility(View.VISIBLE);
        String[] types_of_layer = getResources().getStringArray(R.array.layer_names);
        String[] min_date_of_layer=new String[types_of_layer.length];
        String[] max_date_of_layer=new String[types_of_layer.length];
        String[] wmsLayerName=new String[types_of_layer.length];
        legends.setVisibility(View.VISIBLE);
        int []legends=new int[types_of_layer.length];
        min_date_of_layer[0]="2000-02-18";max_date_of_layer[0]="2017-03-22";wmsLayerName[0]="MOD15A2_M_LAI";legends[0]=R.drawable.leafareaindex;
        min_date_of_layer[1]="1998-01-02";max_date_of_layer[1]="2016-08-30";wmsLayerName[1]="TRMM_3B43M";legends[1]=R.drawable.rainfall;
        min_date_of_layer[2]="2002-07-05";max_date_of_layer[2]="2018-07-11";wmsLayerName[2]="MYDAL2_D_SKY_WV";legends[2]=R.drawable.watervapor;
        min_date_of_layer[3]="2000-02-25";max_date_of_layer[3]="2017-03-27";wmsLayerName[3]="MOD11C1_D_LSTDA";legends[3]=R.drawable.landsurfacetem;
        min_date_of_layer[4]="2000-02-18";max_date_of_layer[4]="2018-07-11";wmsLayerName[4]="MOD_NDVI_M";legends[4]=R.drawable.vi;
        String selected=adapterView.getItemAtPosition(pos).toString();
        for (int i=0;i<types_of_layer.length;i++){
            if(types_of_layer[i].equals(selected)){
                if(layer!=null)
                this.wwd.getLayers().removeLayer(layer);
                datePicker.setVisibility(View.VISIBLE);
                datePicker.setMinDate(cnvtStringtoDate(min_date_of_layer[i]));
                datePicker.setMaxDate(cnvtStringtoDate(max_date_of_layer[i]));
                config.layerNames = wmsLayerName[i];
                this.legends.setImageResource(legends[i]);
//                config.timeString="2013-31-01 14:10:00";
                datePicker.clear();
                datePicker.shake();
//                this.wwd.getLayers().addLayer(layer);
                return;
            }
        }
    }


    private Date cnvtStringtoDate(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");
        try {
            Date minDate = format.parse(dtStart);
            System.out.println(minDate);
            return minDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String cnvtDateToString(Long timeStamp){
        Date date = new Date(timeStamp);
        DateFormat format = new SimpleDateFormat("yyy-dd-MM%2014:10:00");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));//your zone
        formatted = format.format(date);
        System.out.println(formatted);
        return formatted;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
