package com.hjc.service;

import java.util.HashMap;

public interface CheckinService {
    String validCanCheckIn(int userId, String date);
    void checkin(HashMap param);
}
