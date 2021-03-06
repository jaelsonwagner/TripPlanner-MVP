package com.hlabexamples.tripplanner.modules.home.browse;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hlabexamples.commonmvp.base.mvp.callback.IFirebaseCallbackListener;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.RowItemAttractionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created in BindingConstraintMVP-Demo on 11/01/17.
 */

public class BrowseAttractionAdapter extends RecyclerView.Adapter<BrowseAttractionAdapter.BrowseItemHolder> {

    private List<String> ids = new ArrayList<>();

    private List<TripModel> items = new ArrayList<>();
    private LayoutInflater inflater;
    private IFirebaseCallbackListener<TripModel> iFirebaseCallbackListener;

    private BrowseAttractionFragment fragment;

    BrowseAttractionAdapter(BrowseAttractionFragment fragment) {
        this.fragment = fragment;
        inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.iFirebaseCallbackListener = new IFirebaseCallbackListener<TripModel>() {
            @Override
            public void childAdded(TripModel trip) {
                ids.add(trip.getId());
                items.add(trip);
                notifyItemInserted(items.size() - 1);
            }

            @Override
            public void childChanged(TripModel trip) {
                String id = trip.getId();

                int index = ids.indexOf(id);
                if (index > -1) {
                    items.set(index, trip);
                    notifyItemChanged(index);
                }
            }

            @Override
            public void childRemoved(TripModel trip) {
                String id = trip.getId();

                int index = ids.indexOf(id);
                if (index > -1) {
                    ids.remove(index);
                    items.remove(index);
                    notifyItemRemoved(index);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
    }

    @Override
    public BrowseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowItemAttractionBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_item_attraction, parent, false);
        return new BrowseItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(BrowseItemHolder holder, int position) {
        TripModel model = items.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    IFirebaseCallbackListener<TripModel> getiFirebaseCallbackListener() {
        return iFirebaseCallbackListener;
    }

    public List<TripModel> getItems() {
        return items;
    }

    public interface BrowseItemListener {
        void onClick();

        void onDeleteClick();

        void onFavoriteClick();
        //Other listener methods
    }

    class BrowseItemHolder extends RecyclerView.ViewHolder implements BrowseItemListener {

        private RowItemAttractionBinding binding;

        BrowseItemHolder(RowItemAttractionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TripModel item) {
            binding.setPresenter(this);
            binding.setData(item);
        }

        @Override
        public void onClick() {
            fragment.showDetail(items.get(getAdapterPosition()));
        }

        @Override
        public void onDeleteClick() {
            fragment.deleteTrip(items.get(getAdapterPosition()).getId());
        }

        @Override
        public void onFavoriteClick() {
            TripModel model = items.get(getAdapterPosition());
            fragment.changeFavorite(model.getId(), model.isFavourite() == 0 ? 1 : 0);
        }
    }
}
