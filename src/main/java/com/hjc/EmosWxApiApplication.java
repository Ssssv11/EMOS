package com.hjc;

import cn.hutool.core.util.StrUtil;
import com.hjc.db.dao.SysConfigDao;
import com.hjc.db.pojo.SysConfig;
import com.hjc.dto.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

@EnableAsync
@SpringBootApplication
@ServletComponentScan
@Slf4j
public class EmosWxApiApplication {

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private SystemConstants systemConstants;

    public static void main(String[] args) {
        SpringApplication.run(EmosWxApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        List<SysConfig> list = sysConfigDao.selectAllParam();
        list.forEach(one -> {
            String key = one.getParamKey();
            key = StrUtil.toCamelCase(key);
            String value = one.getParamValue();
            try {
                Field field = systemConstants.getClass().getDeclaredField(key);
                field.set(systemConstants, value);
            } catch (Exception e) {
                log.error("执行异常", e);
            }
        });
    }
}
