package com.mybatisPlus;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/30 15:22
 * @Version 1.0
 **/
@RestController
@RequestMapping("aaa")
public class MyPlusUserController extends AbstractBaseController<MyUserService,User>{

    @Resource
    MyUserService myUserService;

    @RequestMapping("one")
    public Object findObe(){

        return myUserService.getOne();
    }

    @RequestMapping("list")
    public Object findList(Page page,User user){
        return myUserService.getList(page, user);
    }

}
