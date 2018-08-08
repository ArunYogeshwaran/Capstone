package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day implements Parcelable
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("runs")
    @Expose
    private String runs;
    public final static Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return (new Day[size]);
        }

    }
            ;

    Day(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.runs = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Day() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(runs);
    }

    public int describeContents() {
        return 0;
    }

}