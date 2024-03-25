package com.gundemgaming.fukantin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gundemgaming.fukantin.R;
import com.gundemgaming.fukantin.ReadRepliesActivity;
import com.gundemgaming.fukantin.WriteReplyActivity;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Post;

import java.util.List;


public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostsCardViewHolder>{

    private Context context;
    private ConstraintLayout CL_MY_PROFILE;
    private List<Post> postList;
    private DatabaseRepository databaseRepository;

    public MyPostsAdapter(Context context, ConstraintLayout CL_MY_PROFILE, List<Post> postList) {
        this.context = context;
        this.CL_MY_PROFILE = CL_MY_PROFILE;
        this.postList = postList;
        databaseRepository = new DatabaseRepository(context, CL_MY_PROFILE);
    }

    public class MyPostsCardViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewPost_MP;
        TextView textViewTitle_MP;
        TextView textViewPost_MP;
        TextView textViewReplyNumber_MP;
        TextView textViewUsername_MP_cardview;
        TextView textViewPostDate_MP;
        ImageButton imageButtonReplyPost_MP;
        ImageButton imageButtonReadReplies_MP;
        ImageButton imageButtonMorePost_MP;

        public MyPostsCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewPost_MP = itemView.findViewById(R.id.cardViewPost_MP);
            textViewTitle_MP = itemView.findViewById(R.id.textViewTitle_MP);
            textViewPost_MP = itemView.findViewById(R.id.textViewPost_MP);
            textViewReplyNumber_MP = itemView.findViewById(R.id.textViewReplyNumber_MP);
            textViewUsername_MP_cardview = itemView.findViewById(R.id.textViewUsername_MP_cardview);
            textViewPostDate_MP = itemView.findViewById(R.id.textViewPostDate_MP);
            imageButtonReplyPost_MP = itemView.findViewById(R.id.imageButtonReplyPost_MP);
            imageButtonReadReplies_MP = itemView.findViewById(R.id.imageButtonReadReplies_MP);
            imageButtonMorePost_MP = itemView.findViewById(R.id.imageButtonMorePost_MP);
        }
    }

    @NonNull
    @Override
    public MyPostsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post_myposts, null);
        return new MyPostsCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsCardViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textViewTitle_MP.setText(post.getTitle());
        holder.textViewPost_MP.setText(post.getPost());
        holder.textViewReplyNumber_MP.setText(post.getNumberOfReplies());
        holder.textViewUsername_MP_cardview.setText(post.getUsername());
        holder.textViewPostDate_MP.setText(post.getDate());

        holder.cardViewPost_MP.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadRepliesActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getTitle());
            intent.putExtra("post", post.getPost());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("date", post.getDate());
            intent.putExtra("numberOfReplies", post.getNumberOfReplies());
            context.startActivity(intent);
        });

        holder.imageButtonReadReplies_MP.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadRepliesActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getTitle());
            intent.putExtra("post", post.getPost());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("date", post.getDate());
            intent.putExtra("numberOfReplies", post.getNumberOfReplies());
            context.startActivity(intent);
        });

        holder.imageButtonReplyPost_MP.setOnClickListener(view -> {
            Intent intent = new Intent(context, WriteReplyActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

        holder.imageButtonMorePost_MP.setOnClickListener(view -> {
            PopupMenu popupMenuMore = new PopupMenu(context, holder.imageButtonMorePost_MP);
            MenuInflater menuInflater = popupMenuMore.getMenuInflater();
            menuInflater.inflate(R.menu.menu_post_more, popupMenuMore.getMenu());

            popupMenuMore.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Sil")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Post Silinsin Mi ?");
                        alertDialog.setMessage("Post silindiğinde Post'a yapılan cevaplar da silinir.");

                        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DELETE POST
                                databaseRepository.deletePost(post.getPostId());
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
        return postList.size();
    }

}
