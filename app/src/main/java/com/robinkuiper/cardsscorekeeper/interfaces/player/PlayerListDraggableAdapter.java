package com.robinkuiper.cardsscorekeeper.interfaces.player;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class PlayerListDraggableAdapter extends DragItemAdapter<Integer, PlayerListDraggableAdapter.PlayerViewHolder>  {
    private final Activity ACTIVITY;
    private final ArrayList<Pair<Long, String>> data;

    public PlayerListDraggableAdapter(Activity ACTIVITY, ArrayList<Pair<Long, String>> data) {
        this.ACTIVITY = ACTIVITY;
        this.data = data;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class PlayerViewHolder extends DragItemAdapter.ViewHolder {
        // each data item is just a string in this case
        private View player;
        private TextView textView;

        PlayerViewHolder(View v) {
            super(v, R.id.player_select_player_container, true);
            player = v;
            textView = v.findViewById(R.id.player_select_player_name);
        }

        void fillView(String playerName) {
            textView.setText(playerName);
        }
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View player = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_select_player, viewGroup, false);
//        button.setOnLongClickListener(new CheckBoxOnLongClickListener(i));

        PlayerViewHolder vh = new PlayerViewHolder(player);
        vh.fillView(data.get(i).second);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.fillView(data.get(i).second);
//        button.setOnLongClickListener(new CheckBoxOnLongClickListener(i));
    }

    @Override
    public long getUniqueItemId(int position) {
        return data.get(position).first;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class CheckBoxOnLongClickListener implements View.OnLongClickListener {
        final int ID;

        public CheckBoxOnLongClickListener(int ID) {
            this.ID = ID;
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(ACTIVITY, EditPlayerActivity.class);
            intent.putExtra(EditPlayerActivity.PLAYERIDEXTRA, ID);
            ACTIVITY.startActivity(intent);
            return true;
        }
    }
}