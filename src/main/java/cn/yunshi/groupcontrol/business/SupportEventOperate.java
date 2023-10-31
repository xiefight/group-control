package cn.yunshi.groupcontrol.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.MD5;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.dao.SupportRecordDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.entity.SupportRecordEntity;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-27 15:33
 **/
@Component
public class SupportEventOperate extends BaseOperate {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private GroupEventDao groupEventDao;
    @Autowired
    private SupportRecordDao supportRecordDao;

    public static ThreadLocal<String> contentUrlMap = new ThreadLocal<>();

    @Override
    protected GroupEventEntity saveGroupEvent(Integer taskId, TaskBo taskBo) {
        contentUrlMap.set(taskBo.getContentUrl());
        //构建事件基础信息
        GroupEventEntity groupEventEntity = new GroupEventEntity();
        groupEventEntity.setEventType(ControlTypeEnum.SUPPORT.getCode());
        //状态执行中
        groupEventEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
        groupEventEntity.setTaskId(taskId);
        int eventId = groupEventDao.insert(groupEventEntity);
        return groupEventEntity;
    }

    @Override
    protected boolean executeEvent(IControlScriptService scriptService,
                                   GroupEventEntity groupEventEntity,
                                   String contentUrl, String androidId) {
        //执行事件
        boolean supportRes = false;
        try {
            supportRes = scriptService.support(androidId, contentUrl);
            //点赞成功后，记录点赞信息
            if (supportRes) {
                SupportRecordEntity supportRecord = new SupportRecordEntity();
                supportRecord.setAndroidId(androidId);
                supportRecord.setContentUrl(contentUrl);
                supportRecord.setContentMd5(MD5.create().digestHex16(contentUrl));
                supportRecordDao.insert(supportRecord);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonClient.getLock(androidId).unlock();
            System.out.println("点赞事件 >>>>>>>>>>>> " + Thread.currentThread().getId() + " 释放了锁：" + androidId);
        }
        return supportRes;
    }

    /**
     * @param androidIds 已经排除过一次点赞设备了，这里重写获取锁操作，是在获取到锁之后，还要判断当前锁（androidId）是否被别的点赞（线程）使用了
     * @return
     */
    @Override
    protected String getAndroidLock(List<String> androidIds) {
        //先获取一个
        String androidId = super.getAndroidLock(androidIds);
        //从数据库中查找已点过赞的设备
        List<String> usedAndroidIds = usedAndroidIds();
        //包含就说明当前获取的锁已经被用了，要剔除掉，重新获取
        if (usedAndroidIds.contains(androidId)) {
            //将此设备剔除
            androidIds.remove(androidId);
            List<String> usableAndroidIds = new ArrayList<>(androidIds);
            if (CollUtil.isEmpty(usableAndroidIds)) {
                //剔除后的设备集合为空，说明没有可用设备了，返回失败提示
                return null;
            }
            return this.getAndroidLock(usableAndroidIds);
        }
        //否则就返回该设备锁
        return androidId;
    }

    /**
     * 点赞事件剔除已点过赞的设备
     *
     * @param contentUrl 点赞的视频url
     * @param androidIds 设备集合
     * @return 可以点赞的设备id
     */
    @Override
    protected List<String> excludeAndroidIds(String contentUrl, List<String> androidIds) {
        List<String> allAndroidIds = new ArrayList<>(androidIds);
        //1.从数据库中查找已点过赞的设备
        List<String> usedAndroidIds = usedAndroidIds();
        //所有的设备和已使用的设备去重
        allAndroidIds.removeAll(usedAndroidIds);
        return allAndroidIds;
    }

    private List<String> usedAndroidIds() {
        List<String> usedAndroidIds = new ArrayList<>();
        QueryWrapper<SupportRecordEntity> qw = new QueryWrapper();
//        System.out.println("线程==> " + Thread.currentThread().getId() + "  contentUrl==>" + contentUrlMap.get());
        qw.eq("content_md5", MD5.create().digestHex16(contentUrlMap.get()));
        List<SupportRecordEntity> supportRecordEntities = supportRecordDao.selectList(qw);
        if (CollUtil.isEmpty(supportRecordEntities)) {
            return usedAndroidIds;
        }
        return supportRecordEntities.stream().map(supportRecordEntity -> supportRecordEntity.getAndroidId()).collect(Collectors.toList());
    }
}
