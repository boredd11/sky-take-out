package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    //@Scheduled(cron = "0/10 * * * * ?")
    public void processTimeOutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getStatusByOrderTimeOUt(Orders.PENDING_PAYMENT,time);
       if(ordersList != null && !ordersList.isEmpty()){
           for(Orders orders : ordersList){
               orders.setStatus(Orders.CANCELLED);
               orders.setCancelTime(LocalDateTime.now());
               orders.setCancelReason("订单超时，取消订单");
               orderMapper.update(orders);
           }
       }
    }

    /**
     * 定时处理正在派送的订单
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理正在派送的订单：{}",LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getStatusByOrderTimeOUt(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单一直处于派送中，取消订单");
                orderMapper.update(orders);
            }
        }
    }
}
