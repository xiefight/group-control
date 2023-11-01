package cn.yunshi.groupcontrol.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.yunshi.groupcontrol.bo.TaskBo;
import cn.yunshi.groupcontrol.business.BrowseEventOperate;
import cn.yunshi.groupcontrol.business.CommentEventOperate;
import cn.yunshi.groupcontrol.business.ForwardEventOperate;
import cn.yunshi.groupcontrol.business.SupportEventOperate;
import cn.yunshi.groupcontrol.common.BasicResult;
import cn.yunshi.groupcontrol.common.Constant;
import cn.yunshi.groupcontrol.common.GroupTaskStatus;
import cn.yunshi.groupcontrol.common.Response;
import cn.yunshi.groupcontrol.dao.GroupEventDao;
import cn.yunshi.groupcontrol.dao.GroupTaskDao;
import cn.yunshi.groupcontrol.entity.GroupEventEntity;
import cn.yunshi.groupcontrol.entity.GroupTaskEntity;
import cn.yunshi.groupcontrol.exception.InputErrorException;
import cn.yunshi.groupcontrol.service.IControlScriptService;
import cn.yunshi.groupcontrol.service.IGroupWebService;
import cn.yunshi.groupcontrol.util.DateUtil;
import cn.yunshi.groupcontrol.util.PageUtils;
import cn.yunshi.groupcontrol.util.Query;
import cn.yunshi.groupcontrol.vo.GroupEventRespVo;
import cn.yunshi.groupcontrol.vo.GroupTaskReqVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
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
public class GroupWebServiceImpl extends ServiceImpl<GroupTaskDao, GroupTaskEntity> implements IGroupWebService {

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
    private SupportEventOperate supportEventOperate;
    @Autowired
    private CommentEventOperate commentEventOperate;
    @Autowired
    private BrowseEventOperate browseEventOperate;
    @Autowired
    private ForwardEventOperate forwardEventOperate;


    @Override
    public Response reportTask(Map<String, Object> param) {
        //1.分解事件
        TaskBo taskBo = new TaskBo();
        BeanUtil.fillBeanWithMap(param, taskBo, true);

        //2.校验
        if (taskBo.getSearchMethod().equals("2")) {
            if (Objects.isNull(taskBo.getWeixinExtraVo())
                    || StringUtils.isEmpty(taskBo.getWeixinExtraVo().getWeixinVideoName())) {
                return BasicResult.getFailResponse("视频号名字全称方式搜索，缺少必要的字段");
            }
        }

        IControlScriptService scriptService = weixinScriptService;
        if (Constant.PlatForm.DOUYIN.equals(taskBo.getPlatform())) {
            scriptService = douyinScriptService;
        }
        //3.获取空闲设备，给设备加锁，可以使用redis
        List<String> androidIds = scriptService.getAndroidIds();
        int eventSums = taskBo.getEventSums();

//        if (eventSums > androidIds.size()) {
//            return BasicResult.getFailResponse("任务数量不能大于设备数量，任务数量：" + eventSums + "  可用设备数量：" + androidIds.size());
//        }

        //2.拼装参数，1任务+n事件，存入数据库
        //2.1构建任务基础信息
        GroupTaskEntity groupTaskEntity = new GroupTaskEntity();
        groupTaskEntity.setContentUrl(taskBo.getContentUrl());
        groupTaskEntity.setPlatform(taskBo.getPlatform());
        //状态执行中--等后续使用消息队列，完全异步时，再设置初始状态，等消费者领到任务后置为执行状态
        groupTaskEntity.setStatus(CollUtil.isEmpty(androidIds) ? GroupTaskStatus.FAIL.getCode() : GroupTaskStatus.EXECUTE.getCode());
        groupTaskEntity.setCreateTime(new Date());
        groupTaskEntity.setUpdateTime(new Date());
        groupTaskEntity.setUserId("1001");
        groupTaskEntity.setUserName("test");
        groupTaskEntity.setErrMsg(CollUtil.isEmpty(androidIds) ? "无可用设备" : "");
        this.baseMapper.insert(groupTaskEntity);
        Integer taskId = groupTaskEntity.getId();

        IControlScriptService finalScriptService = scriptService;
        CompletableFuture.runAsync(() -> {
            try {
                asyncExeEvents(taskBo, taskId, finalScriptService, androidIds, eventSums);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, threadPoolExecutor);
        //4.返回
        return BasicResult.getSuccessResponse(taskId);
    }

    private void asyncExeEvents(TaskBo taskBo, Integer taskId,
                                IControlScriptService scriptService,
                                List<String> androidIds, int eventSums) throws InterruptedException {


        CountDownLatch cdl = new CountDownLatch(eventSums);

        //4.放入线程池，异步执行
        if (null != taskBo.getCommentVo()) {
            int nums = Optional.ofNullable(taskBo.getCommentVo().getNums()).orElse(0);
            for (int comment = 0; comment < nums; comment++) {
                CompletableFuture.runAsync(() -> {
                    commentEventOperate.reportFlow(taskId, androidIds, taskBo, scriptService);
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        if (null != taskBo.getSupportVo()) {
            int nums = Optional.ofNullable(taskBo.getSupportVo().getNums()).orElse(0);
            for (int support = 0; support < nums; support++) {
                CompletableFuture.runAsync(() -> {
                    supportEventOperate.reportFlow(taskId, androidIds, taskBo, scriptService);
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        if (null != taskBo.getBrowseVo()) {
            int nums = Optional.ofNullable(taskBo.getBrowseVo().getNums()).orElse(0);
            for (int browse = 0; browse < nums; browse++) {

                CompletableFuture.runAsync(() -> {
                    browseEventOperate.reportFlow(taskId, androidIds, taskBo, scriptService);
                    cdl.countDown();
                }, threadPoolExecutor);
            }
        }
        if (null != taskBo.getForwardVo()) {
            int nums = Optional.ofNullable(taskBo.getForwardVo().getNums()).orElse(0);
            for (int forward = 0; forward < nums; forward++) {

                CompletableFuture.runAsync(() -> {
                    forwardEventOperate.reportFlow(taskId, androidIds, taskBo, scriptService);
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


}
