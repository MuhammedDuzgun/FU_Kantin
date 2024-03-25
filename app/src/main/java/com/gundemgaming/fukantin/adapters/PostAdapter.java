package com.gundemgaming.fukantin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gundemgaming.fukantin.LoginActivity;
import com.gundemgaming.fukantin.R;
import com.gundemgaming.fukantin.ReadRepliesActivity;
import com.gundemgaming.fukantin.UserProfileActivity;
import com.gundemgaming.fukantin.WriteReplyActivity;
import com.gundemgaming.fukantin.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostCardViewHolder>{

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Adapter Elements
    private Context context;
    private List<Post> postList;


    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public class PostCardViewHolder extends RecyclerView.ViewHolder {
        public CardView cardViewPost;
        public TextView textViewPostTitle;
        public TextView textViewPost;
        public TextView textViewUsername;
        public TextView textViewNumberOfReplay;
        public TextView textViewDate;
        public ImageButton imageButtonReplyPost;
        public ImageButton imageButtonReadReplies;
        public ImageView imageViewUserProfile;

        public PostCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewPost = itemView.findViewById(R.id.cardviewPost);
            textViewPostTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewPost = itemView.findViewById(R.id.textViewPost);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewNumberOfReplay = itemView.findViewById(R.id.textViewReplyNumber);
            textViewDate = itemView.findViewById(R.id.textViewPostDate);
            imageButtonReplyPost = itemView.findViewById(R.id.imageButtonReplyPost);
            imageButtonReadReplies = itemView.findViewById(R.id.imageButtonReadReplies);
            imageViewUserProfile = itemView.findViewById(R.id.imageViewUserProfile);
        }
    }

    @NonNull
    @Override
    public PostCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, null);
        return new PostCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCardViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textViewPostTitle.setText(post.getTitle());
        holder.textViewPost.setText(post.getPost());
        holder.textViewUsername.setText(post.getUsername());
        holder.textViewDate.setText(post.getDate());
        holder.textViewNumberOfReplay.setText(post.getNumberOfReplies());

        holder.cardViewPost.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadRepliesActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getTitle());
            intent.putExtra("post", post.getPost());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("date", post.getDate());
            intent.putExtra("numberOfReplies", post.getNumberOfReplies());
            context.startActivity(intent);
        });

        holder.imageButtonReplyPost.setOnClickListener(view -> {
            sharedPreferencesLogInInfo = context.getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
            boolean isUserEntered = sharedPreferencesLogInInfo.getBoolean("isUserEntered" ,false);

            if(isUserEntered) {
                Intent intent = new Intent(context, WriteReplyActivity.class);
                intent.putExtra("postId", post.getPostId());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }

        });

        holder.imageButtonReadReplies.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadRepliesActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getTitle());
            intent.putExtra("post", post.getPost());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("date", post.getDate());
            intent.putExtra("numberOfReplies", post.getNumberOfReplies());
            context.startActivity(intent);
        });

        holder.textViewUsername.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("userId", post.getUserId());
            context.startActivity(intent);
        });

        holder.imageViewUserProfile.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("userId", post.getUserId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
