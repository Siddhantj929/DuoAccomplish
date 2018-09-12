package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainUserPanelFragment extends Fragment {

    private DBHandler mDBHandler;

    private Callbacks mCallbacks;

    private Toolbar toolbar;

    private ViewPager mViewPager;

    private DrawerLayout drawerLayout;

    private NavigationView mNavigationView;

    private TabLayout mTabLayout;

    private ImageView mUserImageView;

    private ImageView mHeaderImageView;

    RecyclerView recyclerView;

    private AppBarLayout mAppBarLayout;

    private TextView userName;
    private TextView userEmail;

    private TextView userNameText;

    private File mPhotoFile;

    public static MainUserPanelFragment newInstance() {
        return new MainUserPanelFragment();
    }

    public interface Callbacks {
        void setNewActionBar(Toolbar toolbar, String titleText);

        void switchToActivity(Class<? extends Activity> activityToOpen);

        void changeMenuIcon(int MODE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBHandler = new DBHandler();

        mPhotoFile = User.get().getPhotoFile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_user_panel, container, false);

        toolbar = v.findViewById(R.id.toolbar);
        mCallbacks.setNewActionBar(toolbar, null);

        mViewPager = v.findViewById(R.id.tab_ViewPager);

        mViewPager.setAdapter(new PagerAdapter() {

            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == (ConstraintLayout) o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = mLayoutInflater.inflate(R.layout.pager_item, container, false);

                recyclerView = view.findViewById(R.id.recyclerView);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                String[] list = new String[100];

                int i;

                for(i = (position == 1) ? 20 : 50; i < 100; i++) {
                    list[i] = ("String #" + String.valueOf(i + 1));
                }

                MyAdapter myAdapter = new MyAdapter(list);
                recyclerView.setAdapter(myAdapter);

                container.addView(view);

                return view;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "YOU";

                } else {
                    return "Them";
                }
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((ConstraintLayout) object);
            }
        });

        mTabLayout = v.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_you);

        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_them);

        mNavigationView = v.findViewById(R.id.navigationView);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // drawerLayout.closeDrawer(Gravity.START);

                 menuItem.setChecked(true);

                 if (menuItem.getItemId() == R.id.logout_nav) {

                     drawerLayout.closeDrawer(Gravity.START);

                     new AlertDialog.Builder(getActivity())
                             .setTitle("Confirm Action")
                             .setMessage("Are you sure you want to logout?")
                             .setPositiveButton("Yep!", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                    User.get().logout();
                                    mCallbacks.switchToActivity(RegisterActivity.class);
                                 }
                             })
                             .setNegativeButton("Oops, Sorry!", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     // Do nothing.
                                 }
                             })
                             .create()
                             .show();
                 }

                return true;
            }
        });

        mAppBarLayout = v.findViewById(R.id.appBarLayout);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    changeAppBar(1);

                    mCallbacks.changeMenuIcon(1);
                }
                else
                {
                    //Expanded
                    changeAppBar(0);

                    mCallbacks.changeMenuIcon(0);
                }
            }
        });

        drawerLayout = v.findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.welcome, R.string.exit);

        drawerLayout.setDrawerListener(toggle);

        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(getActivity());
        arrowDrawable.setColor(getActivity().getResources().getColor(R.color.secondaryColor));

        toggle.setDrawerArrowDrawable(arrowDrawable);

        toggle.syncState();

        // Inflating the view of navigation header to get access to the views in it.
        View temp = mNavigationView.getHeaderView(0);

        userName = temp.findViewById(R.id.userNameTextView);
        userEmail = temp.findViewById(R.id.userEmailTextView);
        mUserImageView = temp.findViewById(R.id.userNavImageView);

        userNameText = v.findViewById(R.id.userNameText);

        mHeaderImageView = v.findViewById(R.id.headerImageView);

        updateDrawer();

        return v;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public MyViewHolder(LayoutInflater inflater,  ViewGroup parent) {
            super(inflater.inflate(R.layout.my_text_view, parent, false));

            mText = itemView.findViewById(R.id.myText);
        }

        public void changeText(String text) {
            mText.setText("I am " + text);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private String[] mStrings;

        public MyAdapter(String[] db) {
            mStrings = db;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new MyViewHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.changeText(String.valueOf(i));
        }

        @Override
        public int getItemCount() {
            return mStrings.length;
        }
    }

    private void updateDrawer() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {

            mUserImageView.setImageDrawable(getActivity().
                    getResources().getDrawable(R.drawable.camera_placeholder));

            mHeaderImageView.setImageDrawable(getActivity().
                    getResources().getDrawable(R.drawable.camera_placeholder));

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());

            mUserImageView.setImageBitmap(bitmap);
            mHeaderImageView.setImageBitmap(bitmap);
        }

        if (User.get().getName() != null) {
            userName.setText(User.get().getName());
            userNameText.setText(User.get().getName());

        } else {
            userNameText.setVisibility(View.GONE);
        }

        if(User.get().getEmail() != null) {
            userEmail.setText(User.get().getEmail());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    private void changeAppBar(int MODE) {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.welcome, R.string.exit);

        drawerLayout.setDrawerListener(toggle);

        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(getActivity());

        if (MODE == 0) {

            arrowDrawable.setColor(getActivity().getResources().getColor(R.color.primaryColor));
        }

        toggle.setDrawerArrowDrawable(arrowDrawable);

        toggle.syncState();
    }
}
