package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToStation implements Parcelable
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<ToStation> CREATOR = new Creator<ToStation>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ToStation createFromParcel(Parcel in) {
            return new ToStation(in);
        }

        public ToStation[] newArray(int size) {
            return (new ToStation[size]);
        }

    }
            ;

    ToStation(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.lng = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ToStation() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(lng);
        dest.writeValue(lat);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}