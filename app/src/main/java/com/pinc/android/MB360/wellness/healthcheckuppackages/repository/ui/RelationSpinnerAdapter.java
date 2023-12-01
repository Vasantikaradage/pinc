package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Relation;

import java.util.List;

public class RelationSpinnerAdapter extends ArrayAdapter<Relation> {

    public RelationSpinnerAdapter(Context context, List<Relation> relationList) {
        super(context, 0, relationList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        //custom View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_items, parent, false);
        }

        TextView name = convertView.findViewById(R.id.nameTV);
        Relation relation = getItem(position);

        // when current item is not null
        if (relation != null) {
            name.setText(relation.getRelationName());
        }

        return convertView;
    }
}
