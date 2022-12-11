package com.ruoyi.parking.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公司基本信息对象 company
 * 
 * @author ruoyi
 * @date 2022-11-24
 */

public class Company extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String name;

    /** 公司地址 */
    @Excel(name = "公司地址")
    private String adress;

    /** 公司邮箱 */
    @Excel(name = "公司邮箱")
    private String email;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAdress(String adress) 
    {
        this.adress = adress;
    }

    public String getAdress() 
    {
        return adress;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("adress", getAdress())
            .append("email", getEmail())
            .toString();
    }
}
