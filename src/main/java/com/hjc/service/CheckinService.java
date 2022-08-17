package com.hjc.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface CheckinService {
    String validCanCheckIn(int userId, String date);
    void checkin(HashMap param);
    HashMap searchTodayCheckin(int userId);
    long searchCheckinDays(int userId);
    ArrayList<HashMap> searchWeekCheckin(HashMap param);
}
