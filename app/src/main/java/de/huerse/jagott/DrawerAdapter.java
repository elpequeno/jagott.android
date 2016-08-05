package de.huerse.jagott;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>{
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;

    private List<String> rows;

    private int[] mHeaderImages = {R.drawable.header_image_1,
            R.drawable.header_image_2,
            R.drawable.header_image_3};

    private int[] mNavigationImages = {R.drawable.ic_action_today,
            R.drawable.ic_action_collection,
            R.drawable.ic_action_group,
            R.drawable.ic_action_alarms,
            R.drawable.ic_fav};

    ViewGroup mViewGroup;

    public DrawerAdapter(List<String> rows) {
        this.rows = rows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewGroup = parent;
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            Random r = new Random();
            int index = r.nextInt(3);
            view.setBackgroundResource(mHeaderImages[index]);
            return new ViewHolder(view, viewType);
        } else if (viewType == ROW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolder(view, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.mViewType == ROW_TYPE) {
            String rowText = rows.get(position - 1);
            holder.textView.setText(rowText);
            holder.imageView.setImageResource(mNavigationImages[position-1]);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ROW_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected int mViewType;

        @InjectView(R.id.drawer_row_icon) ImageView imageView;
        @InjectView(R.id.drawer_row_text) TextView textView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            this.mViewType = viewType;

            if (viewType == ROW_TYPE) {
                ButterKnife.inject(this, itemView);
            }

            itemView.setOnClickListener(this);
            Global.GlobalMainActivity.onNavigationDrawerItemSelected(0);
        }

        @Override
        public void onClick(View view) {
            if (mViewType == 0){
                return;
            } else {

                Global.GlobalMainActivity.onNavigationDrawerItemSelected(getAdapterPosition()-1);
                }
        }
    }
}