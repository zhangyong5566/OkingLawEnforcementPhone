package com.zhang.okinglawenforcementphone;


import android.support.annotation.NonNull;
import android.util.Log;

import com.zhang.okinglawenforcementphone.email.MailInfo;
import com.zhang.okinglawenforcementphone.email.MailSender;

import java.io.File;


/**
 * Created by Administrator on 2018/5/23/023.
 */

public class SendEmailManager {
    //qq
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "1010472239@qq.com";
    private static final String FROM_PSW = "kpqqarhitqcibcjg";

//    //163
//    private static final String HOST = "smtp.163.com";
//    private static final String PORT = "465"; //或者465  994
//    private static final String FROM_ADD = "teprinciple@163.com";
//    private static final String FROM_PSW = "teprinciple163";
////    private static final String TO_ADD = "2584770373@qq.com";


    public static boolean send(final File file, String toAdd, String subject, String content) {
        final MailInfo mailInfo = creatMail(toAdd, subject, content);
        final MailSender sms = new MailSender();
        return sms.sendFileMail(mailInfo, file);
    }


    public static boolean send(String toAdd, String subject, String content) {
        final MailInfo mailInfo = creatMail(toAdd, subject, content);
        final MailSender sms = new MailSender();
        return sms.sendTextMail(mailInfo);

    }

    @NonNull
    private static MailInfo creatMail(String toAdd, String subject, String content) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject(subject); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }

}
