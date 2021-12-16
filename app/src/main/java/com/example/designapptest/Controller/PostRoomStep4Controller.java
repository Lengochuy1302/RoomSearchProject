package com.example.designapptest.Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.designapptest.Controller.Interfaces.ICallBackFromAddRoom;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.Views.roomManagementModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PostRoomStep4Controller {
    RoomModel roomModel;
    Context context;

    public PostRoomStep4Controller(Context context){
        this.roomModel = new RoomModel();
        this.context=context;
    }

    public void callAddRoomFromModel(String UID, RoomModel dataRoom, List<String> listConvenient, List<String> listPathImg,
                                     int electricBill, int warterBill, int InternetBill, int parkingBill, Dialog progressDialog){

        ICallBackFromAddRoom iCallBackFromAddRoom = new ICallBackFromAddRoom() {
            @Override
            public void stopProgess(boolean isSuccess) {
                if(isSuccess){
                    //Stop progess
                    progressDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Thành công!")
                            .setContentText("Thêm thành công hãy đợi duyệt!")
                            .show();

                }else {
                    //do nothing
                }
            }
        };

        //Gọi hàm thêm phòng ở model
        roomModel.addRoom(UID, dataRoom,listConvenient,listPathImg,electricBill,warterBill,InternetBill,parkingBill,iCallBackFromAddRoom,context);
    }
}
