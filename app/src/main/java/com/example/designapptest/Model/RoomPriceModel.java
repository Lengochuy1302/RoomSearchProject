package com.example.designapptest.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomPriceModel implements Parcelable {
    String name;
    String roomPriceID;
    int price;
    String imageName;

    protected RoomPriceModel(Parcel in) {
        name = in.readString();
        roomPriceID = in.readString();
        price = in.readInt();
        imageName = in.readString();
    }

    public static final Creator<RoomPriceModel> CREATOR = new Creator<RoomPriceModel>() {
        @Override
        public RoomPriceModel createFromParcel(Parcel in) {
            return new RoomPriceModel(in);
        }

        @Override
        public RoomPriceModel[] newArray(int size) {
            return new RoomPriceModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomPriceID() {
        return roomPriceID;
    }

    public void setRoomPriceID(String roomPriceID) {
        this.roomPriceID = roomPriceID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public RoomPriceModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(roomPriceID);
        dest.writeInt(price);
        dest.writeString(imageName);
    }
}
