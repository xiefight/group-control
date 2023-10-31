package cn.yunshi.groupcontrol.util;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-27 15:12
 **/
@Component
public class CommonUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取设备锁
     *
     * @param androidIds 要遍历的设备集合
     * @return
     */
    public String getAndroidLock(List<String> androidIds) {
        //获取一个设备的锁
        int size = androidIds.size();
//        System.out.println("线程 " + Thread.currentThread().getId() + "准备获取锁 》》》》》》》》" + androidIds);
        for (int i = 0; i < size; i++) {
            String androidId = androidIds.get(i);
            //获取到了锁，就不再往后找了
            //获取不到锁，就一直往后找，直到找到锁或者集合遍历完
//            redissonClient.getLock(androidId).remainTimeToLive();
            if (redissonClient.getLock(androidId).tryLock()) {
                System.out.println("线程 " + Thread.currentThread().getId() + "获取到了锁 》》》》》》》》" + androidId);
                return androidId;
            }
        }
        return null;
    }

}
