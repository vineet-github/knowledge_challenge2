package com.example.lmstask;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserModel> item;
    Context context;
    private int lastPosition = -1;
    public UserAdapter(Context context, List<UserModel> item) {
        this.item = item;
        this.context = context;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_element, null);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
          holder.userfullnametv.setText(item.get(position).getFull_name());
          holder.usernametv.setText(item.get(position).getLast_name());
          holder.grouptv.setText(item.get(position).getGroup());

          if(position%2==0){
              holder.linearLayout.setBackgroundColor(Color.parseColor("#dfdfdf"));

          }
          else{
              holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
          }



    }

    @Override
    public int getItemCount() {
        return item.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernametv, userfullnametv, grouptv;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            usernametv=itemView.findViewById(R.id.full_name);
            userfullnametv=itemView.findViewById(R.id.name);
            grouptv=itemView.findViewById(R.id.group_name);
            linearLayout=itemView.findViewById(R.id.linear_ll);




        }
    }


}
