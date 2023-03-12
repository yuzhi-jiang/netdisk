package com.yefeng.netdisk.common.verificationservice;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.yefeng.netdisk.common.util.CheckUtil;

//@Service
public class SmsVerificationCodeSender implements VerificationCodeSender {
    @Override
    public void send(String target, String code) {
        CheckUtil.checkPhone(target);
        // 调用短信发送接口发送验证码
        sent(target,code);

    }
    void sent(String target,String code){
        try {
            com.aliyun.dysmsapi20170525.Client client = createClient("accessKeyId", "accessKeySecret");
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                    .setPhoneNumbers(target)
                    .setSignName("夜枫之家")

                    .setTemplateCode("SMS_206561359").
                    setTemplateParam("{code:\""+code+"\"}");
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            System.out.println(sendSmsResponse.statusCode);
            System.out.println(sendSmsResponse.getBody().getMessage());
        } catch (TeaException error) {
            // 如有需要，请打印 error
            error.printStackTrace();
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            _error.printStackTrace();
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
    public  Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}