package io.github.thachillera.cardsscorekeeper.interfaces.player;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder> {
    private final PlayerManager PLAYERMANAGER = PlayerManager.getInstance();
    private final Activity ACTIVITY;

    public PlayerListAdapter(Activity ACTIVITY) {
        this.ACTIVITY = ACTIVITY;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CheckBox button = new CheckBox(viewGroup.getContext());
        MyViewHolder vh = new MyViewHolder(button);

        vh.updateContents(PLAYERMANAGER.getActivePlayerIds()[i]);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.updateContents(PLAYERMANAGER.getActivePlayerIds()[i]);
    }

    @Override
    public int getItemCount() {
        return PLAYERMANAGER.getActivePlayersCount();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CheckBox checkBox;

        MyViewHolder(CheckBox v) {
            super(v);
            checkBox = v;
        }

        void updateContents(long playerId) {
            checkBox.setChecked(PLAYERMANAGER.isPlayerSelected(playerId));
            checkBox.setText(PLAYERMANAGER.getPlayerName(playerId));
            checkBox.setOnClickListener(new CheckBoxOnClickListener(playerId));
            checkBox.setOnLongClickListener(new CheckBoxOnLongClickListener(playerId));
        }
    }

    private class CheckBoxOnClickListener implements View.OnClickListener {
        final long ID;

        public CheckBoxOnClickListener(long ID) {
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
        final long ID;

        public CheckBoxOnLongClickListener(long ID) {
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
