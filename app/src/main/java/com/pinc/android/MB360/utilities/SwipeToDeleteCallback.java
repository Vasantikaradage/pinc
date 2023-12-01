package com.pinc.android.MB360.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.pinc.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapter;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

import java.util.List;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;
    private List<DependantHelperModel> dependantData;

    public SwipeToDeleteCallback(Context context, int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, List<DependantHelperModel> dependantData) {
        super(dragDirs, swipeDirs);
        this.dependantData = dependantData;
        this.listener = listener;
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        DependantHelperModel dependant = dependantData.get(position);
        if (dependant.isCanDelete()) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else return 0;

    }

    @Override
    public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged( RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.llChild1;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver( Canvas c,  RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.llChild1;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);
    }

    @Override
    public void clearView( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.llChild1;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw( Canvas c,  RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.llChild1;
        float newDX = dX;

      /*  if(newDX <= -350f) {
            newDX = -350f;
        }*/


        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, newDX, dY,
                ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);

        drawBackground(viewHolder, dX, actionState);
    }

    private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
        final View backgroundView = ((DependantDetailsAdapter.DependantDetailsViewHolder) viewHolder).binding.completeView;

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            backgroundView.setLeft((int) Math.max(dX, 0));
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
