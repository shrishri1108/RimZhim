package com.rimzhim.MainMenu;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.ViewPagerAdapter;
import com.rimzhim.Fragments.ChatFragment;
import com.rimzhim.Fragments.ContestsFragment;
import com.rimzhim.Fragments.HomeFragment;
import com.rimzhim.Fragments.ProfileFragment;
import com.rimzhim.Fragments.WalletFragment;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.OnBackPressListener;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PermissionUtils;


public class MainMenuFragment extends RootFragment {
    public static TabLayout tabLayout;
    protected CustomViewPager pager;
    private ViewPagerAdapter adapter;
    Context context;
    PermissionUtils takePermissionUtils;
    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        Functions.blackStatusBar(getActivity());

        context = view.getContext();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(4);
        pager.setPagingEnabled(false);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    int selected_postion = tabLayout.getSelectedTabPosition();
                    if (selected_postion == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Functions.blackStatusBar(getActivity());
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Functions.whiteStatusBar(getActivity());
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Functions.whiteStatusBar(getActivity());
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        TextView title1 = view1.findViewById(R.id.text);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_pink));
        title1.setText("Home");
        title1.setTextColor(context.getResources().getColor(R.color.pink));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        TextView title2 = view2.findViewById(R.id.text);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.context_icon));
        title2.setText("Contests");
        title2.setTextColor(context.getResources().getColor(R.color.white));
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView3 = view3.findViewById(R.id.image);
        TextView title3 = view3.findViewById(R.id.text);
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
        title3.setText("My Profile");
        title3.setTextColor(context.getResources().getColor(R.color.white));
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView4 = view4.findViewById(R.id.image);
        TextView title4 = view4.findViewById(R.id.text);
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon));
        title4.setText("Chat");
        title4.setTextColor(context.getResources().getColor(R.color.white));
        tabLayout.getTabAt(3).setCustomView(view4);

        View view5 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView5 = view5.findViewById(R.id.image);
        TextView title5 = view5.findViewById(R.id.text);
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.wallet_icon));
        title5.setText("Wallet");
        title5.setTextColor(context.getResources().getColor(R.color.white));
        tabLayout.getTabAt(4).setCustomView(view5);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        Functions.blackStatusBar(getActivity());
                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_pink));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.pink));
                        break;

                    case 1:
                        Functions.whiteStatusBar(getActivity());
                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.context_icon_pink));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor( R.color.pink));
                        break;


                    case 2:
                        Functions.whiteStatusBar(getActivity());
                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_pink));
                        image.setColorFilter(ContextCompat.getColor(context,  R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor( R.color.pink));
                        break;
                    case 3:
                        Functions.whiteStatusBar(getActivity());
                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_pink));
                        image.setColorFilter(ContextCompat.getColor(context,  R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor( R.color.pink));
                        break;

                    case 4:
                        Functions.whiteStatusBar(getActivity());
                         onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.wallet_icon_pink));
                        image.setColorFilter(ContextCompat.getColor(context,  R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor( R.color.pink));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.context_icon));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;

                    case 2:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.wallet_icon));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });



    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new HomeFragment();
                    break;

                case 1:
                    result = new ContestsFragment();
                    break;

                case 2:
                    result = new ProfileFragment();
                    break;

                case 3:
                    result = new ChatFragment();
                    break;

                case 4:
                    result = new WalletFragment();
                    break;

                default:
                    result = new HomeFragment();
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


    }

    public void onotherTabClick() {
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        View view1 = tab1.getCustomView();
        TextView tex1 = view1.findViewById(R.id.text);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex1.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        View view2 = tab2.getCustomView();
        TextView tex2 = view2.findViewById(R.id.text);
        ImageView imageView2 = view2.findViewById(R.id.image);
        imageView2.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex2.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab2.setCustomView(view2);



        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        View view3 = tab3.getCustomView();
        ImageView imageView3 = view3.findViewById(R.id.image);
        TextView tex3 = view3.findViewById(R.id.text);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex3.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab3.setCustomView(view3);


        TabLayout.Tab tab4 = tabLayout.getTabAt(3);
        View view4 = tab4.getCustomView();
        ImageView imageView4 = view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4 = view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab4.setCustomView(view4);

        TabLayout.Tab tab5 = tabLayout.getTabAt(4);
        View view5 = tab5.getCustomView();
        ImageView imageView5 = view5.findViewById(R.id.image);
        imageView5.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex5 = view4.findViewById(R.id.text);
        tex5.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab5.setCustomView(view5);

        tabLayout.setBackgroundColor(getResources().getColor(R.color.black));

       /* if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.black));
        }*/

    }







    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }



}