package evoytenkoapps.mangohelper.ru;

import android.app.Dialog;
import android.content.Context;
//import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.view.WindowManager;


public class CustomToast extends Dialog {
    public CustomToast(Context context, String text) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.
                        LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.toast, null);
        TextView tv = (TextView) layout.findViewById(R.id.toastText);
        tv.setText(text);
        tv.setShadowLayer(0,0,0,0);
        setContentView(layout);
        show();
        setCanceledOnTouchOutside(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });

        setCancelable(true);
        Window window = getWindow();
        // Чтобы подсказка показывалась в нижней части экрана
        window.getAttributes().verticalMargin = 0.2F;
        // Удоляет темный фон на заднем плане диалога
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
