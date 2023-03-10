package com.dream.crm.workbench.mapper;

import com.dream.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat Dec 31 16:14:21 CST 2022
     */
    int insertSelective(Activity record);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat Dec 31 16:14:21 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);


    /**
     * 向数据库中存放市场活动数据
     * @param activity 市场活动对象
     * @return
     */
    int insertActivity(Activity activity);


    /**
     * 根据条件查询市场活动的列表
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询有多少条记录
     * @param map
     * @return
     */
    int selectCountOfActivityByCondition(Map<String,Object> map);

    /**
     * 通过 Id 删除指定的记录
     *
     * @mbggenerated Sat Dec 31 16:14:21 CST 2022
     */
    int deleteByPrimaryKey(String[] ids);


    /**
     * 通过 id 查询市场活动
     *
     * @mbggenerated Sat Dec 31 16:14:21 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * 将更新的数据保存
     *
     * @mbggenerated Sat Dec 31 16:14:21 CST 2022
     */
    int updateByPrimaryKey(Activity activity);


    /**
     * 查询所有的市场活动
     * @return
     */
    List<Activity> selectAllActivities();

    /**
     * 通过id选择要导出的市场活动
     * @param ids
     * @return
     */

    List<Activity> selectActivitiesById(String[] ids);


    /**
     * 导入市场活动
     * @param activityList
     * @return
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * 查询市场活动明细
     * @param id
     * @return
     */
    Activity selectActivityForDetailById(String id);

    /**
     * 通过线索id查询市场活动
     * @param id
     * @return
     */
    List<Activity> selectActivityForDetailByClueId(String id);


    /**
     * 根据name模糊查询市场活动，并把已经跟clueId关联过的市场活动排除
     * @param map
     * @return
     */
    List<Activity> selectActivityDetailByNameClueId(Map<String,Object> map);

    /**
     * 根据ids询市场活动
     * @param ids
     * @return
     */
    List<Activity> selectActivityDetailByIds(String[] ids);

    /**
     * 根据name模糊查询市场活动，并且查询那些与clueId关联的市场活动
     * @param map
     * @return
     */
    List<Activity> selectActivityForConvertByNameClueId(Map<String,Object> map);
}