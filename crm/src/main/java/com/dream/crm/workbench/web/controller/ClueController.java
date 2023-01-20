package com.dream.crm.workbench.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.commons.utils.UUIDUtils;
import com.dream.crm.settings.pojo.DicValue;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.DicValueService;
import com.dream.crm.settings.services.UserService;
import com.dream.crm.workbench.pojo.Activity;
import com.dream.crm.workbench.pojo.Clue;
import com.dream.crm.workbench.pojo.ClueRemark;
import com.dream.crm.workbench.services.ActivityService;
import com.dream.crm.workbench.services.ClueService;
import com.dream.crm.workbench.services.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //获取所有的用户
        List<User> userList = userService.selectAllUsers();
        List<DicValue> appellation = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueState = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> source = dicValueService.queryDicValueByTypeCode("source");
        //
        //将用户信息发送到浏览器
        request.setAttribute("usersList",userList);
        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);
        //请求转发到市场活动主页面
        return "workbench/clue/index";
    }


    /**
     * 保存创建的线索
     * @param clue
     * @return
     */
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        //获取用户
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        try {
            //调用service 层方法，保存数据
            int ret = clueService.saveCreateClue(clue);
            if (ret > 0){
                if(ret>0){
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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

    /**
     * 进行分页查询
     * @return
     */
    @RequestMapping("/workbench/clue/QueryClueConditionForPage.do")
    @ResponseBody
    public Object QueryClueConditionForPage(String fullname,String owner,String company,String phone,
                                            String mphone,String appellation,String state,String source,
                                            int pageNo,int pageSize){

        //封装参数
        HashMap<String, Object> map = new HashMap<>();
//        fullname = fullname + appellation;
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("pageNo",(pageNo - 1) * pageSize);
        map.put("pageSize",pageSize);
        //查询数据
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);

        //根据结果生成响应信息
        HashMap<String, Object> clueMap = new HashMap<>();
        clueMap.put("clueList",clueList);
        clueMap.put("totalRows",totalRows);
        return clueMap;
    }

    /**
     * 删除数据库中指定的记录
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueById.do")
    @ResponseBody
    public Object deleteClueById(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int deleteClueById = clueService.deleteClueById(id);
            if (deleteClueById > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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


    /**
     * 通过id查询clue
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id){
        return clueService.queryClueById(id);
    }

    /**
     * 将修改好的线索信息保存到数据库
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        clue.setEditBy(user.getId());
        //保存参数
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.saveEditClue(clue);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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

    /**
     * 查询线索详情
     * @return
     */
    @RequestMapping(value = "/workbench/clue/detailClue.do")
    public String detailClue(@RequestParam(value = "id") String id, HttpServletRequest request){
        //调用方法，查询数据
        Clue detailClue = clueService.queryClueForDetailById(id);
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkForDetailById(id);
        List<Activity> activities = activityService.queryActivityForDetailByClueId(id);

        //把数据保存到request中
        request.setAttribute("detailClue",detailClue);
        request.setAttribute("clueRemarks",clueRemarks);
        request.setAttribute("activities",activities);

        //请求转发
        return "workbench/clue/detail";
    }

    /**
     * 查询可关联的市场活动
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityDetailByNameClueId(String activityName,String clueId) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用方法
        List<Activity> activityList = activityService.queryActivityDetailByNameClueId(map);
        return activityList;
    }
}