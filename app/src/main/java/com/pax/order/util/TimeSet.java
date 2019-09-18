package com.pax.order.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Terry on 10/13/2017.
 */

public class TimeSet {

    private String timeZone = null;
    private String date_time = null;
    private String gmt_time = null;



    private String local_date = null;



    private String local_time = null;

    public TimeSet(){
        calculateTime();
    }


    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getDateTime() {
        return date_time;
    }

    public void setDateTime(String date_time) {
        this.date_time = date_time;
    }

    public String getGMTTime() {
        return gmt_time;
    }

    public String getLocalDate() {
        return local_date;
    }

    public String getLocalTime() {
        return local_time;
    }



    public void calculateTime(){

//        Calendar cal = Calendar.getInstance();
//        TimeZone tz = cal.getTimeZone();
//        timeZone = tz.getDisplayName();
//        Log.d("Time zone","="+timeZone);
        Calendar calendar = Calendar.getInstance();
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("z", Locale.getDefault());


        // Time zone
        TimeZone tz = calendar.getTimeZone();
        String full_time_zone = tz.getDisplayName();
        timeZone = getFirstLetters(full_time_zone);
        String localTime = date.format(currentLocalTime);
        System.out.println("Get the local time with time zone:" + localTime);
        System.out.println("Get the local time with timeZone:" + timeZone);

        // date time
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        date_time = df.format(calendar.getTime());
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        gmt_time = df.format(new Date());
        System.out.println(gmt_time);
        System.out.println("Get the local formattedDate:" + date_time);

        // local date and local time
        local_date = date_time.substring(0, 8);
        local_time = date_time.substring(8);

    }




    public String getFirstLetters(String text)
    {
        String firstLetters = "";
        text = text.replaceAll("[.,]", ""); // Replace dots, etc (optional)
        for(String s : text.split(" "))
        {
            firstLetters += s.charAt(0);
        }
        return firstLetters;
    }


}
