package com.supsi.alessandro.pollutionproblemclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supsi.alessandro.pollutionproblemclient.R;
import com.supsi.alessandro.pollutionproblemclient.api.pojo.Child;

import java.util.List;

/**
 * Created by Alessandro on 15/07/2017.
 */

public class ChildrenListAdapter extends RecyclerView.Adapter<ChildrenListAdapter.ChildViewHolder> {

    private List<Child> mChildren;

    public ChildrenListAdapter(List<Child> children){
        mChildren = children;
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_card_view_layout, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        //Update the cards values
        holder.childName.setText(mChildren.get(position).getFirstName());
    }

    @Override
    public int getItemCount() {
        return mChildren.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView childName;
        TextView description;
        ImageView childImage;

        public ChildViewHolder(View itemView) {
            super(itemView);

            childName = (TextView) itemView.findViewById(R.id.tv_child_name);
            description = (TextView) itemView.findViewById(R.id.tv_child_description);
            childImage = (ImageView) itemView.findViewById(R.id.iv_child_image);

        }
    }
}
