package com.shakti.quizgame;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    Context context;
    private List<Test> testList;

    public TestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false); // Replace with your item layout
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.TestViewHolder holder, int position) {
        Test test=testList.get(position);
        holder.testName.setText(test.getTestName());
        holder.status.setText("unlocked");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,test.getIntentClass());
                intent.putExtra("test",holder.getAdapterPosition());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        TextView testName,status;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testName=itemView.findViewById(R.id.testName);
            status=itemView.findViewById(R.id.status);
        }
    }
}
