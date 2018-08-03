package e.chandrakumar.myapplication;

import java.io.Serializable;
import java.util.List;

public class PredictionResults implements Serializable{
    String name;
    List<Double> predictVal;
    int year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getPredictVal() {
        return predictVal;
    }

    public void setPredictVal(List<Double> predictVal) {
        this.predictVal = predictVal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
