package cn.yunshi.groupcontrol.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.common.BasicResult;
import cn.yunshi.groupcontrol.common.Constant;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.common.Response;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.dao.GroupTaskDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.entity.GroupTaskEntity;
import cn.yunshi.groupcontrol.enums.ControlTypeEnum;
import cn.yunshi.groupcontrol.exception.InputErrorException;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import cn.yunshi.groupcontrol.service.IGroupService;
import cn.yunshi.groupcontrol.util.DateUtil;
import cn.yunshi.groupcontrol.util.PageUtils;
import cn.yunshi.groupcontrol.util.Query;
import cn.yunshi.groupcontrol.vo.GroupEventRespVo;
import cn.yunshi.groupcontrol.vo.GroupTaskReqVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-18 15:35
 **/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupTaskDao, GroupTaskEntity> implements IGroupService {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private GroupEventDao groupEventDao;

    @Autowired
    @Qualifier("douyinScriptService")
    private DouyinScriptServiceImpl douyinScriptService;

    @Autowired
    @Qualifier("weixinScriptService")
    private WeixinVideoScriptServiceImpl weixinScriptService;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public Response reportTask(Map<String, Object> param) {
        //1.分解事件
        TaskBo taskBo = new TaskBo();
        BeanUtil.fillBeanWithMap(param, taskBo, true);

        IControlScriptService scriptService = weixinScriptService;
        if (Constant.PlatForm.DOUYIN.equals(taskBo.getPlatform())) {
            scriptService = douyinScriptService;
        }
        //3.获取空闲设备，给设备加锁，可以使用redis
        List<String> androidIds = scriptService.getAndroidIds();
        int eventSums = taskBo.getEventSums();

        if (eventSums > androidIds.size()) {
            return BasicResult.getFailResponse("任务数量不能大于设备数量，任务数量：" + eventSums + "  可用设备数量：" + androidIds.size());
        }

        //2.拼装参数，1任务+n事件，存入数据库
        //2.1构建任务基础信息
        GroupTaskEntity groupTaskEntity = new GroupTaskEntity();
        groupTaskEntity.setContentUrl(taskBo.getContentUrl());
        groupTaskEntity.setPlatform(taskBo.getPlatform());
        //状态执行中--等后续使用消息队列，完全异步时，再设置初始状态，等消费者领到任务后置为执行状态
        groupTaskEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
        groupTaskEntity.setCreateTime(new Date());
        groupTaskEntity.setUpdateTime(new Date());
        groupTaskEntity.setUserId("1001");
        groupTaskEntity.setUserName("test");
        this.baseMapper.insert(groupTaskEntity);
        Integer taskId = groupTaskEntity.getId();

        IControlScriptService finalScriptService = scriptService;
        CompletableFuture.runAsync(() -> {
            try {
                asyncExeEvents(taskBo, groupTaskEntity, taskId, finalScriptService, androidIds, eventSums);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, threadPoolExecutor);
        //4.返回
        return BasicResult.getSuccessResponse(taskId);
    }

    private void asyncExeEvents(TaskBo taskBo, GroupTaskEntity groupTaskEntity, Integer taskId,
                                IControlScriptService scriptService, List<String> androidIds, int eventSums) throws InterruptedException {


        CountDownLatch cdl = new CountDownLatch(eventSums);

        //4.放入线程池，异步执行
        if (null != taskBo.getCommentVo()) {
            Integer nums = Optional.ofNullable(taskBo.getCommentVo().getNums()).orElse(0);
            for (int comment = 0; comment < nums; comment++) {

                IControlScriptService finalScriptService = scriptService;
                CompletableFuture.runAsync(() -> {

                    String androidId = getAndroidLock(androidIds);
                    System.out.println("评论事件 >> " + Thread.currentThread().getId() + " 获取到了锁：" + androidId);
//                    System.out.println("评论--androidId：" + androidId);

                    //构建事件基础信息
                    GroupEventEntity groupEventEntity = new GroupEventEntity();
                    groupEventEntity.setEventType(ControlTypeEnum.COMMENT.getCode());
                    //状态执行中
                    groupEventEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
                    groupEventEntity.setTaskId(taskId);
                    groupEventEntity.setCommentText(taskBo.getCommentVo().getComment());
                    groupEventEntity.setAndroidId(androidId);
                    int eventId = groupEventDao.insert(groupEventEntity);
                    //执行事件
                    boolean commentRes = false;
                    try {
                        commentRes = finalScriptService.comment(groupEventEntity, groupTaskEntity.getContentUrl());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GroupEventEntity update = new GroupEventEntity();
                    update.setId(groupEventEntity.getId());
                    update.setStatus(commentRes ? GroupTaskStatus.SUCCESS.getCode() : GroupTaskStatus.FAIL.getCode());
                    groupEventDao.updateById(update);
                    redissonClient.getLock(androidId).unlock();
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        if (null != taskBo.getSupportVo()) {
            Integer nums = Optional.ofNullable(taskBo.getSupportVo().getNums()).orElse(0);
            for (int support = 0; support < nums; support++) {

                IControlScriptService finalScriptService = scriptService;

                CompletableFuture.runAsync(() -> {

                    String androidId = getAndroidLock(androidIds);
                    System.out.println("点赞事件 >> " + Thread.currentThread().getId() + " 获取到了锁：" + androidId);
//                    System.out.println("点赞--androidId：" + androidId);

                    //构建事件基础信息
                    GroupEventEntity groupEventEntity = new GroupEventEntity();
                    groupEventEntity.setEventType(ControlTypeEnum.SUPPORT.getCode());
                    //状态执行中
                    groupEventEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
                    groupEventEntity.setTaskId(taskId);
                    groupEventEntity.setAndroidId(androidId);
                    int eventId = groupEventDao.insert(groupEventEntity);
                    //执行事件
                    boolean supportRes = false;
                    try {
                        supportRes = finalScriptService.support(groupEventEntity, groupTaskEntity.getContentUrl());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GroupEventEntity update = new GroupEventEntity();
                    update.setId(groupEventEntity.getId());
                    update.setStatus(supportRes ? GroupTaskStatus.SUCCESS.getCode() : GroupTaskStatus.FAIL.getCode());
                    groupEventDao.updateById(update);

                    redissonClient.getLock(androidId).unlock();
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        if (null != taskBo.getBrowseVo()) {
            Integer nums = Optional.ofNullable(taskBo.getBrowseVo().getNums()).orElse(0);
            for (int browse = 0; browse < nums; browse++) {

                IControlScriptService finalScriptService = scriptService;

                CompletableFuture.runAsync(() -> {

                    String androidId = getAndroidLock(androidIds);
                    System.out.println("浏览事件 >> " + Thread.currentThread().getId() + " 获取到了锁：" + androidId);
//                    System.out.println("浏览--androidId：" + androidId);

                    //构建事件基础信息
                    GroupEventEntity groupEventEntity = new GroupEventEntity();
                    groupEventEntity.setEventType(ControlTypeEnum.BROWSE.getCode());
                    //状态执行中
                    groupEventEntity.setStatus(GroupTaskStatus.EXECUTE.getCode());
                    groupEventEntity.setTaskId(taskId);
                    groupEventEntity.setAndroidId(androidId);
                    groupEventEntity.setBrowseTime(taskBo.getBrowseVo().getBrowseTime());
                    int eventId = groupEventDao.insert(groupEventEntity);
                    //执行事件
                    boolean browseRes = false;
                    try {
                        browseRes = finalScriptService.browse(groupEventEntity, groupTaskEntity.getContentUrl());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        //redissonClient.getLock(androidId).unlock();
                    }
                    GroupEventEntity update = new GroupEventEntity();
                    update.setId(groupEventEntity.getId());
                    update.setStatus(browseRes ? GroupTaskStatus.SUCCESS.getCode() : GroupTaskStatus.FAIL.getCode());
                    groupEventDao.updateById(update);

                    redissonClient.getLock(androidId).unlock();
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        cdl.await();
        System.out.println("所有事件执行完成");
        GroupTaskEntity update = new GroupTaskEntity();
        update.setId(taskId);
        update.setStatus(GroupTaskStatus.SUCCESS.getCode());
        this.baseMapper.updateById(update);
    }

    @Override
    public Response listTask(GroupTaskReqVo groupTaskReqVo) throws InputErrorException {

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put(Constant.Page.PAGE, groupTaskReqVo.getPage());
        pageMap.put(Constant.Page.LIMIT, groupTaskReqVo.getLimit());

        QueryWrapper<GroupTaskEntity> queryWrapper = new QueryWrapper<>();
        if (groupTaskReqVo.getPlatform() != null) {
            queryWrapper.eq("platform", groupTaskReqVo.getPlatform());
        }
        if (groupTaskReqVo.getStatus() != null) {
            queryWrapper.eq("status", groupTaskReqVo.getStatus());
        }

        //获取前端传的开始时间和结束时间字段
        String startTime = groupTaskReqVo.getStartTime();
        String endTime = groupTaskReqVo.getEndTime();

        Date startDate = StringUtils.isEmpty(startTime) ? new Date() :
                DateUtil.getStartOrEndTime(startTime, "startTime", false);
//                DateUtil.getDayZero(DateUtil.getStartOrEndTime(startTime, "startTime", false));
        Date endDate = StringUtils.isEmpty(endTime) ? new Date() :
                DateUtil.getStartOrEndTime(endTime, "endTime", true);

//        queryWrapper.between("create_time", startDate, endDate);
        queryWrapper.gt("create_time", startDate);
        queryWrapper.lt("create_time", endDate);

        IPage<GroupTaskEntity> page = this.page(new Query<GroupTaskEntity>().getPage(pageMap, "create_time", false), queryWrapper);

        List<GroupTaskEntity> tasks = page.getRecords().stream().map(task -> {
            Integer taskId = task.getId();
            List<GroupEventEntity> eventEntityList = groupEventDao.selectList(new QueryWrapper<GroupEventEntity>().eq("task_id", taskId));

            GroupEventRespVo eventRespVo = new GroupEventRespVo();
            for (GroupEventEntity eventEntity : eventEntityList) {
                eventRespVo.distributeType(eventEntity);
            }
            task.setEventRespVo(eventRespVo);
            return task;
        }).collect(Collectors.toList());
        page.setRecords(tasks);
        return BasicResult.getSuccessResponse(new PageUtils(page));
    }

    private String getAndroidLock(List<String> androidIds) {
        //获取一个设备的锁
        int size = androidIds.size();
        for (int i = 0; i < size; i++) {
            String androidId = androidIds.get(i);
            //获取到了锁，就不再往后找了
            //获取不到锁，就一直往后找，直到找到锁或者集合遍历完
            if (redissonClient.getLock(androidId).tryLock()) {
                return androidId;
            }
            //一轮没找到就重复遍历，直到找到为止
            if (i == size - 1) {
                return getAndroidLock(androidIds);
            }
        }
        return null;
    }

}
