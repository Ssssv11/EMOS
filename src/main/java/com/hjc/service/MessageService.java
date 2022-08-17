package com.hjc.service;

import com.hjc.db.pojo.MessageEntity;
import com.hjc.db.pojo.MessageRefEntity;

import java.util.HashMap;
import java.util.List;

public interface MessageService {
    String insertMessage(MessageEntity entity);

    List<HashMap> searchMessageByPage(int userId, long start, int length);

    HashMap searchMessageById(String id);

    String insertRef(MessageRefEntity entity);

    long searchUnreadCount(int userId);

    long searchLastCount(int userId);

    long updateUnreadMessage(String id);

    long deleteMessageRefById(String id);

    long deleteUserMessageRef(int userId);
}
