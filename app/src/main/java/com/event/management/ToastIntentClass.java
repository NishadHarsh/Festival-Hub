package com.event.management;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ToastIntentClass {

    public ToastIntentClass(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public ToastIntentClass(Context context, Class<?> nextClass) {
        Intent intent = new Intent(context, nextClass);
        context.startActivity(intent);
    }

}
