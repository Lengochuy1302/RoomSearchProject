package com.example.designapptest.Views;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.designapptest.Adapters.AdapterRecyclerImageUpload;
import com.example.designapptest.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class postRoomStep3 extends Fragment implements View.OnClickListener{

    //Biến filnal lưu tên fragment
    public static final String FRAG_NAME = "POST_ROOM_STEP_3";
    //End biến filnal lưu tên fragment

    //Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    //End Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager

    //Biến final share dữ liệu
    public final static String SHARE_LIST_CONVENIENT = "LIST_CONVENIENT";
    public final static String SHARE_LISTPATHIMAGE = "LISTPATHIMGAE";
    //End biến filnal để share dữ liệu

    CheckBox chBoxWc,chBoxWifi,chBoxClock,chBoxBed,chBoxFridge,chBoxWardrobe,
    chBoxPet,chBoxWindow,chBoxParking,chBoxWashmachine,chBoxWaterheater,chBoxSecurity,chBoxArcondition, gac;

    public static final int RQ_LOAD_IMAGE=10;
    public static final int RQ_IMAGE_CAPTURE = 11;

    ImageButton btnImgUpLoadPushRoom;
    RecyclerView recyclerImgUpload;
    Button btnNextStep3PostRoom;

    //Bien global
    List<String> listConvenient;
    List<String> listPathImageChoosed;
    //End bien global

    AdapterRecyclerImageUpload adapterRecyclerImageUpload;

    //Activity chứa viewpager
    postRoomAdapter postRoom;

    public postRoomStep3(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_room_step_3_view, container, false);
        //khoi tao view
        initControl(view);
        initData();
        Checkbox();
        //Lấy ra activity hiện tại
        postRoom = (postRoomAdapter) getContext();
        return view;
    }

    private void initData(){
        listPathImageChoosed = new ArrayList<String>();
        adapterRecyclerImageUpload = new AdapterRecyclerImageUpload(getContext(),R.layout.upload_image_element_recycler_view,listPathImageChoosed);
        recyclerImgUpload.setAdapter(adapterRecyclerImageUpload);
    }

    private void initControl(View view){
        chBoxWc = view.findViewById(R.id.chBox_wc);
        chBoxWifi = view.findViewById(R.id.chBox_wifi);
        chBoxClock = view.findViewById(R.id.chBox_clock);
        chBoxBed = view.findViewById(R.id.chBox_bed);
        chBoxFridge = view.findViewById(R.id.chBox_fridge);
        chBoxWardrobe = view.findViewById(R.id.chBox_wardrobe);
        chBoxPet = view.findViewById(R.id.chBox_pet);
        chBoxWindow = view.findViewById(R.id.chBox_window);
        chBoxParking = view.findViewById(R.id.chBox_parking);
        chBoxWashmachine = view.findViewById(R.id.chBox_washmachine);
        chBoxWaterheater = view.findViewById(R.id.chBox_waterheater);
        chBoxSecurity = view.findViewById(R.id.chBox_security);
        chBoxArcondition = view.findViewById(R.id.chBox_arcondition);
        gac = view.findViewById(R.id.chBox_gac);
        btnNextStep3PostRoom = view.findViewById(R.id.btn_nextStep3_post_room);
        btnNextStep3PostRoom.setOnClickListener(this);

        btnImgUpLoadPushRoom = view.findViewById(R.id.btnImg_upLoad_push_room);
        btnImgUpLoadPushRoom.setOnClickListener(this);


        recyclerImgUpload = view.findViewById(R.id.recycler_img_upload);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
       // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerImgUpload.setLayoutManager(layoutManager);
    }

    private void Checkbox(){
        //Them vao theo ma neu nhu check dung
        chBoxWc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWc.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWc.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        gac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    gac.setTextColor(Color.rgb(0,133,119));

                } else {
                    gac.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        chBoxArcondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxArcondition.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxArcondition.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        chBoxSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxSecurity.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxSecurity.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        chBoxWaterheater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWaterheater.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWaterheater.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        chBoxWashmachine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWashmachine.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWashmachine.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxParking.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxParking.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWindow.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWindow.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxPet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxPet.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxPet.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxWardrobe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWardrobe.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWardrobe.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxFridge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxFridge.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxFridge.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxBed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxBed.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxBed.setTextColor(Color.rgb(102,102,102));
                }

            }
        });


        chBoxClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxClock.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxClock.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

        chBoxWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Code khi trạng thái check thay đổi
                if (b == true) {
                    chBoxWifi.setTextColor(Color.rgb(0,133,119));

                } else {
                    chBoxWifi.setTextColor(Color.rgb(102,102,102));
                }

            }
        });

    }

    //Theem cac id tien ich vao trong list tien ich
    private void loadConvenientFromCheckbox(){
        //Tao moi list
        listConvenient = new ArrayList<String>();
        //Them vao theo ma neu nhu check dung
        if(chBoxWc.isChecked()==true){
            //Wc rieng
            listConvenient.add("IDC1");

        }
        if(chBoxWifi.isChecked()==true){
            //co wifi
            listConvenient.add("IDC3");
        }
        if(chBoxClock.isChecked()==true){
            //tu do
            listConvenient.add("IDC4");
        }
        if(chBoxBed.isChecked()==true){
            //giuong ngu
            listConvenient.add("IDC9");
        }
        if(chBoxFridge.isChecked()==true){
            //Tu lanh
            listConvenient.add("IDC6");
        }
        if(chBoxWardrobe.isChecked()==true){
            //Tu quan cao
            listConvenient.add("IDC10");
        }
        if(chBoxPet.isChecked()==true){
            //cho phep co thu cung
            listConvenient.add("IDC5");
        }
        if(chBoxWindow.isChecked()==true){
            //co cua so
            listConvenient.add("IDC11");
        }
        if(chBoxParking.isChecked()==true){
            //Cho de xe
            listConvenient.add("IDC2");
        }
        if(chBoxWashmachine.isChecked()==true){
            //co may giat
            listConvenient.add("IDC7");
        }
        if(chBoxWaterheater.isChecked()==true){
            //co may nuoc nong
            listConvenient.add("IDC12");
        }
        if(chBoxSecurity.isChecked()==true){
            //an ninh
            listConvenient.add("IDC8");
        }
        if(chBoxArcondition.isChecked()==true){
            //may lanh
            listConvenient.add("IDC0");
        }

        if(gac.isChecked()==true){
            //gac lung
            listConvenient.add("IDC13");
        }
    }

    //ham kiem tra du lieu nhap vao tu control
    private boolean checkTrueDataFromControl(){
        return true;
    }

    //Hàm đổ dữ liệu vào trong recyclerview
    private void loadImageForRecyclerView(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQ_LOAD_IMAGE){
            if(resultCode==RESULT_OK &&data!=null){

                ClipData clipData = data.getClipData();
                if(clipData!=null){
                    for (int i = 0;i<clipData.getItemCount();i++){
                        String pathImage = clipData.getItemAt(i).getUri().toString();
                        listPathImageChoosed.add(pathImage);
                        //Thông báo thay đổi dữ liệu
                        adapterRecyclerImageUpload.notifyDataSetChanged();
                    }
                }
                else {
                    Uri uri = data.getData();
                    if(uri!=null){
                        listPathImageChoosed.add(uri.toString());
                        //Thông báo thay đổi dữ liệu
                        adapterRecyclerImageUpload.notifyDataSetChanged();
                    }
                }
            }
        }
        else if(requestCode == RQ_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK && data!=null){
                Uri uri = data.getData();
                if(uri !=null){
                    listPathImageChoosed.add(uri.toString());
                    //Thông báo thay đổi dữ liệu
                    adapterRecyclerImageUpload.notifyDataSetChanged();
                }
            }
        }
    }

    //Hàm chuyển màu ở activity
    private void changeColorInActivity(boolean isComplete) {

        //Gọi hàm trong posrRoom Thông qua Activity
        postRoom.onMsgFromFragToPostRoom(FRAG_NAME, isComplete);
    }


    //Hàm lưu share dữ liệu giữa các fragment
    private void saveDataToPreference(){
        sharedpreferences = this.getActivity().getSharedPreferences(postRoomAdapter.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        Set<String> setConvenient = new HashSet<String>();
        setConvenient.addAll(listConvenient);
        editor.putStringSet(SHARE_LIST_CONVENIENT,setConvenient);

        Set<String> setPathImage = new HashSet<String>();
        setPathImage.addAll(listPathImageChoosed);
        editor.putStringSet(SHARE_LISTPATHIMAGE,setPathImage);

        editor.commit();
    }


    //Truy cập vô camera trong máy
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, RQ_IMAGE_CAPTURE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnImg_upLoad_push_room:

                //Load ảnh lên bằng galory
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Chọn ảnh"),RQ_LOAD_IMAGE);
                break;

            case R.id.btn_nextStep3_post_room:
                if (checkTrueDataFromControl() == true) {
                    loadConvenientFromCheckbox();
                    if (listConvenient.size() > 0 && listPathImageChoosed.size() >= 4) {
                        //Lưu dữ liệu truyền qua các fragment
                        Log.d("check2", listPathImageChoosed.size()+"");
                        saveDataToPreference();
                        //Đổi màu
                        changeColorInActivity(true);
                        //Chuyển sang page kế tiếp
                        postRoom.setCurrentPage(3);
                    }else {
                        Toast.makeText(getContext(),"Vui lòng chọn đủ ảnh và tiện ích",Toast.LENGTH_LONG).show();
                    }
                }

                break;

        }
    }
}
