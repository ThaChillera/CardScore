package com.robinkuiper.cardsscorekeeper.interfaces.player;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>  {
    private final PlayerManager PLAYERMANAGER = PlayerManager.getInstance();
    private final Activity ACTIVITY;

    public PlayerListAdapter(Activity ACTIVITY) {
        this.ACTIVITY = ACTIVITY;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CheckBox checkBox;
        MyViewHolder(CheckBox v) {
            super(v);
            checkBox = v;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CheckBox button = new CheckBox(viewGroup.getContext());
        button.setChecked(PLAYERMANAGER.isPlayerSelected(i));
        button.setText(PLAYERMANAGER.getPlayerName(i));
        button.setOnClickListener(new CheckBoxOnClickListener(i));
        button.setOnLongClickListener(new CheckBoxOnLongClickListener(i));

        MyViewHolder vh = new MyViewHolder(button);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        CheckBox button = viewHolder.checkBox;
        button.setChecked(PLAYERMANAGER.isPlayerSelected(i));
        button.setText(PLAYERMANAGER.getPlayerName(i));
        button.setOnClickListener(new CheckBoxOnClickListener(i));
        button.setOnLongClickListener(new CheckBoxOnLongClickListener(i));
    }

    @Override
    public int getItemCount() {
        return PLAYERMANAGER.getAllPlayersCount();
    }

    private class CheckBoxOnClickListener implements View.OnClickListener {
        final int ID;

        public CheckBoxOnClickListener(int ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View v) {
            CheckBox box = (CheckBox) v;
            if (box.isChecked()) {
                PLAYERMANAGER.selectPlayer(ID);
            } else {
                PLAYERMANAGER.deselectPlayer(ID);
            }
        }
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
