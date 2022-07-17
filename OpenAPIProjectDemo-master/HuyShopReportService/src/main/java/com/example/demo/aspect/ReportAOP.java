package com.example.demo.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ReportAOP {

    private Logger logger = LoggerFactory.getLogger(ReportAOP.class);

        @Before("execution(* com.example.demo.controller.CartReportController.showReportBetween(..))")
    public void beforeExport(JoinPoint joinPoint)
    {
        logger.info("Export PDF start");
    }
    @AfterReturning("execution(* com.example.demo.controller.CartReportController.showReportBetween(..))")
    public void AfterExport(JoinPoint joinPoint)
    {
        logger.info("Export PDF done");
    }
}
