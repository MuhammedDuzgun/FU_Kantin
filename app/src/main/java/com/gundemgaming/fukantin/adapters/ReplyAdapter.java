package com.gundemgaming.fukantin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gundemgaming.fukantin.R;
import com.gundemgaming.fukantin.UserProfileActivity;
import com.gundemgaming.fukantin.models.Reply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyCardViewHolder>{

    private Context context;
    private List<Reply> replyList;

    public ReplyAdapter(Context context, List<Reply> replyList) {
        this.context = context;
        this.replyList = replyList;
    }

    public class ReplyCardViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewReply;
        public TextView textViewReplyUsername;
        public TextView textViewReplyDate;

        public ReplyCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReply = itemView.findViewById(R.id.textViewReply);
            textViewReplyUsername = itemView.findViewById(R.id.textViewReplyUsername);
            textViewReplyDate = itemView.findViewById(R.id.textViewReplyDate);
        }
    }

    @NonNull
    @Override
    public ReplyCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_reply, null);
        return new ReplyCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyCardViewHolder holder, int position) {
        Reply reply = replyList.get(position);

        holder.textViewReply.setText(reply.getReply());
        holder.textViewReplyUsername.setText(reply.getUsername());
        holder.textViewReplyDate.setText(reply.getDate());

        holder.textViewReplyUsername.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("userId", reply.getUserId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

}
