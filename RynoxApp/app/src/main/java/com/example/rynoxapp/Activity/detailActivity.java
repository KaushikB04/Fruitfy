package com.example.rynoxapp.Activity;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.module.LibraryGlideModule;
import com.example.rynoxapp.Domain.ItemsDomain;
import com.example.rynoxapp.Domain.SliderItems;
import com.example.rynoxapp.Fragment.DescriptionFragment;
import com.example.rynoxapp.Fragment.ReviewFragment;
import com.example.rynoxapp.Fragment.SoldFragment;
import com.example.rynoxapp.R;
import com.example.rynoxapp.adpter.SizeAdapter;
import com.example.rynoxapp.adpter.SliderAdapter;
import com.example.rynoxapp.databinding.ActivityDetailBinding;
import com.example.rynoxapp.helper.ManagmentCart;

import java.util.ArrayList;
import java.util.List;

public class detailActivity extends BaseActivity {
    ActivityDetailBinding binding;

    private ItemsDomain object;

    private int nemberOrder = 1;
    private ManagmentCart managmentCart;
    private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        getBundles();
        initBanners();
        initSize();
        setupViewPager();


    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXl");

        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewPageSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewPageSlider));
        binding.viewPageSlider.setClipToPadding(false);
        binding.viewPageSlider.setClipChildren(false);
        binding.viewPageSlider.setOffscreenPageLimit(3);
        binding.viewPageSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void getBundles() {

        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceText.setText("$" + object.getPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.rating.setText(object.getRating() + " Rating");

        binding.addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberinCart(nemberOrder);
                managmentCart.insertFood(object);
            }
        });
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();
        bundle1.putString("description",object.getDescription());
        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Description");
        adapter.addFrag(tab2,"Review");
        adapter.addFrag(tab3,"Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);


    };


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentsList = new ArrayList<>();
        private final List<String> mFragmentsTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        private void addFrag(Fragment fragment,String title){
            mFragmentsList.add(fragment);
            mFragmentsTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentsTitleList.get(position);
        }
    }
}




