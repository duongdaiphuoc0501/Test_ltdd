package com.example.danhba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DanhBaAdapter extends RecyclerView.Adapter<DanhBaAdapter.DanhBaViewHolder> {
    private ArrayList<ConTact> list_ConTacts;
    private BackToInf BackInf;
    private Case_Interface case_interface2;

    public DanhBaAdapter(ArrayList<ConTact> list_ConTacts, BackToInf backInf, Case_Interface case_interface2) {
        this.list_ConTacts = list_ConTacts;
        BackInf = backInf;
        this.case_interface2 = case_interface2;
    }

    @NonNull
    @Override
    public DanhBaAdapter.DanhBaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.danhba_row, parent, false);
        return new DanhBaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhBaAdapter.DanhBaViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_ConTacts.size();
    }

    public void forTimKiem(ArrayList<ConTact> list){
        list_ConTacts = list;
        notifyDataSetChanged();
    }

    class DanhBaViewHolder extends RecyclerView.ViewHolder{
        TextView txtTen;
        ConstraintLayout layout_User_row;
        ImageView imgContact;
        public DanhBaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.txtTen);
            layout_User_row = (ConstraintLayout) itemView.findViewById(R.id.layout_User_row);
            imgContact = (ImageView) itemView.findViewById(R.id.imgContact);
        }

        public void bindView(int index){
            txtTen.setText(list_ConTacts.get(index).getName());
            layout_User_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_Add fragment_add = new Fragment_Add(case_interface2, list_ConTacts.get(index));
                    BackInf.BackInf(fragment_add);
                }
            });
            Bitmap bitmap = BitmapFactory.decodeByteArray(list_ConTacts.get(index).getImage(), 0,
                    list_ConTacts.get(index).getImage().length);
            imgContact.setImageBitmap(bitmap);
        }
    }
}
