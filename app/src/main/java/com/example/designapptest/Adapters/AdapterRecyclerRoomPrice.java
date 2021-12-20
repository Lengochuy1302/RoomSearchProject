package com.example.designapptest.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.designapptest.Model.RoomPriceModel;
import com.example.designapptest.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterRecyclerRoomPrice extends RecyclerView.Adapter<AdapterRecyclerRoomPrice.ViewHolder> {
    Context context;
    Context contextMain;
    int layout;
    List<RoomPriceModel> PriceModelList;
    final NumberFormat fmd = new DecimalFormat("#,###");
    public AdapterRecyclerRoomPrice(Context context, Context contextMain, int layout, List<RoomPriceModel> PriceModelList) {
        this.context = context;
        this.contextMain = contextMain;
        this.layout = layout;
        this.PriceModelList = PriceModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPriceRoomDetail;
        TextView txtNamePriceRoomDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPriceRoomDetail = (ImageView) itemView.findViewById(R.id.img_price_room_detail);
            txtNamePriceRoomDetail = (TextView) itemView.findViewById(R.id.txt_namePrice_price_room_detail);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerRoomPrice.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerRoomPrice.ViewHolder viewHolder, int i) {
        final RoomPriceModel roomPriceModel = PriceModelList.get(i);

        //Gán các giá trị vào giao diện
        int price = roomPriceModel.getPrice();
        if (price == 0) {
            viewHolder.txtNamePriceRoomDetail.setText("Miễn phí");
        } else {
            if (roomPriceModel.getRoomPriceID().equals("IDRPT0")) {
                viewHolder.txtNamePriceRoomDetail.setText(fmd.format(price) + "đ/ m³");
            } else  if (roomPriceModel.getRoomPriceID().equals("IDRPT1")) {
                viewHolder.txtNamePriceRoomDetail.setText(fmd.format(price) + "đ/ tháng");
            } else  if (roomPriceModel.getRoomPriceID().equals("IDRPT3")) {
                viewHolder.txtNamePriceRoomDetail.setText(fmd.format(price) + "đ/ kg");
            }  else  if (roomPriceModel.getRoomPriceID().equals("IDRPT2")) {
                viewHolder.txtNamePriceRoomDetail.setText(fmd.format(price) + "đ/ tháng");
            } else {
                viewHolder.txtNamePriceRoomDetail.setText(fmd.format(price) + "đ");
            }

        }


        int resourceId = context.getResources().getIdentifier(roomPriceModel.getImageName(), "drawable", contextMain.getPackageName());
        viewHolder.imgPriceRoomDetail.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return PriceModelList.size();
    }
}
