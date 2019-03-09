package nl.klokhet.app.flow.main;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.flow.main.tabs.ListFragment;
import nl.klokhet.app.flow.main.tabs.ScannerFragment;
import nl.klokhet.app.flow.main.tabs.ScannerFragmentGoogleVisionFragment;
import nl.klokhet.app.flow.main.tabs.ScannerFragmentQReader;
import nl.klokhet.app.flow.main.tabs.ScannerFragmentZX;
import nl.klokhet.app.flow.main.tabs.SettingsMainFragment;
import nl.klokhet.app.flow.webview.WebActivity;

public class FragmentsViewPagerAdapter extends FragmentPagerAdapter {

    Typeface typeface;
    private ArrayList<Fragment> tabFragments = new ArrayList<>();
    private Context context;
    private String tabTitles[] = new String[] { " Scan QR", " Checklist", " Settings" };
    private List<Integer> mFragmentIconList = new ArrayList<>();
    {
        initTabFragments();
    }

    public FragmentsViewPagerAdapter(FragmentManager fragmentManager, Typeface typeface, Context context) {
        super(fragmentManager);
        this.typeface=typeface;
        this.context=context;
        mFragmentIconList.add(R.drawable.ic_qr_code_green);
        mFragmentIconList.add(R.drawable.ic_list_grey);
        mFragmentIconList.add(R.drawable.ic_settings_green);
    }

    private void initTabFragments() {
        tabFragments.add(ScannerFragmentGoogleVisionFragment.newInstance());
        tabFragments.add(ListFragment.newInstance());
        tabFragments.add(SettingsMainFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        int[] imageResId = {
//                R.drawable.ic_list_green, R.drawable.ic_list_green, R.drawable.ic_list_green
//        };
//        Drawable image = App.getInstance().getResources().getDrawable(imageResId[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(new CustomTypefaceSpan("", typeface), 0, sb.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return null;
    }
    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setTypeface(typeface);
        tabTextView.setText(tabTitles[position]);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(tabTitles[position]);
        tabTextView.setTypeface(typeface);
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.warmGrey));
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.greenblue), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}