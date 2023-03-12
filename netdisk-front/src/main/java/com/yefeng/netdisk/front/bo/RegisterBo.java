package com.yefeng.netdisk.front.bo;

import lombok.Data;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-12 11:03
 */
@Data
public class RegisterBo {

   private String type;
   private String email;
   private String mobile;
   private String password;
   private String captcha;
   private String username;

}
