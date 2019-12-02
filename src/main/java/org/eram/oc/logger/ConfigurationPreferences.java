package org.eram.oc.logger;

import android.content.SharedPreferences;

import org.eram.oc.logger.db.LogLine;

public class ConfigurationPreferences {

    private static  final String spreadsheetIdKey = "SheetID";
    private static  final String rangeKey = "SheetRange";
    private static final String defaultStringValue = "";


    public static void initConfigurations(SharedPreferences sharedPreferences)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(spreadsheetIdKey, defaultStringValue);
        editor.putString(rangeKey, defaultStringValue);
        editor.commit();
    }

    public static void setConfigurations(SharedPreferences sharedPreferences, String sheetSpread, String range)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(spreadsheetIdKey, sheetSpread);
        editor.putString(rangeKey, range);
        editor.commit();
    }

    public static String getSpreadID(SharedPreferences sharedPreferences)
    {

        return sharedPreferences.getString(spreadsheetIdKey, defaultStringValue);
    }

    public static String getRange(SharedPreferences sharedPreferences)
    {

        return sharedPreferences.getString(rangeKey, defaultStringValue);
    }

}
