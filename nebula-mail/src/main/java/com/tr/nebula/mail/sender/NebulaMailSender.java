package com.tr.nebula.mail.sender;

import com.tr.nebula.mail.MailItem;

/**
 * Created by recepkoseoglu on 3/22/17.
 */
public interface NebulaMailSender {
    void sendMail(MailItem mailItem);
}
