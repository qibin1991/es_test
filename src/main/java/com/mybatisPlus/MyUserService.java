package com.mybatisPlus;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.mybatisPlus.mapper.UserMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("myUserService")
public class MyUserService extends MPJBaseServiceImpl<UserMapper, User> {

    @Resource
    UserMapper userMapper;

    // 分页查
    public Object getList(Page page, User user) {
        MPJQueryWrapper<User> persDTOQueryWrapper = new MPJQueryWrapper<>();

//        QueryWrapperUtil.convertQuery(persDTOQueryWrapper,user);
        persDTOQueryWrapper.selectAll(User.class).select("cc.compnay_name", "cc.org_name")
                .leftJoin("left join c_company cc on cc.id = t.company_id").leftJoin("left join c_org co on co.id = t.org_id");

        persDTOQueryWrapper.eq("real_name", "1111")
                .like("", "").le("","").gt("", "")
                .isNotNull("").between("", "", "");
        Page page1 = userMapper.selectPage(page, persDTOQueryWrapper);

        //

        MPJLambdaWrapper<User> mpjLambdaWrapper = new MPJLambdaWrapper<>();

        mpjLambdaWrapper.selectAll(User.class).selectAs(User::getRealName,User::getConsignee)
                .leftJoin(CCompany.class, CCompany::getId, User::getCompanyId).like(User::getRealName, "李")
                .le(User::getCompanyId, "");

        try {
            page1 = userMapper.selectPage(page, mpjLambdaWrapper);
        } catch (Exception e) {
            Throwable cause = e.getCause();

            throw new RuntimeException(e);
        }


        return page1;
    }

    public Object getOne() {
        return userMapper.selectById(1);
//        return baseMapper.selectOne(new QueryWrapper<>().eq("id", 11));
    }
}
