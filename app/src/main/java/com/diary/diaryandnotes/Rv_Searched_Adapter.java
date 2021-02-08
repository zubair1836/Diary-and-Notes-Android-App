package com.diary.diaryandnotes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Rv_Searched_Adapter extends RecyclerView.Adapter<Rv_Searched_Adapter.ViewHolder> {

    ArrayList<String> arl_folder_name;
    Context c;
    ArrayList<String> arl_lastmsg;

    Rv_Searched_Adapter(ArrayList<String> arl_folder_name, ArrayList<String> arl_lastmsg) {
        this.arl_folder_name = arl_folder_name;
        this.arl_lastmsg = arl_lastmsg;

    }

    @NonNull
    @Override
    public Rv_Searched_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_Searched_Adapter.ViewHolder holder, int position) {
        holder.SetData(position);

    }

    @Override
    public int getItemCount() {
        return arl_folder_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button bt_firstLatter;
        TextView tv_F_name;
        TextView tv_short_link;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bt_firstLatter=itemView.findViewById(R.id.bt_firstlatter);
            tv_F_name = itemView.findViewById(R.id.tv_f_name);
            tv_short_link = itemView.findViewById(R.id.tv_short_link);

        }

        public void SetData(final int position) {

            char f_char = arl_folder_name.get(position).toUpperCase().charAt(0);

            Log.d("MAINADPAPer", "SetData: "+f_char);

            bt_firstLatter.setText(String.valueOf(f_char));

            tv_F_name.setText(arl_folder_name.get(position));
            tv_short_link.setText(arl_lastmsg.get(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c,Show_Chats.class);
                    i.putExtra("folder",arl_folder_name.get(position));
                    c.startActivity(i);
                }
            });

        }
    }
}
