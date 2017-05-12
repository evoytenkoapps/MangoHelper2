package evoytenkoapps.mangohelper.ru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
//import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    final String LOG_TAG = "ActivityState";
    final int DIALOG_ABOUT= 1;
    // Пройденные уроки
    static SharedPreferences sPrefLessons;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPrefLessons = this.getSharedPreferences("LessonsResults", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Log.d(LOG_TAG, "MainActivity onCreate");

        // Делает картинку с меню в правом верхнем углу
        final ActionBar ab = getSupportActionBar();
        // Рисует меню для навигатион дравера
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            setupDrawerContent(navigationView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null)
        {
            setupViewPager(viewPager);
        }

//        // Круглая кнопка
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view)
//                {
//                    Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                }
//            });

        // Картинки для табов  
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_lessons);
    }

    // Загружаем список пройденных уроков
    public void loadSharedPreferenses()
    {

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//         getMenuInflater().inflate(R.menu.sample_actions, menu);
//        return true;
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        switch (AppCompatDelegate.getDefaultNightMode())
//        {
//            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
//                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_AUTO:
//                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_YES:
//                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
//                break;
//            case AppCompatDelegate.MODE_NIGHT_NO:
//                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
//                break;
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
////            case R.id.menu_night_mode_system:
////                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
////                break;
////            case R.id.menu_night_mode_day:
////                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
////                break;
////            case R.id.menu_night_mode_night:
////                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
////                break;
////            case R.id.menu_night_mode_auto:
////                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
////                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void setNightMode(@AppCompatDelegate.NightMode int nightMode)
//    {
//        AppCompatDelegate.setDefaultNightMode(nightMode);
//
//        if (Build.VERSION.SDK_INT >= 11)
//        {
//            recreate();
//        }
//    }

    //  Отображение пейджера
    private void setupViewPager(ViewPager viewPager)
    {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        // Главный фрагмент
        adapter.addFragment(new MainFragment() , "");
        // Фрагмент с уроками
        adapter.addFragment(new LessonsListFragment(), "");
        viewPager.setAdapter(adapter);
    }

    // Главное меню
    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem)
                {
                    menuItem.setChecked(true);
                    String mail = getResources().getString(R.string.mail) ;
                    Intent i = new Intent(Intent.ACTION_SEND);


                    switch (menuItem.getItemId())
                    {
                            // Сброс уроков
                        case R.id.nav_reset_lessons:
                            // Обнуляем файл с рузультатами уроков
                            sPrefLessons.edit().clear().commit();
                            // Перерисовываем весь писок уроков                    
                            // LessonsListFragment.rv.getAdapter().notifyItemRangeChanged(0, LessonsListFragment.rv.getAdapter().getItemCount());
                            LessonsListFragment.rv.getAdapter().notifyDataSetChanged();
                            // Обнуляем прогресс уроков в главном фрагменте
                            MainFragment.tvLessonProgress.setText(MainFragment.getLessonsProgress());
                            // Обновляем стату
                            MainFragment.tvLessonCount.setText(MainFragment.getLessonsCount());
                            Log.d(LOG_TAG, "Sbros nastroek");
                            break;

                        case R.id.nav_about:
                            showDialog(DIALOG_ABOUT);
                            break;

                        case R.id.nav_send_feedback:
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Предложение по улучшению Mango Helper");
                            i.putExtra(Intent.EXTRA_TEXT   , "Добрый день!\n");
                            try
                            {
                                startActivity(Intent.createChooser(i, "Выбирите почтовый клиент"));
                            }
                            catch (android.content.ActivityNotFoundException ex)
                            {
                                Toast.makeText(MainActivity.this, " Вы не можете отправить почту разработчикам на адресс" + mail + "т.к у вас не установлен почтовый клиент. Воспользуйтесь пожалуйста браузером", Toast.LENGTH_SHORT).show();
                            }
                            break;


                        case R.id.nav_send_error:
                            String version ="unknown";
                            try
                            {
                               version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            }
                            catch (NameNotFoundException e)
                            {
                                Log.e("tag", e.getMessage());
                            }
                            
                            
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Ошибка в Mango Helper "+version);
                            i.putExtra(Intent.EXTRA_TEXT   , "Mango Helper "+version+"\n"+getDeviceName());
                            try
                            {
                                startActivity(Intent.createChooser(i, "Выбирите почтовый клиент"));
                            }
                            catch (android.content.ActivityNotFoundException ex)
                            {
                                Toast.makeText(MainActivity.this, " Вы не можете отправить почту разработчикам на адресс" + mail + "т.к у вас не установлен почтовый клиент. Воспользуйтесь пожалуйста браузером", Toast.LENGTH_SHORT).show();
                            }
                            break;

                    }
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
    }

    static class Adapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm)
        {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        // Получаем конкретный фрагмент
        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        // Количество фрагментов для viewpager (2)
        @Override
        public int getCount()
        {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitles.get(position);
        }
    }


    protected void onStart()
    {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity onStart");
    }

    protected void onResume()
    {
        super.onResume();
//        int position =0;
//        Bundle extras = getIntent().getExtras();
//        // Открываем фрагмент со списком уроков, если из вебактивити пришла 1
//        if (extras != null)
//        {
//            position = extras.getInt("viewpager_position");
//            viewPager.setCurrentItem(position);
//        }

        Log.d(LOG_TAG, "MainActivity onResume");
    }

    protected void onPause()
    {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity onPause");
    }

    protected void onStop()
    {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity onStop");
    }

    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity onDestroy");
    }


// Создаем диалог после завершении урока
    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_ABOUT)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            // заголовок

            String version ="";
            //String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            try
            {
                version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            }
            catch (NameNotFoundException e)
            {
                Log.e("tag", e.getMessage());
            }

            String message = getResources().getString(R.string.about_message);
            adb.setTitle(R.string.about);
//            // Нельзя закрыть диалог
//            adb.setCancelable(false);
//            // сообщение
            adb.setMessage("Версия " + version + "\n" + message);
//            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
//            // кнопка ok
            adb.setPositiveButton("Ok", null);
//            // кнопка список уроков
//            adb.setNegativeButton(R.string.listoflessons, myClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }
    
    // Определяем модель телефона для send_error
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    } 
    
    
}
