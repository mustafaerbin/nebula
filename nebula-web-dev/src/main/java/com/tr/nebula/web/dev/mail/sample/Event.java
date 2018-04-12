package com.tr.nebula.web.dev.mail.sample;

import com.tr.nebula.mail.MailEvent;
import com.tr.nebula.mail.MailItem;

/**
 * Created by recepkoseoglu on 3/21/17.
 */
public class Event implements MailEvent {
    @Override
    public void before(MailItem item) {

    }

    @Override
    public void success(MailItem item) {

    }

    @Override
    public void error(MailItem item, Exception e) {

    }
}
