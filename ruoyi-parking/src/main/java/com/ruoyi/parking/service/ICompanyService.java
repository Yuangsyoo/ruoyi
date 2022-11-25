package com.ruoyi.parking.service;

import java.util.List;
import com.ruoyi.parking.domain.Company;

/**
 * 公司基本信息Service接口
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
public interface ICompanyService 
{
    /**
     * 查询公司基本信息
     * 
     * @param id 公司基本信息主键
     * @return 公司基本信息
     */
    public Company selectCompanyById(Long id);

    /**
     * 查询公司基本信息列表
     * 
     * @param company 公司基本信息
     * @return 公司基本信息集合
     */
    public List<Company> selectCompanyList(Company company);

    /**
     * 新增公司基本信息
     * 
     * @param company 公司基本信息
     * @return 结果
     */
    public int insertCompany(Company company);

    /**
     * 修改公司基本信息
     * 
     * @param company 公司基本信息
     * @return 结果
     */
    public int updateCompany(Company company);

    /**
     * 批量删除公司基本信息
     * 
     * @param ids 需要删除的公司基本信息主键集合
     * @return 结果
     */
    public int deleteCompanyByIds(Long[] ids);

    /**
     * 删除公司基本信息信息
     * 
     * @param id 公司基本信息主键
     * @return 结果
     */
    public int deleteCompanyById(Long id);
}
