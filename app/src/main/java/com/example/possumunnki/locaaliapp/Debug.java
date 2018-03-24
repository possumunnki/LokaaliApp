package com.example.possumunnki.locaaliapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Makes easy to debug.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class Debug {
    static int defaultLevel = 3;
    private static int DEBUG_LEVEL = defaultLevel;
    private static final boolean UI = true;
    private static final boolean CONSOLE = false;
    private static final boolean LOGGING = CONSOLE;
    private static Context context;
    private static int duration = Toast.LENGTH_SHORT;

    public static void print(String className,
                             String methodName,
                             String message,
                             int level) {

        if(LOGGING == UI && context != null) {
            if(BuildConfig.DEBUG && level <= DEBUG_LEVEL) {
                CharSequence text = className + ": " + methodName + ", " + message;
                Toast.makeText(context, text, duration).show();
            }

        } else if(LOGGING == CONSOLE){
            if (BuildConfig.DEBUG && level <= DEBUG_LEVEL) {
                Log.d(className,methodName + ", " + message);
            }
        }

    }

    public static void loadDebug(Context host) {
        String debugLevelStr ="5";
        try {
            DEBUG_LEVEL = Integer.parseInt(debugLevelStr);
        } catch (NumberFormatException e) {
            DEBUG_LEVEL = defaultLevel;
            print("Debug", "loadDebug()",
                  "the xml-file does not contain an integer value", 3);
        }
    }

    public static void setContext(Context c) {
        context = c;
    }
}
