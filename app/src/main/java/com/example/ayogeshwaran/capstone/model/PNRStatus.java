package com.example.ayogeshwaran.capstone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PNRStatus implements Parcelable
{

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("boarding_point")
    @Expose
    private BoardingPoint boardingPoint;
    @SerializedName("doj")
    @Expose
    private String doj;
    @SerializedName("to_station")
    @Expose
    private ToStation toStation;
    @SerializedName("reservation_upto")
    @Expose
    private ReservationUpto reservationUpto;
    @SerializedName("debit")
    @Expose
    private Integer debit;
    @SerializedName("total_passengers")
    @Expose
    private Integer totalPassengers;
    @SerializedName("chart_prepared")
    @Expose
    private Boolean chartPrepared;
    @SerializedName("pnr")
    @Expose
    private String pnr;
    @SerializedName("passengers")
    @Expose
    private List<Passenger> passengers = new ArrayList<>();
    @SerializedName("journey_class")
    @Expose
    private JourneyClass journeyClass;
    @SerializedName("train")
    @Expose
    private Train train;
    @SerializedName("from_station")
    @Expose
    private FromStation fromStation;
    public final static Parcelable.Creator<PNRStatus> CREATOR = new Creator<PNRStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PNRStatus createFromParcel(Parcel in) {
            return new PNRStatus(in);
        }

        public PNRStatus[] newArray(int size) {
            return (new PNRStatus[size]);
        }

    }
            ;

    private PNRStatus(Parcel in) {
        this.responseCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.boardingPoint = ((BoardingPoint) in.readValue((BoardingPoint.class.getClassLoader())));
        this.doj = ((String) in.readValue((String.class.getClassLoader())));
        this.toStation = ((ToStation) in.readValue((ToStation.class.getClassLoader())));
        this.reservationUpto = ((ReservationUpto) in.readValue((ReservationUpto.class.getClassLoader())));
        this.debit = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPassengers = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.chartPrepared = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.pnr = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.passengers, (Passenger.class.getClassLoader()));
        this.journeyClass = ((JourneyClass) in.readValue((JourneyClass.class.getClassLoader())));
        this.train = ((Train) in.readValue((Train.class.getClassLoader())));
        this.fromStation = ((FromStation) in.readValue((FromStation.class.getClassLoader())));
    }

    public PNRStatus() {
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public BoardingPoint getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(BoardingPoint boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public ToStation getToStation() {
        return toStation;
    }

    public void setToStation(ToStation toStation) {
        this.toStation = toStation;
    }

    public ReservationUpto getReservationUpto() {
        return reservationUpto;
    }

    public void setReservationUpto(ReservationUpto reservationUpto) {
        this.reservationUpto = reservationUpto;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Integer getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(Integer totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public Boolean getChartPrepared() {
        return chartPrepared;
    }

    public void setChartPrepared(Boolean chartPrepared) {
        this.chartPrepared = chartPrepared;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public JourneyClass getJourneyClass() {
        return journeyClass;
    }

    public void setJourneyClass(JourneyClass journeyClass) {
        this.journeyClass = journeyClass;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public FromStation getFromStation() {
        return fromStation;
    }

    public void setFromStation(FromStation fromStation) {
        this.fromStation = fromStation;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(responseCode);
        dest.writeValue(boardingPoint);
        dest.writeValue(doj);
        dest.writeValue(toStation);
        dest.writeValue(reservationUpto);
        dest.writeValue(debit);
        dest.writeValue(totalPassengers);
        dest.writeValue(chartPrepared);
        dest.writeValue(pnr);
        dest.writeList(passengers);
        dest.writeValue(journeyClass);
        dest.writeValue(train);
        dest.writeValue(fromStation);
    }

    public int describeContents() {
        return 0;
    }

}