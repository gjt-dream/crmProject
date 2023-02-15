package com.dream.crm.workbench.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.settings.pojo.DicValue;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.DicValueService;
import com.dream.crm.settings.services.UserService;
import com.dream.crm.workbench.pojo.Clue;
import com.dream.crm.workbench.pojo.Tran;
import com.dream.crm.workbench.pojo.TranHistory;
import com.dream.crm.workbench.pojo.TranRemark;
import com.dream.crm.workbench.services.CustomerService;
import com.dream.crm.workbench.services.TranHistoryService;
import com.dream.crm.workbench.services.TranRemarkService;
import com.dream.crm.workbench.services.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @Autowired
    private TranHistoryService tranHistoryService;
    /**
     * 跳转到交易页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        //保存到request
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);


        return "workbench/transaction/index";

    }


    /**
     * 保存创建的交易
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){


        List<User> userList = userService.selectAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        //保存到request
        request.setAttribute("userList",userList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);

        return "workbench/transaction/save";
    }



    /**
     * 进行分页查询
     * @return
     */
    @RequestMapping("/workbench/transaction/QueryTranConditionForPage.do")
    @ResponseBody
    public Object QueryTranConditionForPage(String owner,String name,String customerId,String stage,
                                            String type,String source,String contactsId,
                                            int pageNo,int pageSize){

        //封装参数
        HashMap<String, Object> map = new HashMap<>();
//        fullname = fullname + appellation;
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("pageNo",(pageNo - 1) * pageSize);
        map.put("pageSize",pageSize);
        //查询数据
        List<Clue> tranList = tranService.queryTranByConditionForPage(map);
        int totalRows = tranService.queryCountOfTranByCondition(map);

        //根据结果生成响应信息
        HashMap<String, Object> clueMap = new HashMap<>();
        clueMap.put("tranList",tranList);
        clueMap.put("totalRows",totalRows);
        return clueMap;
    }


    /**
     * 删除数据库中指定的记录
     * @return
     */
    @RequestMapping("/workbench/transaction/deleteTranById.do")
    @ResponseBody
    public Object deleteTranById(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int deleteClueById = tranService.deleteTranByPrimaryKey(id);
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
     * 跳转到修改页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/toEdit.do")
    public String toEdit(String id,HttpServletRequest request){

        List<User> userList = userService.selectAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //保存到request
        request.setAttribute("tranId",id);
        request.setAttribute("userList",userList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);

        return "workbench/transaction/edit";
//        List<User> userList = userService.selectAllUsers();
//        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
//        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
//        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
//
//        //保存到request
//        request.setAttribute("userList",userList);
//        request.setAttribute("transactionTypeList",transactionTypeList);
//        request.setAttribute("sourceList",sourceList);
//        request.setAttribute("stageList",stageList);

    }

    /**
     * 通过id查询交易
     * @param id
     * @return
     */
    @RequestMapping("/workbench/transaction/queryTranById.do")
    @ResponseBody
    public Object queryTranById(String id){
        Tran tran = tranService.queryByPrimaryKey(id);
//        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
//        String value = null;
//        for (DicValue v:stageList) {
//            if (v.getId() == tran.getStage()){
//                value = v.getValue();
//            }
//        }
//        String possibility = (String)getPossibilityByStage(value);
//        tran.setPossibility(possibility);
        return tran;
    }

    /**
     * 将修改好的线索信息保存到数据库
     * @param tran
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/saveEditTran.do")
    @ResponseBody
    public Object saveEditTran(Tran tran,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        tran.setEditTime(DateUtils.formateDateTime(new Date()));
        tran.setEditBy(user.getId());
        //保存参数
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = tranService.saveEditTran(tran);
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


    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //解析properties文件
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        //返回
        return possibility;
    }

    /**
     * 根据客户名字精确查询所有客户
     * @param customerName
     * @return
     */
    @RequestMapping("/workbench/transaction/queryAllCustomerName.do")
    @ResponseBody
    public Object queryAllCustomerName(String customerName){
        List<String> customerNameList = customerService.queryCustomerNameByName(customerName);
        return customerNameList;
    }

    /**
     * 保存创建的交易
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));


        ReturnObject returnObject = new ReturnObject();
        try {
            //调用方法保存交易
            tranService.saveCreateTran(map);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailByTranId(id);

        //根据tran的阶段查出可能性

        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);
        //把数据保存到request中
        request.setAttribute("tran",tran);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);
//        request.setAttribute("possibility",possibility);

        //调用service方法，查询数据字典的值
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";
    }
}
