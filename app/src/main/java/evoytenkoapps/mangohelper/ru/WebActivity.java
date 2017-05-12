//Отображает урок в webview
// Урок это html страничка
package evoytenkoapps.mangohelper.ru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
//import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import android.widget.RadioButton;
//import android.util.Log;
//import android.os.Environment;
import android.webkit.WebSettings;
import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.design.widget.BottomSheetBehavior;
import android.support.annotation.NonNull;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
//import android.view.View.*;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.support.v7.app.AlertDialog;
//import android.support.v7.appcompat.R;
//import android.support.v7.widget.Toolbar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Arrays;
import java.util.Random;

import android.widget.TextView;
//// progress webview
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.webkit.WebChromeClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.webkit.WebChromeClient;
//import masq.mh.ru.R;

public class WebActivity extends AppCompatActivity
{
    public static final String EXTRA_NAME = "Lesson number";
    String LessonUrl = "file:///android_asset/Site/Lessons/lsn1/lsn1_1.html";
    // ссылка на контекст для MakeToast
    Context activity;
    String TAG = "WebActivity Logs";
    final int DIALOG_EXIT = 1;
    final int DIALOG_DEVICE_IP = 2;

    private BottomSheetBehavior bottomSheetBehavior;
    private WebView myWebView;
    // Номер урока, передается в вебактивити из мейна, и сохраняется при завершении урока
    private int LessonNumber;
    private String ToastText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(masq.mh.ru.R.layout.activity_web);
        setContentView(evoytenkoapps.mangohelper.ru.R.layout.activity_web);
        // ссылка на контекст для MakeToast
        activity = this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web);
//        setSupportActionBar(toolbar);


        // При рефреше вебвью показывает тост с подсказкой. вместо загрузки страницы
//        final SwipeRefreshLayout mySwipeWebView = (SwipeRefreshLayout) findViewById(masq.mh.ru.R.id.swip_web);
//        mySwipeWebView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//                @Override
//                public void onRefresh()
//                {
//                    MakeToast(getLessonsToastHelp());
//
//                    //mySwipeWebView.stopNestedScroll();
//                }
//            });


        // Берем переданный номер урока
        Intent intent = getIntent();
        LessonNumber = getLessonNumber(intent.getIntExtra(EXTRA_NAME, 0));
        // Задаем титл номер урока
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Урок №" + LessonNumber);
        LessonUrl = "file:///android_asset/Site/Lessons/lsn" + LessonNumber + "/lsn" + LessonNumber + "_1.html";

        myWebView = (WebView) findViewById(R.id.webview);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        // Круглая кнопка обработчик
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final View llBottomSheet = findViewById(R.id.bottom_sheet);

        // Делаем невидимыми флоатбаттон и боттомшит, они буту отображаться только после полной зашрузки урока. Иначе будет не красиво
        fab.setVisibility(FloatingActionButton.INVISIBLE);
        llBottomSheet.setVisibility(View.INVISIBLE);

        // Что бы при повороте экрана показывалась текущая страница, а не стартовая
        // В онкреате тот же бандл что и в онрестореинстанс
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getString("Url") != null)
            {
                LessonUrl = savedInstanceState.getString("Url");
            }
        }
        myWebView.setWebChromeClient(new WebChromeClient()
        {
        });
        myWebView.setWebViewClient(new WebViewClient()
        {
            // Иначе будет также вызываться officesuite при открытии урока
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {

            }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon)
//                {
//                    super.onPageStarted(view, url, favicon);
//                }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);

                // Удаляем прогресс
                ((RelativeLayout) findViewById(R.id.activitywebRelativeLayout1)).setVisibility(RelativeLayout.INVISIBLE);
                fab.setVisibility(FloatingActionButton.VISIBLE);
                llBottomSheet.setVisibility(View.VISIBLE);


                // Toast toast = Toast.makeText(WebActivity.this, getLessonsToastHelp(), Toast.LENGTH_LONG);

                //toast.show();
                MakeToast(getLessonsToastHelp());

            }
        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        //myWebView.addJavascriptInterface(jsInterface, "");
        // Обработка javascript из html
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.getSettings().setAllowFileAccess(true);

        // Делаем зум  в браузере минимальным по умолчанию
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadUrl(LessonUrl);
//        // Обработка нажатий по ссылкам в вебвью
//        myWebView.setOnTouchListener(new OnTouchListener(){
//                @Override
//                public boolean onTouch(View p1, MotionEvent p2)
//                {
//                    WebView.HitTestResult hr = ((WebView)p1).getHitTestResult();
//                    Log.i(TAG, "getExtra = " + hr.getExtra() + "\t\t Type=" + hr.getType());
//                    return false;                  
//                }            
//            });
//

//        myWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                }
//            }
//        });

        // Задаем тект подсказки
        TextView tvBottomSheet = (TextView) findViewById(R.id.bottomsheetTextView1);
        tvBottomSheet.setText(getLessonMainHelp());
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // По умолчанию ботом шит закрыт.
        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
            }

            @Override
            // Чтобы фаб исчезал когда увеличиваешь боттомбар
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
                //fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        // Клик на фаб показывает/ скрывает подсказки
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Показываем Toast с текстом последнего Toast при нажатии на fab
                MakeToast(ToastText);

                // Показывает / скрывает большую подсказку при нажании на fab
//                    if (bottomSheetBehavior.getState() == 3)
//                    {
//                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
//                    }
//                    else
//                    {
//                        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
//                    }
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    //Создает текст из Lessons для основной подсказки
    String getLessonMainHelp()
    {
        String result = "Инструкция " + "\n" + "Урок №" + LessonNumber + ".\n";
        try
        {
            // Считаем общее количество строк подсказок данного урока и возвращаем их
            for (int i = 1; i < Lessons.sLessonsStrings[LessonNumber - 1].length; i++)
            {
                String stage = " " + LessonNumber + "." + i + ". ";
                result = result + stage + Lessons.sLessonsStrings[LessonNumber - 1][i] + "\n";
            }
        } catch (ArrayIndexOutOfBoundsException ex)
        {
            result = "";
        }
        return result;
    }

    ;


    //Ищет текст из Lessons для текущего Toast
    String getLessonsToastHelp()
    {
        String result = "";
        // String u = myWebView.getUrl();
        Uri uri = Uri.parse(myWebView.getUrl());
        String ur = uri.getLastPathSegment();
        // Определяем этап урока
        String stage = ur.substring(ur.lastIndexOf("_") + 1, ur.lastIndexOf("."));
        int n = Integer.parseInt(stage);
        try
        {
            result = LessonNumber + "." + n + ". " + Lessons.sLessonsStrings[LessonNumber - 1][n];
        } catch (ArrayIndexOutOfBoundsException ex)
        {
            result = "";
        }

        return result;
    }

    ;

    // Ищет первый не пройденный урок.
    // Если все уроки пройденны возвращает рандом
    int getLessonNumber(int extra)
    {
        int result = 0;
        // Если пришел 0 значит нужно вернуть первый не пройденный урок
        if (extra == 0)
        {
            // Ищем его
            int key = 0;
            int size = MainActivity.sPrefLessons.getAll().size();
            // номера пройденных уроков
            int keys[] = new int[size];
            int i = 0;
            Map<String, ?> allEntries = MainActivity.sPrefLessons.getAll();
            // Определяем пройденные уроки
            for (Map.Entry<String, ?> entry : allEntries.entrySet())
            {
                String k = entry.getKey();
                try
                {
                    key = Integer.parseInt(k);
                    keys[i] = key;
                    i++;
                } catch (NumberFormatException nfe)
                {
                    result = 1;
                    break;
                }
            }
            Arrays.sort(keys);
            // Определили. Ищем урок
            // Все количество уроков
            int count = Lessons.sLessonsStrings.length;
            // Если пройденны не все уроки
            if (keys.length != count)
            {
                // Ищем меньший не пройденный
                int j = 0;
                for (; j < keys.length; j++)
                {
                    // Если номер не равет номеру из пройденных уроков, то этот номер искомый урок
                    if (j + 1 != keys[j])
                    {
                        result = j + 1;
                        return result;
                    }
                }
                result = j + 1;
            }
            // Пройденны все уроки, возвращаем рандомный урок
            else
            {
                Random ran = new Random();
                int r = ran.nextInt(count) + 1;
                result = r;
            }
        }
        // Если на вход пришел не 0, делаем это номером урока
        else
        {
            result = extra;
        }
        return result;
    }

    //Создаем меню ( переход по HTML страницам )
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }

    // Обработчик нажатий меню ( переход по HTML страницам )
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.web_back:
                if (myWebView.canGoBack())
                {
                    myWebView.goBack();
                }
                return true;

            case R.id.web_forward:
                if (myWebView.canGoForward())
                {
                    myWebView.goForward();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    // Переход на предыдущую страницу, при нажатии кнопки назад для браузера
//    @Override
//    public void onBackPressed()
//    {
//        if (myWebView.canGoBack())
//        {
//            myWebView.goBack();
//        }
//        else
//        {
//            super.onBackPressed();
//        }
//    }

    // Отрабатывает нажатия в браузере и передает их в webviewactiviy
    public class JavaScriptInterface
    {
        Context mContext;
        JavaScriptInterface(Context c)
        {
            this.mContext = c;
        }

        // Отображение подсказки для текущего этапа урока
        @JavascriptInterface
        public void showToastFromHTML(int n)
        {
            // n - номер этапа в рамках урока
            String str = LessonNumber + "." + n + ". " + Lessons.sLessonsStrings[LessonNumber - 1][n];
            MakeToast(str);
        }

        // Диалог после завершении урока
        @JavascriptInterface
        public void showDialogFromHTML()
        {
            // Сохраняем что урок пройден
            saveLesson();
            // Диалог без картинок по умолчанию
            showDialog(DIALOG_EXIT);
        }



        // Отображение диалога что нужно узнать IP оборудования и как это сделать.
        @JavascriptInterface
        public void showDialogSetupDeviceFromHTML()
        {
            showDialog(DIALOG_DEVICE_IP);
        }
    }

    // Сохраняем что урок пройден
    void saveLesson()
    {
        SharedPreferences sPref = getSharedPreferences("LessonsResults", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        // 1 урок пройден
        ed.putString(LessonNumber + "", "1");
        ed.commit();
    }


    // Создаем диалог после завершении урока
    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_EXIT)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            String t1 = "Поздравляю! ";

            // Последний элемент текущего урока в Lessons.sLessonsStrings
            String t2 = "";

            try
            {
                // Последняя строка в массиве данного урока
                int end = Lessons.sLessonsStrings[LessonNumber - 1].length - 1;
                t2 = Lessons.sLessonsStrings[LessonNumber - 1][end]+" ";
            } catch (ArrayIndexOutOfBoundsException ex)
            {
                t2 = "";
            }
            String t3 = "Вы успешно прошли урок №" + LessonNumber + ". ";

            adb.setMessage(t1 + t2 + t3);
            // Нельзя закрыть диалог
            adb.setCancelable(false);
            // сообщение
            //adb.setMessage(R.string.save_data);
            // иконка
            //adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка след. урок
            adb.setPositiveButton(R.string.nextlesson, myClickListener);
            // кнопка список уроков
            adb.setNegativeButton(R.string.listoflessons, myClickListener);

            return adb.create();
        }

        if (id == DIALOG_DEVICE_IP)
        {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            String t = "5.6 " + Lessons.sLessonsStrings[4][6];
            int end = Lessons.sLessonsStrings[LessonNumber - 1].length - 1;
            adb.setMessage(t);
            // Нельзя закрыть диалог
            adb.setCancelable(false);
            // кнопка след. урок
            adb.setPositiveButton("Ok", new Dialog.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    switch (which)
                    {
                        // Выбор след. урок
                        case Dialog.BUTTON_POSITIVE:
                            //
                            dialog.dismiss();
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    final WebView webView = (WebView) findViewById(R.id.webview);
                                    myWebView.loadUrl("file:///android_asset/Site/Lessons/lsn5/lsn5_7.html");
                                }
                            });

                            break;
                    }
                }
            });
            return adb.create();
        }

        return super.onCreateDialog(id);
    }


    //Нажатия в диалоге при завершении урока
    Dialog.OnClickListener myClickListener = new Dialog.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                // Выбор след. урок
                case Dialog.BUTTON_POSITIVE:
                    // Вызываем новое вебактивиты
                    // Передаем ей номер урока
                    Intent intent = new Intent(WebActivity.this, WebActivity.class);
                    // Передаем вебвью номер урока и запускаем ее
                    intent.putExtra(WebActivity.EXTRA_NAME, getLessonNumber(0));
                    startActivity(intent);
                    // Удаляем текущее активити
                    finish();
                    break;

                // Возврат в меню
                case Dialog.BUTTON_NEGATIVE:
                    WebActivity.this.finish();
                    break;
            }
        }
    };

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        String url = myWebView.getUrl();
        //Чтобы при повороте экрана загружалась текущая страница, а не первая
        outState.putString("Url", url);
        Log.d(TAG, "onSaveInstanceState= " + url);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //    LessonUrl = savedInstanceState.getString("Url");
        Log.d(TAG, "onRestoreInstanceState =" + LessonUrl);
    }

    // Создаем собственный тоаст, с собственным дизайном
    void MakeToast(String strToast)
    {
        //   new CustomToast(activity, strToast).show();

        // Сохраняем текст подсказки , чтобы он отображался при нажати на fab
        ToastText = strToast;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        //this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast, null);
        builder.setView(layout, 0, 0, 0, 0);
        builder.setInverseBackgroundForced(true);
        TextView tv = (TextView) layout.findViewById(R.id.toastText);
        tv.setText(strToast);
        tv.setShadowLayer(0, 0, 0, 0);

        // builder.setMessage(strToast);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        //dialog.setView(layout, 0, 0, 0, 0);
        //dialog.setContentView(layout);
        // dialog.setContentView(layout);
        //dialog.getWindow().setGravity(Gravity.TOP);
        //dialog.getWindow().setSoftInputMode( android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //dialog.setContentView(layout);
        dialog.show();
        dialog.getWindow().getAttributes().verticalMargin = 0.2F;

        layout.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View p1, MotionEvent p2)
            {
                // TODO: Implement this method
                dialog.dismiss();
                return false;
            }


        });

        // Удоляет темный фон диалога
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        // Задам стиль тоста
//        View view;
//        TextView text;
//        final Toast toast = Toast.makeText(WebActivity.this, strToast, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        view = toast.getView();
//        text = (TextView) view.findViewById(android.R.id.message);
//        text.setTextColor(getResources().getColor(R.color.white));
//        text.setShadowLayer(0, 0, 0, 0);
//        view.setBackgroundResource(R.color.blue);
//
//        // Длинный текст должен показываться 7 сек.
//        if (strToast.length() > 900)
//        {
//            new CountDownTimer(4000, 1000)
//            {
//                public void onTick(long millisUntilFinished)
//                {
//                    toast.show();}
//                public void onFinish()
//                {
//                    toast.show();}
//            }.start();
//        }
//        toast.show();
    }


    //Диалог с картинками
//    private void displayDialogImg(){
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View promptView = layoutInflater.inflate(R.layout.dialog_finish, null);
//        final AlertDialog.Builder alertD = new AlertDialog.Builder(this);
//        alertD.setView(promptView);
//        alertD.show();
//        ImageButton backButton = (ImageButton)promptView.findViewById(R.id.img_back);
//        ImageButton btnNext = (ImageButton)promptView.findViewById(R.id.img_next);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//finish();
//                }
//            });
//        btnNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//
//    }


}
