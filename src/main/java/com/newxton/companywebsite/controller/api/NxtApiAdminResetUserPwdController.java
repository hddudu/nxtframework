package com.newxton.companywebsite.controller.api;

import com.newxton.companywebsite.entity.NxtUser;
import com.newxton.companywebsite.service.NxtUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author soyojo.earth@gmail.com
 * @time 2020/7/23
 * @address Shenzhen, China
 * @github https://github.com/soyojoearth/newxton_company_website
 */
@RestController
public class NxtApiAdminResetUserPwdController {

    @Resource
    private NxtUserService nxtUserService;

    @RequestMapping(value = "/api/admin/reset_user_pwd", method = RequestMethod.POST)
    public Map<String, Object> index(@RequestParam("reset_user_id") Long resetUserId,
                                     @RequestParam(value="reset_user_pwd", required=false) String resetUserPwd
    ) {

        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("message", "");

        NxtUser user = nxtUserService.queryById(resetUserId);

        if (user == null){
            result.put("status",44);
            result.put("message","用户不存在");
            return result;
        }

        //和salt一起改
        String saltNew = getRandomString(32);
        String pwdSaltNew = resetUserPwd+saltNew;
        resetUserPwd = DigestUtils.md5Hex(pwdSaltNew).toLowerCase();

        user.setSalt(saltNew);
        user.setPassword(resetUserPwd);

        //更新
        nxtUserService.update(user);

        return result;

    }

    /**
     * 获取随机字符串
     * @param length
     * @return
     */
    public String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_";
        Random random=new Random();
        StringBuffer buffet=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(str.length()-1);
            buffet.append(str.charAt(number));
        }
        return buffet.toString();
    }

}
