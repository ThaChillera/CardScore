package com.robinkuiper.cardsscorekeeper.interfaces.player;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItemRecyclerView;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PlayerSelectActivity extends AppCompatActivity {
    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final String TAG = "PlayerSelectActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setup data containers
        ArrayList<Pair<Long, String>> unselectedPlayers = playerManager.getAllPlayers();
        ArrayList<Pair<Long, String>> selectedPlayers = new ArrayList<>();

        //setup board
        BoardView boardView = findViewById(R.id.playerselect_dragboard);
        boardView.setSnapToColumnsWhenScrolling(true);
        boardView.setSnapToColumnWhenDragging(true);
        boardView.setSnapDragItemToTouch(true);
        boardView.setColumnWidth(400);
        boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);

        boardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                Log.d(TAG, "onItemDragStarted: ");
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                Log.d(TAG, "onItemDragEnded: ");
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                Log.d(TAG, "onItemChangedPosition: ");
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                Log.d(TAG, "onItemChangedColumn: ");
            }

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
                Log.d(TAG, "onFocusedColumnChanged: ");
            }

            @Override
            public void onColumnDragStarted(int position) {
                Log.d(TAG, "onColumnDragStarted: ");
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
                Log.d(TAG, "onColumnDragChangedPosition: ");
            }

            @Override
            public void onColumnDragEnded(int position) {
                Log.d(TAG, "onColumnDragEnded: ");
            }
        });

        View selectedHeader = View.inflate(getApplicationContext(), R.layout.player_select_player, null);
        ((TextView) selectedHeader.findViewById(R.id.player_select_player_name)).setText("Selected Players");
        View unselectedHeader = View.inflate(getApplicationContext(), R.layout.player_select_player, null);
        ((TextView) unselectedHeader.findViewById(R.id.player_select_player_name)).setText("Unselected Players");

        boardView.addColumn(new PlayerListDraggableAdapter(this, selectedPlayers), selectedHeader, null, true);
        boardView.addColumn(new PlayerListDraggableAdapter(this, unselectedPlayers), unselectedHeader, null, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
