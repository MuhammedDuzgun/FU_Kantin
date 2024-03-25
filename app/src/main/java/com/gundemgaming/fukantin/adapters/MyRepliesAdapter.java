package com.gundemgaming.fukantin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gundemgaming.fukantin.R;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Reply;

import java.util.List;


public class MyRepliesAdapter extends RecyclerView.Adapter<MyRepliesAdapter.MyRepliesCardViewHolder>{

    private Context context;
    private ConstraintLayout CL_MY_PROFILE;
    private List<Reply> replyList;
    private DatabaseRepository databaseRepository;
    public MyRepliesAdapter(Context context, ConstraintLayout CL_MY_PROFILE ,List<Reply> replyList) {
        this.context = context;
        this.CL_MY_PROFILE = CL_MY_PROFILE;
        this.replyList = replyList;
        databaseRepository = new DatabaseRepository(context, CL_MY_PROFILE);
    }

    public class MyRepliesCardViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewReply_MP;
        public TextView textViewReplyUsername_MP;
        public TextView textViewReplyDate_MP;
        public ImageButton imageButtonMoreReply_MP;

        public MyRepliesCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReply_MP = itemView.findViewById(R.id.textViewReply_MP);
            textViewReplyUsername_MP = itemView.findViewById(R.id.textViewReplyUsername_MP);
            textViewReplyDate_MP = itemView.findViewById(R.id.textViewReplyDate_MP);
            imageButtonMoreReply_MP = itemView.findViewById(R.id.imageButtonMoreReply_MP);
        }

    }

    @NonNull
    @Override
    public MyRepliesCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_reply_myreplies, null);
        return new MyRepliesCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRepliesCardViewHolder holder, int position) {
        Reply reply = replyList.get(position);

        holder.textViewReply_MP.setText(reply.getReply());
        holder.textViewReplyUsername_MP.setText(reply.getUsername());
        holder.textViewReplyDate_MP.setText(reply.getDate());

        holder.imageButtonMoreReply_MP.setOnClickListener(view -> {
            PopupMenu popupMenuMore = new PopupMenu(context, holder.imageButtonMoreReply_MP);
            MenuInflater menuInflater = popupMenuMore.getMenuInflater();
            menuInflater.inflate(R.menu.menu_reply_more, popupMenuMore.getMenu());

            popupMenuMore.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Sil")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Cevap Silinsin Mi ?");

                        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DELETE REPLY
                                databaseRepository.deleteReply(reply.getReplyId());
                            }
                        });

                        alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alertDialog.create().show();
                    }
                    return false;
                }
            });
            popupMenuMore.show();
        });

    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

}
