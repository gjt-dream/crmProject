package com.dream.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {
    @RequestMapping("/workbench/index")
    public String index(){
        System.out.println("我进来了！");
        return "workbench/index";
    }
}
