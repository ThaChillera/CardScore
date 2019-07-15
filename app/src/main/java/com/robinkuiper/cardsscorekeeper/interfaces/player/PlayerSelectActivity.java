package com.robinkuiper.cardsscorekeeper.interfaces.player;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;
import com.woxthebox.draglistview.DragItemRecyclerView;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        RecyclerView recyclerView = findViewById(R.id.playerselect_playerlist);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        mAdapter = new PlayerListAdapter(this);
//        recyclerView.setAdapter(mAdapter);

        final ArrayList<String> testData = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            testData.add("Item " + i);
        }

        //custom list
        DragListView dragItemRecyclerView = findViewById(R.id.playerselect_dragitemrecycler);
        dragItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dragItemRecyclerView.setAdapter(new PlayerListDraggableAdapter(this, testData), false);
        dragItemRecyclerView.setCanDragHorizontally(false);

        dragItemRecyclerView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    testData.add(toPosition, testData.get(fromPosition));
                    if (fromPosition > toPosition) {
                        testData.remove(fromPosition + 1);
                    } else {
                        testData.remove(fromPosition);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        PlayerManager.getInstance().savePlayerData(this);
    }

    public void onClickReturnClick(View v) {
        finish();
    }

    public void onClickCreatePlayer(View v) {
        Intent intent = new Intent(this, EditPlayerActivity.class);
        startActivity(intent);
    }
}
