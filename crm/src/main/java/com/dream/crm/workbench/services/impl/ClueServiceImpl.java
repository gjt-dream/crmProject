package com.dream.crm.workbench.services.impl;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.commons.utils.UUIDUtils;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.workbench.mapper.*;
import com.dream.crm.workbench.pojo.*;
import com.dream.crm.workbench.services.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public int deleteClueById(String[] ids) {
        return clueMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveEditClue(Clue clue) {
        return clueMapper.updateByPrimaryKey(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    /**
     * 线索转换
     * @param map
     */
    @Override
    public void saveConvertClue(Map<String, Object> map) {
        //封装参数
        String clueId = (String)map.get("clueId");
        User user = (User)map.get("user");
        String isCreateTran = (String)map.get("isCreateTran");
        //根据id查询线索信息
        Clue clue = clueMapper.selectClueById(clueId);

        //把线索中有关公司的信息转换到客户表中
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        //调用方法
        customerMapper.insertCustomer(customer);

        //把线索中有关个人的信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());

        //调用方法
        contactsMapper.insertContacts(contacts);

        //根据clueId查询该线索下的所有备注信息
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //如果该线索下有备注，则把该线索下所有的备注批量保存到创建的客户备注表中,同时批量保存创建的联系人备注表中
        if (clueRemarkList != null && clueRemarkList.size() > 0){
            //遍历clueRemarkList，生成客户备注
            CustomerRemark customerRemark = null;
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            ArrayList<ContactsRemark> contactsRemarkArrayList = new ArrayList<>();
            for (ClueRemark clueRemark:clueRemarkList) {
                customerRemark = new CustomerRemark();
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemarkList.add(customerRemark);


                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemarkArrayList.add(contactsRemark);

            }
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkArrayList);
        }

        //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //把线索和市场活动关联关系转换到联系人和市场活动的关联关系表中
        if (clueActivityRelationList != null && clueActivityRelationList.size() > 0){
            ContactsActivityRelation contactsActivityRelation = null;
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList) {
                contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelationList.add(contactsActivityRelation);
            }

            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);

            //如果需要创建交易，则在交易表中添加一条记录
            if ("true".equals(isCreateTran)){
                Tran tran = new Tran();
                tran.setActivityId((String) map.get("activityId"));
                tran.setContactsId(contacts.getId());
                tran.setCreateBy(user.getId());
                tran.setCreateTime(DateUtils.formateDateTime(new Date()));
                tran.setCustomerId(customer.getId());
                tran.setExpectedDate((String) map.get("expectedDate"));
                tran.setId(UUIDUtils.getUUID());
                tran.setMoney((String) map.get("money"));
                tran.setName((String) map.get("name"));
                tran.setOwner(user.getId());
                tran.setStage((String) map.get("stage"));

                tranMapper.insertTran(tran);


                if (clueRemarkList != null && clueRemarkList.size() > 0){
                    TranRemark tranRemark = null;
                    ArrayList<TranRemark> tranRemarkList = new ArrayList<>();
                    for (ClueRemark clueRemark:clueRemarkList) {
                        tranRemark = new TranRemark();
                        tranRemark.setCreateBy(clueRemark.getCreateBy());
                        tranRemark.setCreateTime(clueRemark.getCreateTime());
                        tranRemark.setEditBy(clueRemark.getEditBy());
                        tranRemark.setEditFlag(clueRemark.getEditFlag());
                        tranRemark.setEditTime(clueRemark.getEditTime());
                        tranRemark.setId(UUIDUtils.getUUID());
                        tranRemark.setNoteContent(clueRemark.getNoteContent());
                        tranRemark.setTranId(tran.getId());
                        tranRemarkList.add(tranRemark);
                    }
                    tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
                }
                //删除该线索下的所有备注
                clueRemarkMapper.deleteClueRemarkByClueId(clueId);
                //根据ClueId删除线索和市场活动的关联关系
                clueActivityRelationMapper.deleteClueRemarkRelationByClueId(clueId);
                //通过id删除线索
                clueMapper.deleteClueById(clueId);
            }
        }
    }
}
