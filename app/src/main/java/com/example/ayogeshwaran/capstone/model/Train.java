package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Train implements Parcelable
{

    @SerializedName("classes")
    @Expose
    private List<Class> classes = new ArrayList<>();
    @SerializedName("days")
    @Expose
    private List<Day> days = new ArrayList<>();
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<Train> CREATOR = new Creator<Train>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Train createFromParcel(Parcel in) {
            return new Train(in);
        }

        public Train[] newArray(int size) {
            return (new Train[size]);
        }

    }
            ;

    Train(Parcel in) {
        in.readList(this.classes, (Class.class.getClassLoader()));
        in.readList(this.days, (Day.class.getClassLoader()));
        this.number = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Train() {
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(classes);
        dest.writeList(days);
        dest.writeValue(number);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}



