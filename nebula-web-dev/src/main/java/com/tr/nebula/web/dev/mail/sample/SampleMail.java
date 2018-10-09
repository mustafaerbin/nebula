package com.tr.nebula.web.dev.mail.sample;

import com.tr.nebula.mail.MailItem;
import com.tr.nebula.mail.sender.NebulaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Mustafa Erbin on 3/24/17.
 */
public class SampleMail {

    @Qualifier("mailName")
    @Autowired
    NebulaMailSender nebulaMailSender;

    public void sendMail() {
        MailItem item = new MailItem();
        item.setBody("<h1>Hello world</h1>");
        item.setTitle("Hello");
        item.setReceivers("name.surname@company.com");
        item.setEvent(new Event());
        nebulaMailSender.sendMail(item);
    }
}
