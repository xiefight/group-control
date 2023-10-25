/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cn.yunshi.groupcontrol.util;

import cn.yunshi.groupcontrol.common.Constant;
import cn.yunshi.groupcontrol.xss.SQLFilter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 查询参数
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Query<T> {

    /**
     * 前端排序
     * @param params
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    /**
     * 支持后端排序
     * @param params
     * @param defaultOrderField 后端字段
     * @param isAsc 字段顺序
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(Constant.Page.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.Page.PAGE));
        }
        if (params.get(Constant.Page.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.Page.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.Page.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String) params.get(Constant.Page.ORDER_FIELD));
        String order = (String) params.get(Constant.Page.ORDER);


        //前端字段排序
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (Constant.Page.ASC.equalsIgnoreCase(order)) {
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if (StringUtils.isBlank(defaultOrderField)) {
            return page;
        }

        //默认排序
        if (isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        } else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
