package com.rimzhim.Fragments;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rimzhim.Adapters.chatAdapter;
import com.rimzhim.Adapters.contestAdapter;
import com.rimzhim.Chat.ChatA;
import com.rimzhim.Chat.InboxAdapter1;
import com.rimzhim.Chat.InboxModel;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.ModelClasses.ChatModel.ChatsInboxesResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.rimzhim.ModelClasses.ChatModel.DataItem;
import com.rimzhim.SimpleClasses.PaginationListener;


public class ChatFragment  extends RootFragment {
    /*RecyclerView inboxRecyclerView;
    List<DataItem> inboxes =new ArrayList<>();
    ImageView changebackIV;
    ShimmerFrameLayout shimmer;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    chatAdapter adapter;*/

    Context context;
    RecyclerView inboxList;
    ArrayList<InboxModel> inboxArraylist;
    DatabaseReference rootRef;
    InboxAdapter1 inboxAdapter;
    ProgressBar pbar;
    boolean isviewCreated = false;



    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
       /* inboxRecyclerView = (RecyclerView)view.findViewById(R.id.inboxRecyclerView);
        shimmer =(ShimmerFrameLayout)view.findViewById(R.id.shimmer);*/

        context = getContext();

        rootRef = FirebaseDatabase.getInstance("https://rimzhim-3ff4e-default-rtdb.firebaseio.com/").getReference();


        pbar = view.findViewById(R.id.pbar);
        inboxList = view.findViewById(R.id.inboxlist);

        // intialize the arraylist and and inboxlist
        inboxArraylist = new ArrayList<>();

        inboxList = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inboxList.setLayoutManager(layout);
        inboxList.setHasFixedSize(false);
        inboxAdapter = new InboxAdapter1(context, inboxArraylist, new InboxAdapter1.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(InboxModel item) {
                chatFragment(item.getId(), item.getName(), item.getPic());
            }
        }, new InboxAdapter1.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(InboxModel item) {

            }
        });

        inboxList.setAdapter(inboxAdapter);



       /* view.findViewById(R.id.back_btn).setOnClickListener(v -> {
            //InboxA.super.onBackPressed();


        });*/
        isviewCreated = true;
        getData(view);
        return view;
    }



  /*  private void loadInbox() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("page",String.valueOf(currentPage));
        shimmer.startShimmer();
     //   Functions.showProgressDialog(getContext(),"Loading...");
        Call<ChatsInboxesResponse> call = ApiClient.getUserService().ChatsList(hashMap);
        call.enqueue(new Callback<ChatsInboxesResponse>() {
            @Override
            public void onResponse(Call<ChatsInboxesResponse> call, Response<ChatsInboxesResponse> response) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                inboxRecyclerView.setVisibility(View.VISIBLE);
                if(response.isSuccessful()){
                  //  Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                       // Functions.dismissProgressDialog();

                        if(!response.body().getChat_list().getData().isEmpty()){
                            PAGE_SIZE = response.body().getChat_list().getLast_page();
                            inboxes = response.body().getChat_list().getData();
                            *//*chatAdapter adapter = new chatAdapter(getContext(),inboxes);
                            inboxRecyclerView.setAdapter(adapter);*//*

                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(inboxes);
                            if (currentPage < PAGE_SIZE) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;


                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ChatsInboxesResponse> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                inboxRecyclerView.setVisibility(View.VISIBLE);
               // Functions.dismissProgressDialog();
               // Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }*/

    ValueEventListener eventListener2;
    Query inboxQuery;

    public void getData(View view) {

        pbar.setVisibility(View.VISIBLE);

        inboxQuery = rootRef.child("Inbox").child(String.valueOf(SharedPrefManager.getInstance(getContext()).getUser().getId())).orderByChild("date");
        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                pbar.setVisibility(View.GONE);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    InboxModel model = ds.getValue(InboxModel.class);
                    model.setId(ds.getKey());

                    inboxArraylist.add(model);
                }


                if (inboxArraylist.isEmpty()) {
                    Functions.showToast(context, getString(R.string.no_data));
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else {
                    Functions.showToast(context,  getString(R.string.no_data));
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    Collections.reverse(inboxArraylist);
                    inboxAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbar.setVisibility(View.GONE);
               view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
            }
        };

        inboxQuery.addValueEventListener(eventListener2);


    }


    // on stop we will remove the listener
    @Override
    public void onStop() {
        super.onStop();
        if (inboxQuery != null)
            inboxQuery.removeEventListener(eventListener2);
    }


    //open the chat fragment and on item click and pass your id and the other person id in which
    //you want to chat with them and this parameter is that is we move from match list or inbox list
    public void chatFragment(String receiverid, String name, String picture) {
        Intent intent=new Intent(getContext(), ChatA.class);
        intent.putExtra("user_id", receiverid);
        intent.putExtra("user_name", name);
        intent.putExtra("user_pic", picture);
        resultCallback.launch(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    ActivityResultLauncher<Intent> resultCallback = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getBooleanExtra("isShow",false))
                        {

                        }
                    }
                }
            });





    @Override
    public void onResume() {
        super.onResume();
       /* inboxRecyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        inboxes.clear();
        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        inboxRecyclerView.setLayoutManager(layout);
        inboxRecyclerView.setHasFixedSize(false);
        adapter = new chatAdapter(new ArrayList<>(),getContext());
        inboxRecyclerView.setAdapter(adapter);
        loadInbox();
        inboxRecyclerView.addOnScrollListener(new PaginationListener(layout) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadInbox();
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/
    }


}