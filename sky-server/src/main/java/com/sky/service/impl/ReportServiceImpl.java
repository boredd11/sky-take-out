package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 营业额统计
     * @return
     */
    public TurnoverReportVO getTurnoverReportStatistics(LocalDate begin, LocalDate end) {
        //存放begin~end的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //日期计算,从begin到end
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnOverList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnOver = orderMapper.sumByMap(map);
            turnOver = turnOver == null ? 0.0 : turnOver;
            turnOverList.add(turnOver);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnOverList,","))
                .build();
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserReportStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
          begin = begin.plusDays(1);
          dateList.add(begin);
        }
        //统计每日新的用户数量
        List<Integer> newUserList = new ArrayList<>();
        //统计总共用户的数量
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap<>();
            map.put("end",endTime);
            //总用户人数
            Integer totalUser = userMapper.sumByMap(map);

            map.put("begin",beginTime);
            //新增用户
            Integer newUser = userMapper.sumByMap(map);
            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    /**
     * 总订单数量，有效订单数量统计
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderReportStatistics(LocalDate begin, LocalDate end) {
        //shijianl
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
           begin = begin.plusDays(1);
           dateList.add(begin);
        }
        //每天总共订单
        List<Integer> orderCountList = new ArrayList<>();
        //每天有效订单
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            //统计每天的总订单数量
            Integer orderCount = getOrderCount(beginTime,endTime,null);
            orderCountList.add(orderCount);
            //统计每天的有效订单数量
            Integer validOrderCount = getOrderCount(beginTime,endTime,Orders.COMPLETED);
            validOrderCountList.add(validOrderCount);
        }
        //订单总数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        //有效订单数量
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //订单有效率
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO.builder()
                        .dateList(StringUtils.join(dateList,","))
                        .orderCountList(StringUtils.join(orderCountList,","))
                        .validOrderCountList(StringUtils.join(validOrderCountList,","))
                        .totalOrderCount(totalOrderCount)
                        .validOrderCount(validOrderCount)
                        .orderCompletionRate(orderCompletionRate)
                        .build();
    }

    /**
     *统计每天的订单数量
     * @param begin
     * @param end
     * @param status
     * @return
     */
    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end,Integer status){
        Map map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);
        return orderMapper.countByMap(map);
    }

    /**
     * 热销菜品Top10
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10List = orderMapper.getSalesTop10(beginTime,endTime);
        //名字
        List<String> names = salesTop10List.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names,",");
        //数量
        List<Integer> numbers = salesTop10List.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers,",");
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

}
