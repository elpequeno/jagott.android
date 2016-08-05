package de.huerse.jagott;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andre on 28.10.2014.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    Context mContext;
    int[] mImages;
    String[] mTitelArray;

    CustomArrayAdapter(Context c, String[] titles, int images[])
    {
        super(c,R.layout.navigation_single_row,R.id.listItemText, titles);
        this.mContext = c;
        this.mImages = images;
        this.mTitelArray = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.navigation_single_row, parent, false);

        ImageView listItemImage = (ImageView) rowView.findViewById(R.id.imageView);
        TextView listItemText = (TextView) rowView.findViewById(R.id.listItemText);

        listItemImage.setImageResource(mImages[position]);
        listItemText.setText(mTitelArray[position]);

        return rowView;
    }
}