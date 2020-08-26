package com.mine.west.dao;

import com.mine.west.models.Account;
import com.mine.west.models.Pers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    /**
     * 根据主键删除账户
     * @param accountID
     * @return
     */
    int deleteByPrimaryKey(Integer accountID);

    /**
     * 新增账户
     * @param record
     * @return
     */
    int insertAccount(Account record);

    /**
     * 根据主键进行更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Account record);

    /**
     * 根据主键找账户
     * @param accountID
     * @return
     */
    Account selectByPrimaryKey(Integer accountID);

    /**
     * 查询所有账户
     * @return
     */
    List<Account> selectAll();

    /**
     * 管理员：通过用户名/邮箱（模糊）查询账户
     *
     * @param name 用户名/邮箱
     * @return 用户对象信息
     */
    public List<Account> selectAccountLikeName(String name);

    /**
     * 通过用户名查询账户
     *
     * @param name 用户名
     * @return 用户对象信息
     */
    public Account selectAccountByName(String name);


    /**
     * 通过邮箱查询账户
     *
     * @param mailbox 邮箱
     * @return 用户对象信息
     */
    public Account selectAccountByMailbox(String mailbox);

    /**
     * 校验用户名称是否唯一
     *
     * @param name 登录名称
     * @return 结果
     */
    public int checkNameUnique(String name);


    /**
     * 校验 mailbox 是否唯一
     *
     * @param mailbox 账户
     * @return 结果
     */
    public Account checkMailboxUnique(String mailbox);

    Account selectByName(String name);

    Account selectByMailbox(String mailbox);

    /**
     * 根据用户名查询所有角色
     * @param username
     * @return
     */
    Account findRolesByName(String username);
}