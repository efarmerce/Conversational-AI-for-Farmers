package e.chandrakumar.myapplication;

class LatLng {
    double latitude;
    double longitude;

    public LatLng(double v, double v1) {
        latitude=v;
        longitude=v1;
    }

    public LatLng() {

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
