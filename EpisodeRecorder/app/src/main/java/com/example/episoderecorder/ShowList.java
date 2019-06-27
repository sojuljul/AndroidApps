package com.example.episoderecorder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ShowList extends ArrayAdapter<Show> {

    private Activity context;
    private List<Show> showList;

    public ShowList(Activity context, List<Show> showList) {
        super(context, R.layout.layout_show_list, showList);
        this.context = context;
        this.showList = showList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_show_list, null, true);

        TextView titleTextView = (TextView) listViewItem.findViewById(R.id.titleTextView);
        TextView currentEpTextView = (TextView) listViewItem.findViewById(R.id.currentEpTextView);
        TextView totalEpTextView = (TextView) listViewItem.findViewById(R.id.totalEpTextView);

        Show show = showList.get(position);

        titleTextView.setText(show.getTitle());
        currentEpTextView.setText(String.valueOf(show.getCurrentEp()));
        totalEpTextView.setText(String.valueOf(show.getTotalEp()));

        return listViewItem;
    }
}
