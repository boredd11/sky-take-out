package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 营业额统计
     * @return
     */
    TurnoverReportVO getTurnoverReportStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserReportStatistics(LocalDate begin, LocalDate end);

    /**
     * 总订单数量，有效订单数量统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderReportStatistics(LocalDate begin, LocalDate end);

    /**
     * 热销菜品Top10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
