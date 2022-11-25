package com.ruoyi.parking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.parking.mapper.CompanyMapper;
import com.ruoyi.parking.domain.Company;
import com.ruoyi.parking.service.ICompanyService;

/**
 * 公司基本信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-24
 */
@Service
public class CompanyServiceImpl implements ICompanyService 
{
    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 查询公司基本信息
     * 
     * @param id 公司基本信息主键
     * @return 公司基本信息
     */
    @Override
    public Company selectCompanyById(Long id)
    {
        return companyMapper.selectCompanyById(id);
    }

    /**
     * 查询公司基本信息列表
     * 
     * @param company 公司基本信息
     * @return 公司基本信息
     */
    @Override
    public List<Company> selectCompanyList(Company company)
    {
        return companyMapper.selectCompanyList(company);
    }

    /**
     * 新增公司基本信息
     * 
     * @param company 公司基本信息
     * @return 结果
     */
    @Override
    public int insertCompany(Company company)
    {
        return companyMapper.insertCompany(company);
    }

    /**
     * 修改公司基本信息
     * 
     * @param company 公司基本信息
     * @return 结果
     */
    @Override
    public int updateCompany(Company company)
    {
        return companyMapper.updateCompany(company);
    }

    /**
     * 批量删除公司基本信息
     * 
     * @param ids 需要删除的公司基本信息主键
     * @return 结果
     */
    @Override
    public int deleteCompanyByIds(Long[] ids)
    {
        return companyMapper.deleteCompanyByIds(ids);
    }

    /**
     * 删除公司基本信息信息
     * 
     * @param id 公司基本信息主键
     * @return 结果
     */
    @Override
    public int deleteCompanyById(Long id)
    {
        return companyMapper.deleteCompanyById(id);
    }
}
