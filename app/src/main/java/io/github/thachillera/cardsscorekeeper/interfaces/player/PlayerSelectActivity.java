package io.github.thachillera.cardsscorekeeper.interfaces.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;

public class PlayerSelectActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.playerselect_playerlist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PlayerListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        PersistenceManager.getInstance().savePlayerData();
    }

    public void onClickReturnClick(View v) {
        finish();
    }

    public void onClickCreatePlayer(View v) {
        Intent intent = new Intent(this, EditPlayerActivity.class);
        startActivity(intent);
    }
}
