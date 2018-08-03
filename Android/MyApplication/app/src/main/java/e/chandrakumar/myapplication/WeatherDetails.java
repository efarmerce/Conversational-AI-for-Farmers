package e.chandrakumar.myapplication;

import java.io.Serializable;
import java.util.List;

public class WeatherDetails implements Serializable {
    List<Double> jan;
    List<Double> feb;
    List<Double> mar;
    List<Double> apr;
    List<Double> may;

    public List<Double> getMay() {
        return may;
    }

    public void setMay(List<Double> may) {
        this.may = may;
    }

    List<Double> jun;
    List<Double> jul;
    List<Double> aug;
    List<Double> sep;
    List<Double> oct;
    List<Double> nov;
    List<Double> dec;
    String id;

    public List<Double> getJan() {
        return jan;
    }

    public void setJan(List<Double> jan) {
        this.jan = jan;
    }

    public List<Double> getFeb() {
        return feb;
    }

    public void setFeb(List<Double> feb) {
        this.feb = feb;
    }

    public List<Double> getMar() {
        return mar;
    }

    public void setMar(List<Double> mar) {
        this.mar = mar;
    }

    public List<Double> getApr() {
        return apr;
    }

    public void setApr(List<Double> apr) {
        this.apr = apr;
    }

    public List<Double> getJun() {
        return jun;
    }

    public void setJun(List<Double> jun) {
        this.jun = jun;
    }

    public List<Double> getJul() {
        return jul;
    }

    public void setJul(List<Double> jul) {
        this.jul = jul;
    }

    public List<Double> getAug() {
        return aug;
    }

    public void setAug(List<Double> aug) {
        this.aug = aug;
    }

    public List<Double> getSep() {
        return sep;
    }

    public void setSep(List<Double> sep) {
        this.sep = sep;
    }

    public List<Double> getOct() {
        return oct;
    }

    public void setOct(List<Double> oct) {
        this.oct = oct;
    }

    public List<Double> getNov() {
        return nov;
    }

    public void setNov(List<Double> nov) {
        this.nov = nov;
    }

    public List<Double> getDec() {
        return dec;
    }

    public void setDec(List<Double> dec) {
        this.dec = dec;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
