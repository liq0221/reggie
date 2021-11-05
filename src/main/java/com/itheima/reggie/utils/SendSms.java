package com.itheima.reggie.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSms {
    public static void main(String[] args) {
        //用户登录名称 ruiji@1788049635679550.onaliyun.com
        //AccessKey ID LTAI5tAx4JiSX2ab8vpuKgyf
        //AccessKey Secret hpopVLzrX47yfMAVnblmKQjjUlTHfr
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tAx4JiSX2ab8vpuKgyf", "hpopVLzrX47yfMAVnblmKQjjUlTHfr");//自己账号的AccessKey信息
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");//短信服务的服务接入地址
        request.setSysVersion("2017-05-25");//API的版本号
        request.setSysAction("SendSms");//API的名称
        request.putQueryParameter("PhoneNumbers", "18234127031");//接收短信的手机号码
        request.putQueryParameter("SignName", "黑马程序员");//短信签名名称
        request.putQueryParameter("TemplateCode", "SMS_171354585");//短信模板ID
        request.putQueryParameter("TemplateParam", "{\"code\":\"123456\"}");//短信模板变量对应的实际值
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}