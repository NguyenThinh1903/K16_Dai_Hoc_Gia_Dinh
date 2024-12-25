package com.example.viewandfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private BottomNavigationView mBottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mViewPager = findViewById(R.id.pager_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        // Khởi tạo adapter với `FragmentStateAdapter`
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(adapter);

        // Xử lý sự kiện khi trang thay đổi
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.menu_tab1);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.menu_tab2);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.menu_tab3);
                        break;
                    case 3:
                        mBottomNavigationView.setSelectedItemId(R.id.menu_tab4);
                        break;
                }
            }
        });

        // Xử lý sự kiện khi người dùng chọn lại tab
        mBottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_tab1) {
                    mViewPager.setCurrentItem(0);
                } else if (itemId == R.id.menu_tab2) {
                    mViewPager.setCurrentItem(1);
                } else if (itemId == R.id.menu_tab3) {
                    mViewPager.setCurrentItem(2);
                } else if (itemId == R.id.menu_tab4) {
                    mViewPager.setCurrentItem(3);
                }

                return true;
            }
        });

    }
}


