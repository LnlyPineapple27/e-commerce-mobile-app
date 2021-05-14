package com.example.greentea.NotificationModels;

public interface MixedNotification {
    int TYPE_DISCOUNT = 100;
    int TYPE_ORDER = 101;
    int getType();
    long getDateLong();
    String getDateString();
    String getMessage();
    String getTitle();
}
