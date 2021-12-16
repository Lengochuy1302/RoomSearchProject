package com.example.designapptest.Controller;

import android.content.Context;

import com.example.designapptest.Controller.Interfaces.IPostRoomUpdateModel;
import com.example.designapptest.Controller.Interfaces.IUpdateRoomModel;
import com.example.designapptest.Model.RoomModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PostRoomUpdateController {
    Context context;
    RoomModel roomModel;

    public PostRoomUpdateController(Context context) {
        this.context = context;
        roomModel = new RoomModel();
    }

    public void postRoomStep1Update(String roomID, String city, String district, String ward, String street, String no, double longtitude, double latitude,
                                    String oldCity, String oldDistrict, String oldWard, String oldStreet, String oldNo, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thành công!")
                        .setContentText("Phòng trọ đã được update hãy đợi duyệt!")
                        .show();

               // postRoomAdapterUpdate.setOnPostRoomStep1Update(city, district, ward, street, no, longtitude, latitude);
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }
        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep1Update(roomID, city, district, ward, street, no, iPostRoomUpdateModel, longtitude, latitude,
                oldCity, oldDistrict, oldWard, oldStreet, oldNo);
    }

    public void postRoomStep2Update(String roomId, String typeId, boolean genderRoom, long currentNumber, long maxNumber, float width, float length,
                                    int priceRoom, int electricBill, int warterBill, int InternetBill, int parkingBill, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {

            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thành công!")
                        .setContentText("Phòng trọ đã được update hãy đợi duyệt!")
                        .show();

//                postRoomAdapterUpdate.setOnPostRoomStep2Update( typeId,  genderRoom,  currentNumber,
//                 maxNumber,  width,  length,  priceRoom,
//                 electricBill,  warterBill,  InternetBill,  parkingBill);

            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }

        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep2Update(roomId, typeId, genderRoom, maxNumber, width, length,
                priceRoom, electricBill, warterBill, InternetBill, parkingBill, iPostRoomUpdateModel);
    }

    public void postRoomStep3Update(String roomId, String owner,  List<String> listConvenient, List<String> listPathImageChoosed,
                                    boolean isChangeConvenient, boolean isChangeImageRoom, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thành công!")
                        .setContentText("Phòng trọ đã được update hãy đợi duyệt!")
                        .show();
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {

            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }

        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep3Update(roomId, owner, listConvenient, listPathImageChoosed,
                isChangeConvenient, isChangeImageRoom, iPostRoomUpdateModel, context);
    }

    public void postRoomStep4Update(String roomId, String name, String describe, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thành công!")
                        .setContentText("Phòng trọ đã được update hãy đợi duyệt!")
                        .show();
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }
        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep4Update(roomId, name, describe, iPostRoomUpdateModel);
    }
}
