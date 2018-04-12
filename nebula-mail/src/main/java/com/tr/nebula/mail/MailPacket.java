package com.tr.nebula.mail;

import javax.mail.Message;

/**
 * Created by recepkoseoglu on 3/22/17.
 */
public class MailPacket {
    public MailItem mailItem;
    public Message message;

    public MailPacket() {
    }

    public MailPacket(MailItem mailItem, Message message) {
        this.mailItem = mailItem;
        this.message = message;
    }

    public MailPacket(MailItem mailItem) {
        this.mailItem = mailItem;
    }

    public MailPacket(Message message) {
        this.message = message;
    }

    public MailItem getMailItem() {
        return mailItem;
    }

    public void setMailItem(MailItem mailItem) {
        this.mailItem = mailItem;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailPacket that = (MailPacket) o;

        return mailItem.equals(that.mailItem);
    }

    @Override
    public int hashCode() {
        return mailItem.hashCode();
    }

    @Override
    public String toString() {
        return "MailPacket{" +
                "mailItem=" + mailItem +
                '}';
    }
}
