package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO OrderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询历史订单
     * @param pageNumber
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery(int pageNumber, int pageSize, Integer status);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 取消订单
     * @param id
     * @return
     */
    void userCancelById(Long id) throws Exception;

    /**
     * 再来一单
     * @param id
     * @return
     */
    void repetition(Long id);

    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSerch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistic();

    /**
     * 接单
     * @return
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     * @throws Exception
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     *取消订单
     * @param ordersCancelDTO
     * @return
     * @throws Exception
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     * @param id
     * @return
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     * @return
     */
    void complete(Long id);

    /**
     * 客户催单
     * @param id
     * @return
     */
    void remind(Long id);
}
