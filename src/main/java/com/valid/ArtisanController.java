package com.valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName TestController
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/28 09:28
 * @Version 1.0
 **/
@RestController
@RequestMapping("artisan")
public class ArtisanController {

    @PostMapping
    public ResponseData getTestArtisan(@Validated @RequestBody Artisan artisan){
        return ResponseData.success();
    }

}
