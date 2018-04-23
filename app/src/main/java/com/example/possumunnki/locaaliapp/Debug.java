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
    /**default debug level*/
    static int defaultLevel = 3;
    /**debug level*/
    private static int DEBUG_LEVEL = defaultLevel;
    /**message will be printed as toast*/
    private static final boolean UI = true;
    /**message will be printed to console*/
    private static final boolean CONSOLE = false;
    /**whether message will be printed as toast text or in console*/
    private static final boolean LOGGING = CONSOLE;
    /**context where the messages will be printed*/
    private static Context context;
    /**duration of the toast text*/
    private static int duration = Toast.LENGTH_SHORT;

    /**
     * Prints on logcat.
     * @param className name of the class
     * @param methodName name of the method
     * @param message message
     * @param level priority of the message
     */
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

    /**
     * Loads debug level from the xml-file
     */
    public static void loadDebug() {
        String debugLevelStr ="5";
        try {
            DEBUG_LEVEL = Integer.parseInt(debugLevelStr);
        } catch (NumberFormatException e) {
            DEBUG_LEVEL = defaultLevel;
            print("Debug", "loadDebug()",
                  "the xml-file does not contain an integer value", 3);
        }
    }

    /**
     * Sets context
     * @param c context
     */
    public static void setContext(Context c) {
        context = c;
    }
}
