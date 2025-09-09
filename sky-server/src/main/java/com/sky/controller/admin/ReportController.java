package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;
    /**
     * 营业额统计
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverReportStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        log.info("营业额统计：开始：{},结束：{}",begin,end);
        return Result.success(reportService.getTurnoverReportStatistics(begin,end));
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户数量统计")
    public Result<UserReportVO> userReportStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        log.info("用户数量统计：开始：{},结束：{}",begin,end);
        return Result.success(reportService.getUserReportStatistics(begin,end));
    }

    /**
     * 总订单数量，有效订单数量统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("总订单数量，有效订单数量统计")
    public Result<OrderReportVO> orderReportStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        log.info("总订单数量，有效订单数量：{}，{}",begin,end);
        return Result.success(reportService.getOrderReportStatistics(begin,end));
    }
}
