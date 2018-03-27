package com.example.android.projectx.HomeScreen.NotificationsFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.android.projectx.HomeScreen.NotificationsFragments.NotificationsModel.NotificationsInfo;
import com.example.android.projectx.HomeScreen.NotificationsFragments.NotificationsModel.NotificationsMain;
import com.example.android.projectx.R;
import com.example.android.projectx.Retrofit.ApiClient;
import com.example.android.projectx.Retrofit.ApiService;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.android.projectx.HomeScreen.NotificationsFragments.SimpleAdpater;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment  {

    UltimateRecyclerView ultimateRecyclerView;
    SimpleAdpater simpleRecyclerViewAdapter = null;

    int moreNum = 2;

    private ItemTouchHelper mItemTouchHelper;


    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View view = inflater.inflate(R.layout.fragment_notifications, container, false);


         final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Notifications...");
         dialog.show();
         ultimateRecyclerView= view.findViewById(R.id.ultimate_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);
//        linearLayoutManager = new LinearLayoutManager(getContext());
        final ScrollSmoothLineaerLayoutManager mgm = new ScrollSmoothLineaerLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 300);
            ultimateRecyclerView.setLayoutManager(mgm);




//        ultimateRecyclerView.setLayoutManager(linearLayoutManager);



        ApiService api = ApiClient.getApiService();
        Call<NotificationsMain> call = api.getNotifications1();
        call.enqueue(new Callback<NotificationsMain>() {
            @Override
            public void onResponse(Call<NotificationsMain> call, Response<NotificationsMain> response) {
                if(response.isSuccessful()){
                    List<NotificationsInfo> list = new ArrayList<>();
                    for(int i = 0;i<response.body().getData().size();i++){
                        list.add(response.body().getData().get(i));

                    }

                    simpleRecyclerViewAdapter= new SimpleAdpater(getContext(),list);
                    ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    simpleRecyclerViewAdapter.insert(new NotificationsInfo("https://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg",
                                            "2018-03-25T00:00:00",0000,"Pravesh"+moreNum++,0,true,"2018-03-25T00:00:00"), 0);
                                    ultimateRecyclerView.setRefreshing(false);
                                    //   ultimateRecyclerView.scrollBy(0, -50);
                                    mgm.scrollToPosition(0);
//                        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
//                        simpleRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }, 1000);
                        }
                    });


//                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(simpleRecyclerViewAdapter);
//                    mItemTouchHelper = new ItemTouchHelper(callback);
//                    mItemTouchHelper.attachToRecyclerView(ultimateRecyclerView.mRecyclerView);


                    ultimateRecyclerView.setLoadMoreView(LayoutInflater.from(getContext())
                            .inflate(R.layout.custom_bottom_progressbar, null));
                    ultimateRecyclerView.reenableLoadmore();
                    dialog.dismiss();
                    ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);



                    ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
                        @Override
                        public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
//                                    simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
////                        ultimateRecyclerView.reenableLoadmore();
//                                    simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                                    simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                                    simpleRecyclerViewAdapter.notifyDataSetChanged();
                                    simpleRecyclerViewAdapter.insert(new NotificationsInfo("https://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg",
                                            "2018-03-25T00:00:00",0000,"Pravesh"+moreNum++,0,true,"2018-03-25T00:00:00"), simpleRecyclerViewAdapter.getAdapterItemCount());
                                    simpleRecyclerViewAdapter.notifyDataSetChanged();

                                    // mgm.scrollToPositionWithOffset(maxLastVisiblePosition,-1);
//                           mgm.scrollToPosition(maxLastVisiblePosition);

                                }
                            }, 1000);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NotificationsMain> call, Throwable t) {

            }
        });



        return view;
    }


}
