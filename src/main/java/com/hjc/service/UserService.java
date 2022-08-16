package com.hjc.service;

import java.util.Set;

public interface UserService {
    int registerUser(String registerCode,String code,String nickname,String photo);

    Set<String> searchUserPermissions(int userId);

    Integer login(String code);

}
