package com.dream.crm.workbench.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.commons.utils.HSSFUtils;
import com.dream.crm.commons.utils.UUIDUtils;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.impl.UserServiceImpl;
import com.dream.crm.workbench.pojo.Activity;
import com.dream.crm.workbench.services.impl.ActivityServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ActivityServiceImpl activityService;

    /**
     * 获取用户信息并跳转到市场活动页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //获取所有的用户
        List<User> userList = userService.selectAllUsers();
        //将用户信息发送到浏览器
        request.setAttribute("usersList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    /**
     * 向数据库中插入添加的数据
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/SaveCreateActivity.do")
    @ResponseBody
    public Object SaveCreateActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            //向数据库中插入数据
            int saveCreateActivity = activityService.SaveCreateActivity(activity);
            if (saveCreateActivity > 0){
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
     * 进行分页查询
     * @return
     */
    @RequestMapping("/workbench/activity/QueryActivityConditionForPage.do")
    @ResponseBody
    public Object QueryActivityConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        //封装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo - 1) * pageSize);
        map.put("pageSize",pageSize);
        //查询数据
        List<Activity> activityList = activityService.QueryActivityConditionForPage(map);
        int totalRows = activityService.QueryCountOfActivityByCondition(map);

        //根据结果生成响应信息
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


    /**
     * 删除数据库中指定的记录
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityById.do")
    @ResponseBody
    public Object deleteActivityById(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int deleteActivityById = activityService.deleteActivityById(id);
            if (deleteActivityById > 0){
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

    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id){
        Activity activity = activityService.selectByPrimaryKey(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setEditBy(user.getId());
        //保存参数
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityService.saveEditActivity(activity);
            if (i > 0){
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
     * 批量导出市场活动
     */
    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        //查询所有的市场活动
        List<Activity> activityList = activityService.queryAllActivities();
        //创建Excel文件，把 activityList 写入到Excel文件中
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");


        if (activityList != null && activityList.size() > 0){
            //遍历activityList，创建HSSFRow对象，生成所有的数据行
            for (int i= 0;i < activityList.size();i++){
                Activity activity = activityList.get(i);
                //每遍历一个activity，生成一行
                row = sheet.createRow(i + 1);
                //没创建一行创建11列，每一列数据都从activity中获取
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //把生成的Excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activities.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    /**
     * 选择导出市场活动
     */
    @RequestMapping("/workbench/activity/choiceExportActivities.do")
    public void choiceExportActivities(String[] id,HttpServletResponse response) throws IOException {
        //查询所有的市场活动
        List<Activity> activityList = activityService.queryActivitiesById(id);
        //创建Excel文件，把 activityList 写入到Excel文件中
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");


        if (activityList != null && activityList.size() > 0){
            //遍历activityList，创建HSSFRow对象，生成所有的数据行
            for (int i= 0;i < activityList.size();i++){
                Activity activity = activityList.get(i);
                //每遍历一个activity，生成一行
                row = sheet.createRow(i + 1);
                //没创建一行创建11列，每一列数据都从activity中获取
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //把生成的Excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activities.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    /**
     * 导入市场活动
     * @param activityFile 导入的文件
     * @param session
     * @return
     */

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            //把excel文件写到磁盘目录中
            //获取源文件的名字
//        String originalFilename = activityFile.getOriginalFilename();
//        File file = new File("文件路径");
            //写到磁盘
//        activityFile.transferTo(file);

            //解析excel文件，获取文件中的数据，并封装成activityList
            //获取输入流
//            FileInputStream inputStream = new FileInputStream("文件路径");
            //根据excel文件生成 HSSFWorkbook 对象，封装了所有的excel文件的所有信息
            InputStream inputStream = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            //根据workbook获取 HSSFSheet 对象，封装了所有的一页文件的所有信息
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity =null;
            ArrayList<Activity> activityArrayList = new ArrayList<>();
            for (int i = 1;i <= sheet.getLastRowNum();i++){
                row = sheet.getRow(i);//获取行下标
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0;j < row.getLastCellNum();j++){
                    cell = row.getCell(j);//获取列下标

                    String cellValue = HSSFUtils.getCellValueForStr(cell);

                    if (j == 0){
                        activity.setName(cellValue);
                    }else if (j == 1){
                        activity.setStartDate(cellValue);
                    }else if (j == 2){
                        activity.setEndDate(cellValue);
                    }else if (j == 3){
                        activity.setCost(cellValue);
                    }else if (j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成之后，把activity保存到list中
                activityArrayList.add(activity);
            }
            //调用service方法
            int byList = activityService.saveCreateActivityByList(activityArrayList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(byList);
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }


}
