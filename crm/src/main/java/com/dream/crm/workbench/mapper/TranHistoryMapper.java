package com.dream.crm.workbench.mapper;

import com.dream.crm.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    int deleteByPrimaryKey(String id);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    int insertSelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    TranHistory selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    int updateByPrimaryKeySelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    int updateByPrimaryKey(TranHistory record);


    /**
     * 保存创建的交易历史
     *
     * @mbggenerated Fri Jan 27 14:29:34 CST 2023
     */
    int insertTranHistory(TranHistory tranHistory);


    /**
     * 根据交易的tranId 查询改交易下历史的明细信息
     * @param tranId
     * @return
     */
    List<TranHistory> selectTranHistoryForDetailByTranId(String tranId);
}