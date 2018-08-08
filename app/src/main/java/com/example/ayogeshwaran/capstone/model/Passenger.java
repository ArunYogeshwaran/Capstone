package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Passenger implements Parcelable
{

    @SerializedName("booking_status")
    @Expose
    private String bookingStatus;
    @SerializedName("no")
    @Expose
    private Integer no;
    @SerializedName("current_status")
    @Expose
    private String currentStatus;
    public final static Parcelable.Creator<Passenger> CREATOR = new Creator<Passenger>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Passenger createFromParcel(Parcel in) {
            return new Passenger(in);
        }

        public Passenger[] newArray(int size) {
            return (new Passenger[size]);
        }

    }
            ;

    Passenger(Parcel in) {
        this.bookingStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.no = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.currentStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Passenger() {
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bookingStatus);
        dest.writeValue(no);
        dest.writeValue(currentStatus);
    }

    public int describeContents() {
        return 0;
    }

}