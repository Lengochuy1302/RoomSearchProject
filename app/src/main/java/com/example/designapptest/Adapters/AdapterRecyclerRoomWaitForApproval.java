package com.example.designapptest.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designapptest.ClassOther.classFunctionStatic;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.Views.detailRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterRecyclerRoomWaitForApproval extends RecyclerView.Adapter<AdapterRecyclerRoomWaitForApproval.ViewHolder> {
    List<RoomModel> RoomModelList;
    //Là biến lưu giao diện custom của từng row
    int resource;
    // Linh thêm
    Context context;

    public AdapterRecyclerRoomWaitForApproval(Context context, List<RoomModel> RoomModelList, int resource) {
        this.context = context;
        this.RoomModelList = RoomModelList;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTimeCreated, txtName, txtMaxNumber, txtPrice, txtAddress, txtArea, txtQuantityComment, txtType, txtQuantityViews;
        ImageView imgRoom, imgGender, imgVerified;
        Button btnVerify;
        CardView cardViewRoomList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTimeCreated = (TextView) itemView.findViewById(R.id.txt_timeCreated);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtMaxNumber = (TextView) itemView.findViewById(R.id.txt_quantityMember);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtArea = (TextView) itemView.findViewById(R.id.txt_area);
            txtQuantityComment = (TextView) itemView.findViewById(R.id.txt_quantityComment);
            imgRoom = (ImageView) itemView.findViewById(R.id.img_room);
            imgGender = (ImageView) itemView.findViewById(R.id.img_gender);
            txtType = (TextView) itemView.findViewById(R.id.txt_type);
            txtQuantityViews = (TextView) itemView.findViewById(R.id.txt_quantityViews);
            imgVerified = (ImageView) itemView.findViewById(R.id.img_verified);
            btnVerify = (Button) itemView.findViewById(R.id.btn_verifi);
            cardViewRoomList = (CardView) itemView.findViewById(R.id.cardViewRoomWaitForApprovalList);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerRoomWaitForApproval.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerRoomWaitForApproval.ViewHolder viewHolder, int i) {
        //Lấy giá trị trong list
        final RoomModel roomModel = RoomModelList.get(i);

        //Gán các giá trị vào giao diện
        //classFunctionStatic.showProgress(context, viewHolder.imgRoom);

        // Gán thời gian cho main room
        // Đổi string date to long.
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date;

        try {
            date = format.parse(roomModel.getTimeCreated());
            viewHolder.txtTimeCreated.setText(classFunctionStatic.getTimeAgo(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num = roomModel.getRentalCosts();
        int count = 0;
        int temp = num;
        int digit;
        int so1 = 0, so2 = 0, so3 = 0, so4 = 0;
        while(num > 0)
        {
            num = num / 10;
            count++;
        }
        while(temp > 0)
        {
            digit = temp % 10;
            if (count == 3) {
                so4 = digit;
            }
            if (count == 3) {
                so3 = digit;
            }
            if (count == 2) {
                so2 = digit;
            }
            if (count == 1) {
                so1 = digit;
            }
            temp = temp / 10;
            count--;
        }

        if (roomModel.getRentalCosts() < 100000) {
            viewHolder.txtPrice.setText(so1+""+so2+"k/ phòng ");
        } else if (100000 <= roomModel.getRentalCosts() && roomModel.getRentalCosts() < 1000000) {
            viewHolder.txtPrice.setText(so1+""+so2+""+so3+"k/ phòng ");
        } else if (1000000 <= roomModel.getRentalCosts()) {
            viewHolder.txtPrice.setText(so1 + "."+ so2 +"tr/ phòng ");
        }

        viewHolder.txtName.setText(roomModel.getName());
        viewHolder.txtMaxNumber.setText(String.valueOf((int) roomModel.getMaxNumber()));
        int dientich = (int) Math.round(roomModel.getLength() * roomModel.getWidth());
        viewHolder.txtArea.setText(dientich+ "m²");
        viewHolder.txtQuantityComment.setText("0");
        viewHolder.txtType.setText(roomModel.getRoomType());
        viewHolder.txtQuantityViews.setText(String.valueOf(roomModel.getViews()));

        //Set address for room
        String longAddress = roomModel.getApartmentNumber() + " " + roomModel.getStreet() + ", "
                + roomModel.getWard() + ", " + roomModel.getCounty() + ", " + roomModel.getCity();
        viewHolder.txtAddress.setText(longAddress);
        //End Set address for room

        //Gán hình cho giới tính
        if (roomModel.isGender() == true) {
            viewHolder.imgGender.setImageResource(R.drawable.ic_png_male_100);
        } else {
            viewHolder.imgGender.setImageResource(R.drawable.ic_svg_female_100);
        }
        //End Gán giá trị cho giới tính

        //Gán giá trị cho số lượt bình luận
        if (roomModel.getListCommentRoom().size() > 0) {
            viewHolder.txtQuantityComment.setText(roomModel.getListCommentRoom().size() + "");
        }

        //End gán giá trị cho số lượng bình luận

        //Hiển thị phòng đã được chứng thực

        if (roomModel.isAuthentication()) {
            viewHolder.imgVerified.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgVerified.setVisibility(View.GONE);
        }

        //End hiển thị phòng đã được chúng thực


        //Download ảnh dùng picaso cho đỡ lag, dùng thuộc tính fit() để giảm dung lượng xuống thấp nhất có thể
        //Picasso.get().load(roomModel.getCompressionImage()).fit().centerCrop().into(viewHolder.imgRoom);
        roomModel.getCompressionImageFit().centerCrop().into(viewHolder.imgRoom);
//        Picasso.get().load(roomModel.getCompressionImage()).fit().centerCrop().into(viewHolder.imgRoom);
        //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/findroomforrent-5bea0.appspot.com/o/Images%2Freceived-405711336891277_1555296117.jpg?alt=media&token=c27bd472-7a97-47dc-9f48-706b202929ce").into(viewHolder.imgRoom);


        //Dowload hình ảnh cho room
//        if (roomModel.getListImageRoom().size() > 0) {
//
//            StorageReference storageReference = FirebaseStorage
//                    .getInstance().getReference()
//                    .child("Images")
//                    .child(roomModel.getListImageRoom().get(0));
//
//            final long ONE_MEGABYTE = 1024 * 1024;
//            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    //Tạo ảnh bitmap từ byte
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    viewHolder.imgRoom.setImageBitmap(bitmap);
//                    Log.d("check 9", "onSuccess: ");
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//
//        }
        //End Dowload hình ảnh cho room

        // Đăng kí sự kiện click cho cardView // Linh thêm
        viewHolder.cardViewRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDetailRoom = new Intent(context, detailRoom.class);
                iDetailRoom.putExtra("phongtro", roomModel);
                context.startActivity(iDetailRoom);
            }
        });

        viewHolder.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(RoomModelList.get(i).getRoomID(),i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RoomModelList.size();
    }

    private void showDialog(String RoomID,int position){
        Dialog verifiDialog = new Dialog(context);
        verifiDialog.setContentView(R.layout.verify_dialog);

        ImageView imgClose = verifiDialog.findViewById(R.id.img_close);
        Button btnVefiry =verifiDialog.findViewById(R.id.btn_verifi);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifiDialog.dismiss();
            }
        });

        btnVefiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove khỏi list
                //Gọi hàm xóa từ model
                RoomModel modelFunction = new RoomModel();
                modelFunction.verifyRoom(RoomModelList.get(position).getRoomID());
                verifiDialog.dismiss();
                RoomModelList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context,"Duyệt thành công",Toast.LENGTH_SHORT).show();
            }
        });

        verifiDialog.show();
    }
}
