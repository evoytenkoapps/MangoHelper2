// Первый фрагмент
// Отображает прогресс прохождения и кнопку старт

package evoytenkoapps.mangohelper.ru;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.app.Activity;
import android.widget.ImageButton;
import android.view.View.*;
import android.content.Context;
import android.content.Intent;



public class MainFragment extends Fragment
{
    final String LOG_TAG = "myLogs";

    // % Прохождения урока
    public static TextView tvLessonProgress ;
    // Кол-во пройденных/ не пройденных уроков
    public static TextView tvLessonCount;

//        static MainFragment newInstance(int page) {
//            MainFragment MainFragment = new MainFragment();
//            Bundle arguments = new Bundle();
//            arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
//            MainFragment.setArguments(arguments);
//            return MainFragment;
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
//
//            Random rnd = new Random();
//            backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, null);
        tvLessonProgress = (TextView) view.findViewById(R.id.lesson_progress);
        tvLessonCount = (TextView) view.findViewById(R.id.lesson_count);
        //loadSharedPreferenses();
        //Кнопка продолжить уроки
        ImageButton btnResume = (ImageButton) view.findViewById(R.id.btn_continue);
        btnResume.setOnClickListener(new OnClickListener(){
                @Override
                //Продолжаем прохождение уроков
                public void onClick(View p1)
                {
                    Context context = p1.getContext();
                    Intent intent = new Intent(context, WebActivity.class);
                    // Передаем вебактивити номер урока и запускаем ее
                    intent.putExtra(WebActivity.EXTRA_NAME, 0);
                    context.startActivity(intent);
                    
                }
            });

        Log.d(LOG_TAG, "Fragment1 onCreateView");
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tvLessonProgress.setText(getLessonsProgress());
        tvLessonCount.setText(getLessonsCount());
        Log.d(LOG_TAG, "Fragment1 onResume");

    }

    // Подсчет процента прохождения уроков 
    public static String getLessonsProgress()
    {
        try
        {
            // в LessonsListFragment хранятся только пройденные уроки
            float count = MainActivity.sPrefLessons.getAll().size();
            float totalCount = Lessons.sLessonsStrings.length;
            float result = count / (totalCount / 100);
            // Убираем цифры после запятой
            return String.format("%.0f", result) + "%";
        }
        catch (ArithmeticException ex)
        {
            return "0%";
        }
    }

    // Отображение уроков пройденно/ всего уроков
    public static String getLessonsCount()
    {
        String count = MainActivity.sPrefLessons.getAll().size() + "";
        String totalCount = Lessons.sLessonsStrings.length + "";
        String result = count + "/" + totalCount;
        return result;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onCreate");
    }



    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onActivityCreated");
    }

    public void onStart()
    {
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 onStart");
    }


    public void onPause()
    {
        super.onPause();
        Log.d(LOG_TAG, "Fragment1 onPause");
    }

    public void onStop()
    {
        super.onStop();
        Log.d(LOG_TAG, "Fragment1 onStop");
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        Log.d(LOG_TAG, "Fragment1 onDestroyView");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TAG, "Fragment1 onDestroy");
    }

    public void onDetach()
    {
        super.onDetach();
        Log.d(LOG_TAG, "Fragment1 onDetach");
    }


}
