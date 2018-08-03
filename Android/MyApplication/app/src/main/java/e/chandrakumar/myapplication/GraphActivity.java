package e.chandrakumar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        StationDetails details=(StationDetails)getIntent().getSerializableExtra("station");
        WeatherDetails weatherDetails=(WeatherDetails)getIntent().getSerializableExtra("weather");
        PredictionResults predictionResults=(PredictionResults)getIntent().getSerializableExtra("prediction");
        this.setTitle(details.getName()+" Rainfall");
        GraphView janGraph=findViewById(R.id.jan);
        GraphView febGraph=findViewById(R.id.feb);
        GraphView marGraph=findViewById(R.id.mar);
        GraphView aprGraph=findViewById(R.id.apr);
        GraphView mayGraph=findViewById(R.id.may);
        GraphView junGraph=findViewById(R.id.jun);
        GraphView julGraph=findViewById(R.id.jul);
        GraphView augGraph=findViewById(R.id.aug);
        GraphView sepGraph=findViewById(R.id.sep);
        GraphView octGraph=findViewById(R.id.oct);
        GraphView novGraph=findViewById(R.id.nov);
        GraphView decGraph=findViewById(R.id.dec);
        GraphView[] graphViews=new GraphView[]{janGraph,febGraph,marGraph,aprGraph,mayGraph,junGraph,julGraph,augGraph,sepGraph,octGraph,novGraph,decGraph};
        List<DataPoint> jan=new ArrayList<>()
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
        int month=predictionResults.getYear()-weatherDetails.getApr().size();
        Log.d("Total months",weatherDetails.getApr().size()+"");
        List<List<DataPoint>> historicMonthlyList=new ArrayList<List<DataPoint>>();
        for (Double val:weatherDetails.getJan()){
            jan.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(jan);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getFeb()){
            feb.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(feb);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getMar()){
            mar.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(mar);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getApr()){
            apr.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(apr);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getMay()) {
            may.add(new DataPoint((month++)%100, val));
        }
        historicMonthlyList.add(may);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getJun()){
            jun.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(jun);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getJul()){
            jul.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(jul);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getAug()){
            aug.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(aug);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getSep()){
            sep.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(sep);
        month=predictionResults.getYear()-weatherDetails.getApr().size();
        for (Double val:weatherDetails.getOct()){
            Log.d("Months are",month+" and the value "+val);
            oct.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(oct);
        month=predictionResults.getYear()-weatherDetails.getApr().size();

        for (Double val:weatherDetails.getNov()){
            nov.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(nov);
        month=predictionResults.getYear()-weatherDetails.getApr().size();
        for (Double val:weatherDetails.getDec()){
            dec.add(new DataPoint((month++)%100,val));
        }
        historicMonthlyList.add(dec);
        for (int i=0;i<historicMonthlyList.size();i++)
        {
            DataPoint[] points=new DataPoint[historicMonthlyList.get(i).size()];
            for (int j=0;j<historicMonthlyList.get(i).size();j++){
                points[j]=historicMonthlyList.get(i).get(j);
            }
            LineGraphSeries<DataPoint> series=new LineGraphSeries<>(points);
            graphViews[i].addSeries(series);
            graphViews[i].setHorizontalScrollBarEnabled(true);
            graphViews[i].getViewport().setScalable(true);
            graphViews[i].getViewport().setScrollable(true);
            graphViews[i].getViewport().setScalableY(true);
            graphViews[i].getViewport().setScrollableY(true);
            GridLabelRenderer gridLabel = graphViews[i].getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Year");
            gridLabel.setVerticalAxisTitle("Rainfall in mm");
            graphViews[i].computeScroll();
        }

        int pred_year = predictionResults.getYear();
        for (Double val:predictionResults.getPredictVal()){
            Log.d("Val is ",String.valueOf(val));
        }
        Log.d("Val is ",predictionResults.getPredictVal().size()+"");
        Double pred_val = predictionResults.getPredictVal().get(0);
        TextView jan_txt = (TextView)findViewById(R.id.jan_txt);
        jan_txt.setText("JAN"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(1);
        TextView feb_txt = (TextView)findViewById(R.id.feb_txt);
        feb_txt.setText("FEB"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(2);
        TextView mar_txt = (TextView)findViewById(R.id.mar_txt);
        mar_txt.setText("MAR "+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(3);
        TextView apr_txt = (TextView)findViewById(R.id.apr_txt);
        apr_txt.setText("APR"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(4);
        TextView may_txt = (TextView)findViewById(R.id.may_txt);
        may_txt.setText("MAY"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(5);
        TextView jun_txt = (TextView)findViewById(R.id.jun_txt);
        jun_txt.setText("JUN"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(6);
        TextView jul_txt = (TextView)findViewById(R.id.jul_txt);
        jul_txt.setText("JUL"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(7);
        TextView aug_txt = (TextView)findViewById(R.id.aug_txt);
        aug_txt.setText("AUG"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(8);
        TextView sep_txt = (TextView)findViewById(R.id.sep_txt);
        sep_txt.setText("SEP"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(9);
        TextView oct_txt = (TextView)findViewById(R.id.oct_txt);
        oct_txt.setText("OCT"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(10);
        TextView nov_txt = (TextView)findViewById(R.id.nov_txt);
        nov_txt.setText("NOV"+pred_year+" predicted value is "+String.format("%.2f",pred_val));

        pred_val = predictionResults.getPredictVal().get(11);
        TextView dec_txt = (TextView)findViewById(R.id.dec_txt);
        dec_txt.setText("DEC"+pred_year+" predicted value is "+String.format("%.2f",pred_val));
    }
}
