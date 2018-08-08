package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JourneyClass implements Parcelable
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private Object name;
    public final static Parcelable.Creator<JourneyClass> CREATOR = new Creator<JourneyClass>() {


        @SuppressWarnings({
                "unchecked"
        })
        public JourneyClass createFromParcel(Parcel in) {
            return new JourneyClass(in);
        }

        public JourneyClass[] newArray(int size) {
            return (new JourneyClass[size]);
        }

    }
            ;

    JourneyClass(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public JourneyClass() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}