package com.example.designapptest.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.designapptest.ClassOther.classFunctionStatic;
import com.example.designapptest.Controller.Interfaces.IMainRoomModel;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.Views.detailRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterRecyclerMainRoom extends RecyclerView.Adapter<AdapterRecyclerMainRoom.ViewHolder> {

    List<RoomModel> RoomModelList;
    //Là biến lưu giao diện custom của từng row
    int resource;
    // Linh thêm
    Context context;
    String UID;

    public AdapterRecyclerMainRoom(Context context, List<RoomModel> RoomModelList, int resource, String UID) {
        this.context = context;
        this.RoomModelList = RoomModelList;
        this.resource = resource;
        this.UID = UID;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTimeCreated, txtName, txtMaxNumber, txtPrice, txtAddress, txtArea, txtQuantityComment, txtType, txtQuantityViews;
        ImageView imgRoom, imgGender, imgVerified;
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
            cardViewRoomList = (CardView) itemView.findViewById(R.id.cardViewRoomList);
            txtQuantityViews = (TextView) itemView.findViewById(R.id.txt_quantityViews);
            imgVerified = (ImageView) itemView.findViewById(R.id.img_verified);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerMainRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerMainRoom.ViewHolder viewHolder, int i) {
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

        viewHolder.txtName.setText(roomModel.getName());
        viewHolder.txtMaxNumber.setText(String.valueOf((int) roomModel.getMaxNumber()));

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
        if (roomModel.isGender()) {
            viewHolder.imgGender.setImageResource(R.drawable.ic_svg_male_100);
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
        roomModel.getCompressionImageFit().centerCrop().into(viewHolder.imgRoom);

        // Đăng kí sự kiện click cho cardView
        viewHolder.cardViewRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDetailRoom = new Intent(context, detailRoom.class);
                iDetailRoom.putExtra("phongtro", roomModel);
                context.startActivity(iDetailRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RoomModelList.size();
    }

    public void removeItem(RecyclerView.ViewHolder viewHolder, RecyclerView recyclerView,
                           AdapterRecyclerMainRoom adapterRecyclerFavoriteRoom, TextView txtQuantity,
                           IMainRoomModel iMainRoomModel, RoomModel roomModel, int quantityLoaded, int quantityEachTime) {
        int removedPosition = viewHolder.getAdapterPosition();
        removeFavoriteRoom(removedPosition, recyclerView, adapterRecyclerFavoriteRoom, txtQuantity,
                iMainRoomModel, roomModel, quantityLoaded, quantityEachTime);
    }

    private void removeFavoriteRoom(int removedPosition, RecyclerView recyclerView,
                                    AdapterRecyclerMainRoom adapterRecyclerFavoriteRoom, TextView txtQuantity,
                                    IMainRoomModel iMainRoomModel, RoomModel roomModel, int quantityLoaded, int quantityEachTime) {
        DatabaseReference nodeFavoriteRooms = FirebaseDatabase.getInstance().getReference()
                .child("FavoriteRooms");
        String roomId = RoomModelList.get(removedPosition).getRoomID();
        String roomName = RoomModelList.get(removedPosition).getName();

        nodeFavoriteRooms.child(UID).child(roomId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                RoomModel deletedRoomModel = RoomModelList.get(removedPosition);

                // xóa khỏi list
                RoomModelList.remove(removedPosition);
                adapterRecyclerFavoriteRoom.notifyItemRemoved(removedPosition);

                // Cập nhật số lượng ở view
                int roomQuantity = Integer.valueOf(txtQuantity.getText().toString().trim()) - 1;
                iMainRoomModel.setQuantityTop(roomQuantity);

                // Load more một lượng rooms nữa
                roomModel.getPartSpecialListRoom(iMainRoomModel,
                        quantityLoaded + 2*quantityEachTime, quantityLoaded + quantityEachTime);
                // Cập nhật lại số lượng đã tải cho controller biết có thay đổi
                iMainRoomModel.setQuantityLoadMore(quantityLoaded + quantityEachTime);

                Snackbar.make(recyclerView, "Đã xóa " + roomName, Snackbar.LENGTH_LONG).setAction("HOÀN TÁC", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String date = df.format(Calendar.getInstance().getTime());

                        nodeFavoriteRooms.child(UID).child(roomId).child("time").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Thêm vào list
                                    RoomModelList.add(removedPosition, deletedRoomModel);

                                    // Load ảnh nén
                                    deletedRoomModel.setCompressionImageFit(Picasso.get().load(deletedRoomModel.getCompressionImage()).fit());

                                    adapterRecyclerFavoriteRoom.notifyItemInserted(removedPosition);

                                    // Cập nhật số lượng ở view
                                    int roomQuantity = Integer.valueOf(txtQuantity.getText().toString().trim()) + 1;
                                    iMainRoomModel.setQuantityTop(roomQuantity);

//                                    recyclerView.scrollToPosition(removedPosition);
                                }
                            }
                        });
                    }
                }).show();
            }
        });
    }
}
