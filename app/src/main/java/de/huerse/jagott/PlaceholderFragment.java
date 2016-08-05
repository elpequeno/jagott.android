package de.huerse.jagott;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends android.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int selectedView = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(selectedView)
        {
            case 1:
                rootView = inflater.inflate(R.layout.ja_gott_heute, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.archiv_list, container, false);
                break;
            case 21:
                rootView = inflater.inflate(R.layout.ja_gott_heute, container, false);
                break;
            case 22:
                rootView = inflater.inflate(R.layout.ja_gott_favorite_text, container, false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.kontakt, container, false);
                break;
            case 4:
                rootView = inflater.inflate(R.layout.alarm, container, false);
                break;
            case 5:
                rootView = inflater.inflate(R.layout.favorite_list, container, false);
                break;
            default:
                rootView = inflater.inflate(R.layout.ja_gott_heute, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((JaGottMain) activity).onSectionAttached(
        //        getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
