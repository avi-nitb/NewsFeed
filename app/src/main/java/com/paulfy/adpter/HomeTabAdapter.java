package com.paulfy.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.paulfy.R;
import com.paulfy.fragments.HomeTabFragment;
import com.paulfy.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class HomeTabAdapter extends RecyclerView.Adapter<HomeTabAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryModel.Data> dataList;
    private List<Integer> categories = new ArrayList();
    private TextView btn_load;
    private HomeTabFragment fragment;

    public HomeTabAdapter(Context context, List<CategoryModel.Data> dataList, TextView btn_load, HomeTabFragment fragment) {
        this.context = context;
        this.dataList = dataList;
        this.btn_load = btn_load;
        this.fragment = fragment;
    }


    @Override
    public HomeTabAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.items_categorylist, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(final HomeTabAdapter.MyViewHolder holder, int position) {
        final CategoryModel.Data current = dataList.get(position);
        holder.cat_name.setText(current.getName());

        holder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.cat_name.isChecked()) {
                    holder.cat_name.setChecked(true);
                    categories.add(current.getId());
                } else {
                    holder.cat_name.setChecked(false);
                    if (categories.size() > 0)
                        categories.remove((Integer) current.getId());
                }
            }
        });
        Log.d("categories array:", "" + categories);

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getfeeds(categories);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckedTextView cat_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.text_cat_name);
            cat_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
