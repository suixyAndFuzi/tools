package com.example.demo.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


/**
 * redis分布式锁帮助类
 *
 * @author
 */
@Service
public class RedissLockUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public  RLock lock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }


    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public RLock lock(String lockKey, int timeout) {
        RLock mylock = redissonClient.getLock(lockKey);
        mylock.lock(timeout, TimeUnit.MINUTES); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
        System.err.println("======lock======" + Thread.currentThread().getName());
        return mylock;
    }


    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        System.err.println("======unlock======" + Thread.currentThread().getName());
        lock.unlock();
    }


    /**
     * 尝试获取锁
     *
     * @param lockKey   * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

}