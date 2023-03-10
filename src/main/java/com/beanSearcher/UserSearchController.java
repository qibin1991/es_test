package com.beanSearcher;

import com.ejlchina.searcher.MapSearcher;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.util.MapUtils;
import com.es.neo4j.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author QiBin
 * @Date 2022/5/9 18:14
 * @Version 1.0
 **/
//@RestController
//@RequestMapping("/userSearch")
public class UserSearchController {

//    @Autowired
    private MapSearcher mapSearcher;              // 注入 BeanSearcher 的检索器

    @GetMapping("/index")
    public SearchResult<Map<String, Object>> index(HttpServletRequest request) {
        // 一行代码，实现一个用户检索接口（MapUtils.flat 只是收集前端的请求参数）
        return mapSearcher.search(User.class, MapUtils.flat(request.getParameterMap()));
    }

}
