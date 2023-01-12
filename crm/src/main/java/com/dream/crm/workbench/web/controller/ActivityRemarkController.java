package com.dream.crm.workbench.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.commons.utils.UUIDUtils;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.workbench.pojo.ActivityRemark;
import com.dream.crm.workbench.services.impl.ActivityRemarkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkServiceImpl activityRemarkService;


    /**
     * 保存市场活动备注
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session){
        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.saveCreateActivityRemark(remark);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
//        ActivityRemark activityRemark = activityRemarkService.queryById(id);
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
//                request.setAttribute("code",Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
//                request.setAttribute("code",Contants.RETURN_OBJECT_CODE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
//            request.setAttribute("code",Contants.RETURN_OBJECT_CODE_FAIL);
        }
//        return "redirect:/workbench/activity/detailActivity.do?id=" + activityRemark.getActivityId();
        return returnObject;
    }


    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activityRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.saveEditActivityRemark(activityRemark);
            if (ret > 0){
                if(ret>0){
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                    returnObject.setRetData(activityRemark);
                }else {
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                    returnObject.setMessage("系统繁忙，请稍后重试...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }
}
