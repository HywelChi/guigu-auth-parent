package com.atguigu.system.service;

public interface LoginLogService {
    void save(String username, String ipAddr, Integer status, String msg);
}
