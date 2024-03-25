package com.gundemgaming.fukantin.dbrepository;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gundemgaming.fukantin.HomeActivity;
import com.gundemgaming.fukantin.MyProfileActivity;
import com.gundemgaming.fukantin.adapters.MyPostsAdapter;
import com.gundemgaming.fukantin.adapters.MyRepliesAdapter;
import com.gundemgaming.fukantin.adapters.PostAdapter;
import com.gundemgaming.fukantin.adapters.ReplyAdapter;
import com.gundemgaming.fukantin.models.Post;
import com.gundemgaming.fukantin.models.Reply;
import com.gundemgaming.fukantin.models.User;
import com.gundemgaming.fukantin.tools.AlertManager;
import com.gundemgaming.fukantin.urls.BaseUrls;
import com.gundemgaming.fukantin.urls.MsSqlUrls;
import com.gundemgaming.fukantin.urls.MysqlUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseRepository {
    private BaseUrls connectionUrl = new MysqlUrls();
    private Context context;
    private ConstraintLayout CL;

    public DatabaseRepository(Context context, ConstraintLayout CL) {
        this.context = context;
        this.CL = CL;
    }

    public void deletePost(int postId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Siliniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestDeletePost = new StringRequest(Request.Method.POST, connectionUrl.getDeletePostUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Silinirken Hata Oluştu");
                } else {
                    Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
                    Intent refresh = new Intent(context, MyProfileActivity.class);
                    refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(refresh);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Birşeyler Ters Gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("postId", String.valueOf(postId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestDeletePost);

    }

    public void deleteReply(int replyId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Siliniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestDeleteReply = new StringRequest(Request.Method.POST, connectionUrl.getDeleteReplyUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Silinirken Hata Oluştu");
                } else {
                    AlertManager.showSuccessfulAlert(CL, "Silindi");
                    Intent refresh = new Intent(context, MyProfileActivity.class);
                    refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(refresh);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("replyId", String.valueOf(replyId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestDeleteReply);
    }

    public void getMyPosts(View rootView, TextView textViewZeroPostInfo_MP, RecyclerView recyclerViewPost_MP, int userId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetPosts = new StringRequest(Request.Method.POST, connectionUrl.getMyPostsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response-getMyPosts", response);
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken hata oluştu");
                } else if(response.trim().equals("zero")) {
                    progressDialog.dismiss();
                    textViewZeroPostInfo_MP.setVisibility(View.VISIBLE);
                } else {
                    List<Post> postList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray posts = jsonObject.getJSONArray("posts");

                        for(int i = 0; i<posts.length(); i++) {
                            JSONObject jsonObjectPost = posts.getJSONObject(i);

                            int postId = jsonObjectPost.getInt("postId");
                            String title = jsonObjectPost.getString("title");
                            String post = jsonObjectPost.getString("post");
                            String date = jsonObjectPost.getString("date");
                            String username = jsonObjectPost.getString("username");
                            String numberOfReplies = jsonObjectPost.getString("numberOfReplies");

                            Post responsePost = new Post(postId, title, post, username, date, numberOfReplies);
                            postList.add(responsePost);
                        }

                        MyPostsAdapter myPostsAdapter = new MyPostsAdapter(rootView.getContext(), CL, postList);
                        recyclerViewPost_MP.setAdapter(myPostsAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(rootView.getContext()).add(requestGetPosts);

    }

    public void getUsersPosts(View rootView, TextView textViewZeroPostInfo_UP, RecyclerView recyclerViewPost_UP, int userId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetPosts = new StringRequest(Request.Method.POST, connectionUrl.getMyPostsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response-getMyPosts", response);
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu");
                } else if(response.trim().equals("zero")) {
                    progressDialog.dismiss();
                    textViewZeroPostInfo_UP.setVisibility(View.VISIBLE);
                } else {
                    List<Post> postList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray posts = jsonObject.getJSONArray("posts");

                        for(int i = 0; i<posts.length(); i++) {
                            JSONObject jsonObjectPost = posts.getJSONObject(i);

                            int postId = jsonObjectPost.getInt("postId");
                            String title = jsonObjectPost.getString("title");
                            String post = jsonObjectPost.getString("post");
                            String date = jsonObjectPost.getString("date");
                            String username = jsonObjectPost.getString("username");
                            String numberOfReplies = jsonObjectPost.getString("numberOfReplies");

                            Post responsePost = new Post(postId, title, post, username, date, numberOfReplies);
                            postList.add(responsePost);
                        }

                        PostAdapter postAdapter = new PostAdapter(rootView.getContext(), postList);
                        recyclerViewPost_UP.setAdapter(postAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(rootView.getContext()).add(requestGetPosts);
    }

    public void getMyReplies(View rootView, TextView textViewZeroReplyInfo_MP, RecyclerView recyclerViewReply_MP, int userId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetReplies = new StringRequest(Request.Method.POST, connectionUrl.getMyRepliesUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                } else if(response.equals("zero")) {
                    progressDialog.dismiss();
                    textViewZeroReplyInfo_MP.setVisibility(View.VISIBLE);
                } else {
                    List<Reply> replyList = new ArrayList<>();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray replies = jsonObject.getJSONArray("replies");

                        for(int i = 0; i<replies.length(); i++) {
                            JSONObject jsonObjectPost = replies.getJSONObject(i);

                            int replyId = jsonObjectPost.getInt("replyId");
                            String username = jsonObjectPost.getString("username");
                            String reply = jsonObjectPost.getString("reply");
                            String date = jsonObjectPost.getString("date");

                            Reply responseReply = new Reply(replyId, reply, username, date);
                            replyList.add(responseReply);
                        }
                        MyRepliesAdapter myRepliesAdapter = new MyRepliesAdapter(rootView.getContext(), CL ,replyList);
                        recyclerViewReply_MP.setAdapter(myRepliesAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(rootView.getContext()).add(requestGetReplies);

    }

    public void getUsersReplies(View rootView, TextView textViewZeroReplyInfo_UP, RecyclerView recyclerViewReply_UP, int userId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetReplies = new StringRequest(Request.Method.POST, connectionUrl.getMyRepliesUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                } else if(response.equals("zero")) {
                    progressDialog.dismiss();
                    textViewZeroReplyInfo_UP.setVisibility(View.VISIBLE);
                } else {
                    List<Reply> replyList = new ArrayList<>();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray replies = jsonObject.getJSONArray("replies");

                        for(int i = 0; i<replies.length(); i++) {
                            JSONObject jsonObjectPost = replies.getJSONObject(i);

                            int replyId = jsonObjectPost.getInt("replyId");
                            String username = jsonObjectPost.getString("username");
                            String reply = jsonObjectPost.getString("reply");
                            String date = jsonObjectPost.getString("date");

                            Reply responseReply = new Reply(replyId, reply, username, date);
                            replyList.add(responseReply);
                        }
                        ReplyAdapter replyAdapter = new ReplyAdapter(rootView.getContext(), replyList);
                        recyclerViewReply_UP.setAdapter(replyAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(rootView.getContext()).add(requestGetReplies);
    }

    public void updateBio(int userId, String bio) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Güncelleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestUpdateBio = new StringRequest(Request.Method.POST, connectionUrl.getUpdateUserBioUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Hata Oluştu");
                } else {
                    progressDialog.dismiss();
                    AlertManager.showSuccessfulAlert(CL, "Bio Güncellendi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("bio", bio);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestUpdateBio);

    }

    public void updateInstagramAddress(int userId, String instagramAddress) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Güncelleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestUpdateInstagram = new StringRequest(Request.Method.POST, connectionUrl.getUpdateInstagramUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Hata Oluştu");
                } else {
                    progressDialog.dismiss();
                    AlertManager.showSuccessfulAlert(CL, "Instagram Güncellendi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("instagram", instagramAddress);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestUpdateInstagram);

    }

    public void updateDepartment(int userId, String department) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Güncelleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestUpdateDepartment = new StringRequest(Request.Method.POST, connectionUrl.getUpdateDepartmentUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Hata Oluştu");
                } else {
                    progressDialog.dismiss();
                    AlertManager.showSuccessfulAlert(CL, "Fakülte Güncellendi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("department", department);
                return params;
            }
        } ;

        Volley.newRequestQueue(context).add(requestUpdateDepartment);

    }

    public void deleteAccount(int userId, SharedPreferences sharedPreferencesLogInInfo) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Siliniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestDeleteAccount = new StringRequest(Request.Method.POST, connectionUrl.getDeleteAccountUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Hata Oluştu");
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Hesap Silindi", Toast.LENGTH_SHORT).show();

                    //SharedPreferences Editor
                    SharedPreferences.Editor editorLogInInfo = sharedPreferencesLogInInfo.edit();
                    editorLogInInfo.clear();
                    editorLogInInfo.apply();

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestDeleteAccount);

    }

    public void getPosts(RecyclerView recyclerView) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetPosts = new StringRequest(Request.Method.GET, connectionUrl.getPostUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu");
                } else {
                    List<Post> postList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray posts = jsonObject.getJSONArray("posts");

                        for(int i = 0; i<posts.length(); i++) {
                            JSONObject jsonObjectPost = posts.getJSONObject(i);

                            int postId = jsonObjectPost.getInt("postId");
                            int categoryId = jsonObjectPost.getInt("categoryId");
                            String title = jsonObjectPost.getString("title");
                            String post = jsonObjectPost.getString("post");
                            int userId = jsonObjectPost.getInt("userId");
                            String username = jsonObjectPost.getString("username");
                            String date = jsonObjectPost.getString("date");
                            String numberOfReplies = jsonObjectPost.getString("numberOfReplies");

                            Post responsePost = new Post(postId, categoryId, title, post, userId, username, date, numberOfReplies);
                            postList.add(responsePost);
                        }
                        PostAdapter postAdapter = new PostAdapter(context, postList);
                        recyclerView.setAdapter(postAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        });

        Volley.newRequestQueue(context).add(requestGetPosts);
    }

    public void getPostsWithCategory(RecyclerView recyclerView, TextView textViewZeroPostInfo, int categoryId) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetPosts = new StringRequest(Request.Method.POST, connectionUrl.getPostsWithCategoryUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu");
                } else if(response.equals("zero")) {
                    progressDialog.dismiss();
                    recyclerView.setVisibility(View.INVISIBLE);
                    textViewZeroPostInfo.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewZeroPostInfo.setVisibility(View.INVISIBLE);
                    List<Post> postList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray posts = jsonObject.getJSONArray("posts");

                        for(int i = 0; i<posts.length(); i++) {
                            JSONObject jsonObjectPost = posts.getJSONObject(i);

                            int postId = jsonObjectPost.getInt("postId");
                            int categoryId = jsonObjectPost.getInt("categoryId");
                            String title = jsonObjectPost.getString("title");
                            String post = jsonObjectPost.getString("post");
                            int userId = jsonObjectPost.getInt("userId");
                            String username = jsonObjectPost.getString("username");
                            String date = jsonObjectPost.getString("date");
                            String numberOfReplies = jsonObjectPost.getString("numberOfReplies");

                            Post responsePost = new Post(postId, categoryId, title, post, userId, username, date, numberOfReplies);
                            postList.add(responsePost);
                        }
                        PostAdapter postAdapter = new PostAdapter(context, postList);
                        recyclerView.setAdapter(postAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("categoryId", String.valueOf(categoryId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestGetPosts);
    }

    public void loginUser(User enteredUser, SharedPreferences.Editor editorLogInInfo) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestUserLogin = new StringRequest(Request.Method.POST, connectionUrl.getUserLoginUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Kullanıcı Adı veya Şifre Hatalı");
                } else {
                    User responseUser = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray users = jsonObject.getJSONArray("users");

                        for (int i=0; i<users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);

                            int userId = user.getInt("userId");
                            String username = user.getString("username");
                            String password = user.getString("password");

                            responseUser = new User(userId, username, password);
                        }

                        editorLogInInfo.putInt("userId", responseUser.getUserId());
                        editorLogInInfo.putString("username", enteredUser.getUsername());
                        editorLogInInfo.putString("password", enteredUser.getPassword());
                        editorLogInInfo.putBoolean("isUserEntered",true);

                        editorLogInInfo.apply();

                        progressDialog.dismiss();

                        Toast toast = Toast.makeText(context, "Giriş Başarılı", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", enteredUser.getUsername());
                params.put("password", enteredUser.getPassword());
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestUserLogin);

    }

    public void getMyProfile(int userId, TextView textViewUsername_MP, TextView textViewDepartment_MP, TextView textViewInstagramAddress_MP, TextView textViewBiography_MP) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetMyProfile = new StringRequest(Request.Method.POST, connectionUrl.getMyProfileUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu - RESPONSE = ERROR");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray users = jsonObject.getJSONArray("users");

                        for(int i = 0; i<users.length(); i++) {
                            JSONObject jsonObjectUser = users.getJSONObject(i);

                            String bio = jsonObjectUser.getString("biography");
                            String instagramAddress = jsonObjectUser.getString("instagramAddress");
                            String department = jsonObjectUser.getString("department");

                            textViewUsername_MP.setText(jsonObjectUser.getString("username"));
                            textViewDepartment_MP.setText(department);
                            textViewInstagramAddress_MP.setText(instagramAddress);
                            textViewBiography_MP.setText(bio);

                            progressDialog.dismiss();
                        }

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti_JSON");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestGetMyProfile);

    }

    public void getReplies(int postId, RecyclerView recyclerviewReply_RR, TextView textViewZeroReplyInfo) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetReplies = new StringRequest(Request.Method.POST, connectionUrl.getRepliesUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu");
                } else if(response.equals("zero")) {
                    progressDialog.dismiss();
                    recyclerviewReply_RR.setVisibility(View.INVISIBLE);
                    textViewZeroReplyInfo.setVisibility(View.VISIBLE);
                } else {
                    List<Reply> replyList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray replies = jsonObject.getJSONArray("replies");

                        for(int i = 0; i<replies.length(); i++) {
                            JSONObject jsonObjectReply = replies.getJSONObject(i);

                            int replyId = jsonObjectReply.getInt("replyId");
                            int userId = jsonObjectReply.getInt("userId");
                            String reply = jsonObjectReply.getString("reply");
                            String username = jsonObjectReply.getString("username");
                            String date = jsonObjectReply.getString("date");

                            Reply responseReply = new Reply(replyId, userId, reply, username, date);
                            replyList.add(responseReply);
                        }

                        ReplyAdapter replyAdapter = new ReplyAdapter(context, replyList);
                        recyclerviewReply_RR.setAdapter(replyAdapter);

                        progressDialog.dismiss();

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("postId", String.valueOf(postId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestGetReplies);

    }

    public void insertUser(String newUserUsername, String newUserPassword, ProgressDialog progressDialog, SharedPreferences.Editor editorLogInInfo) {
        User user = new User(newUserUsername, newUserPassword);
        StringRequest requestInsertUser = new StringRequest(Request.Method.POST, connectionUrl.getInsertUserUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Belirtilen Kullanici Adı Daha Önce Kullanılmış.");
                } else {
                    User responseUser = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray user = jsonObject.getJSONArray("user");

                        for(int i=0; i<user.length(); i++) {
                            JSONObject jsonObjectUser = user.getJSONObject(i);

                            int userId = jsonObjectUser.getInt("userId");
                            int userDetailId = jsonObjectUser.getInt("userDetailId");

                            responseUser = new User(userId, userDetailId);
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(context, "Kayıt Başarılı", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();

                    //Saving username & password & userStatus
                    editorLogInInfo.putInt("userId", responseUser.getUserId());
                    editorLogInInfo.putInt("userDetailId", responseUser.getUserDetailId());
                    editorLogInInfo.putString("username",newUserUsername);
                    editorLogInInfo.putString("password", newUserPassword);
                    editorLogInInfo.putBoolean("isUserEntered", true);
                    editorLogInInfo.apply();

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());

                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestInsertUser);
    }

    public void insertPost(int userId, String title, String post, int categoryId) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Paylaşılıyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestInsertEntry = new StringRequest(Request.Method.POST, connectionUrl.getInsertPostUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Post Paylaşılırken Hata Oluştu - SERVİS İÇİ");
                } else {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(context, "Post Paylaşıldı", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("categoryId", String.valueOf(categoryId));
                params.put("title",title);
                params.put("post", post);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestInsertEntry);

    }

    public void getUserProfile(int userId, TextView textViewUsername_UP, TextView textViewDepartment_UP, TextView textViewInstagramAddress_UP, TextView textViewBiography_UP) {
        //ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestGetMyProfile = new StringRequest(Request.Method.POST, connectionUrl.getMyProfileUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Yüklenirken Hata Oluştu - RESPONSE = ERROR");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray users = jsonObject.getJSONArray("users");

                        for(int i = 0; i<users.length(); i++) {
                            JSONObject jsonObjectUser = users.getJSONObject(i);

                            textViewUsername_UP.setText(jsonObjectUser.getString("username"));
                            textViewDepartment_UP.setText(jsonObjectUser.getString("department"));
                            textViewInstagramAddress_UP.setText(jsonObjectUser.getString("instagramAddress"));
                            textViewBiography_UP.setText(jsonObjectUser.getString("biography"));

                            progressDialog.dismiss();
                        }

                    } catch (JSONException exception) {
                        progressDialog.dismiss();
                        AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                        exception.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestGetMyProfile);

    }

    public void insertReply(int postId, int userId, String reply) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Paylaşılıyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestInsertReply = new StringRequest(Request.Method.POST, connectionUrl.getInsertReplyUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    progressDialog.dismiss();
                    AlertManager.showErrorAlert(CL, "Beklenmedik Bir Hata Meydana Geldi");
                } else {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(context, "Cevap Paylaşıldı", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError) {
                    AlertManager.showErrorAlert(CL, "İnternet Bağlantınız Yok");
                } else if(error instanceof NetworkError) {
                    AlertManager.showErrorAlert(CL, "Ağ Hatası Meydana Geldi");
                } else if(error instanceof TimeoutError) {
                    AlertManager.showErrorAlert(CL, "Bağlantı Zaman Aşımına Uğradı");
                } else {
                    AlertManager.showErrorAlert(CL, "Bir şeyler ters gitti");
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("postId", String.valueOf(postId));
                params.put("userId", String.valueOf(userId));
                params.put("reply", reply);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(requestInsertReply);

    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ConstraintLayout getCL() {
        return CL;
    }

    public void setCL(ConstraintLayout CL) {
        this.CL = CL;
    }



}
