package e.chandrakumar.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private String unzipedLocation;
    List<StationDetails> stationDetails=new ArrayList<>();
    List<LatLng> historicalDataLatLng=new ArrayList<>();
    List<PredictionResults> predictionResults=new ArrayList<>();
    List<WeatherDetails> weatherDetails=new ArrayList<>();
    Helper(String unzipedLocation){
        this.unzipedLocation=unzipedLocation;
        getStationDetailsFromFile();
        fedHistoricalData();
        getPredictResults();
        getWeatherDetails();
    }
    private void getStationDetailsFromFile(){
        InputStream index= null;
        try {
            index = new FileInputStream(unzipedLocation+"index.csv");
            BufferedReader reader=new BufferedReader(
                    new InputStreamReader(index, Charset.forName("UTF-8"))
            );
            try {
                reader.readLine();
                String line="";
                while ((line=reader.readLine())!=null){
                    StationDetails details=new StationDetails();
                    String[] values=line.split(",");
                    details.setName(values[0]);
                    details.setLatitude(Double.parseDouble(values[1]));
                    details.setLongitude(Double.parseDouble(values[2]));
                    stationDetails.add(details);
                    Log.d("Station name is",values[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void fedHistoricalData(){
        for (StationDetails stationDetails:stationDetails){
            LatLng latLng=new LatLng();
            latLng.setLongitude(stationDetails.getLongitude());
            latLng.setLatitude(stationDetails.getLatitude());
            historicalDataLatLng.add(latLng);
        }
    }
    private void getWeatherDetails() {
        for (StationDetails stationDetails:stationDetails){
            try {
                Log.d("Trying to open",stationDetails.getName()+".csv");
                InputStream index = new FileInputStream(unzipedLocation+stationDetails.getName()+".csv");
                Log.d(unzipedLocation+stationDetails.getName()+".csv","SUCCESS");

                BufferedReader reader=new BufferedReader(
                        new InputStreamReader(index, Charset.forName("UTF-8"))
                );
                reader.readLine();
                String line="";
                List<Double> jan=new ArrayList<>()
                        ,feb=new ArrayList<>()
                        ,mar=new ArrayList<>()
                        ,apr=new ArrayList<>()
                        ,may=new ArrayList<>()
                        ,jun=new ArrayList<>()
                        ,jul=new ArrayList<>()
                        ,aug=new ArrayList<>()
                        ,sep=new ArrayList<>()
                        ,oct=new ArrayList<>()
                        ,nov=new ArrayList<>()
                        , dec=new ArrayList<>();
                int month=1;
                WeatherDetails details=new WeatherDetails();
                while ((line=reader.readLine())!=null){
                    String[] value=line.split(",");
                    jan.add(Double.parseDouble(value[month++]));
                    feb.add(Double.parseDouble(value[month++]));
                    mar.add(Double.parseDouble(value[month++]));
                    apr.add(Double.parseDouble(value[month++]));
                    may.add(Double.parseDouble(value[month++]));
                    jun.add(Double.parseDouble(value[month++]));
                    jul.add(Double.parseDouble(value[month++]));
                    aug.add(Double.parseDouble(value[month++]));
                    sep.add(Double.parseDouble(value[month++]));
                    oct.add(Double.parseDouble(value[month++]));
                    nov.add(Double.parseDouble(value[month++]));
                    dec.add(Double.parseDouble(value[month]));
                    month=1;
                }
                details.setId(stationDetails.getName());
                details.setJan(jan);
                details.setFeb(feb);
                details.setMar(mar);
                details.setApr(apr);
                details.setMay(may);
                details.setJun(jun);
                details.setJul(jul);
                details.setAug(aug);
                details.setSep(sep);
                details.setOct(oct);
                details.setNov(nov);
                details.setDec(dec);
                weatherDetails.add(details);
                Log.d("Jan 2000 at "+stationDetails.getName(),weatherDetails.get(weatherDetails.size()-1).jan+"");
            } catch (Exception e) {
                Log.d(unzipedLocation+stationDetails.getName()+".csv","Failed");
                e.printStackTrace();
            }
        }
    }
    private void getPredictResults(){
        for (StationDetails stationDetails:stationDetails){
            try {
                InputStream index = new FileInputStream(unzipedLocation+stationDetails.getName()+"predict.txt");
                Log.d(unzipedLocation+stationDetails.getName()+"predict.txt","SUCCESS");
                BufferedReader reader=new BufferedReader(
                        new InputStreamReader(index, Charset.forName("UTF-8"))
                );
                try {
                    PredictionResults results = new PredictionResults();
                    String line="";
                    List<Double> predict=new ArrayList<>();
                    int year=0;
                    while ((line=reader.readLine())!=null) {
                        String[] value=line.split(" ");
                        year=Integer.parseInt(value[1]);
                        predict.add(Double.parseDouble(value[2]));
                        Log.d(stationDetails.getName(),value[2]+" "+year);
                    }
                    results.setName(stationDetails.getName());
                    results.setPredictVal(predict);
                    results.setYear(year);
                    predictionResults.add(results);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
