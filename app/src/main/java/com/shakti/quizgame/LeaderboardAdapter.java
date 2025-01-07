package com.shakti.quizgame;
import android.hardware.usb.UsbRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<User> userList;

    public LeaderboardAdapter(List<User> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewRank.setText(String.valueOf(position + 1)+". ");
        holder.textViewScore.setText(String.valueOf(user.getScore()));
        holder.img.setImageResource(R.drawable.coder);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void update(List<User> newList) {
        this.userList=newList;
        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewScore,textViewRank;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRank=itemView.findViewById(R.id.textViewRank);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewScore = itemView.findViewById(R.id.textViewScore);
            img=itemView.findViewById(R.id.dp);
        }
    }

}
