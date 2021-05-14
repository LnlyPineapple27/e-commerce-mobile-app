package com.example.greentea.NotificationModels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscountNotifications implements MixedNotification{
    long time_stamp;
    String message, title;

    public DiscountNotifications(long _time, String _title, String _message){
        time_stamp = _time;
        title = _title;
        message = _message;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getType() {
        return MixedNotification.TYPE_DISCOUNT;
    }

    @Override
    public long getDateLong() {
        return time_stamp;
    }

    @Override
    public String getDateString() {
        try {
            Date currentDate = new Date(time_stamp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
            return dateFormat.format(currentDate);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
