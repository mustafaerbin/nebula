package com.tr.nebula.mail.sender;

import com.tr.nebula.mail.MailConfiguration;
import com.tr.nebula.mail.MailItem;
import com.tr.nebula.mail.MailManager;
import com.tr.nebula.mail.MailPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by recepkoseoglu on 3/22/17.
 */
public class NebulaMailSenderImpl implements NebulaMailSender, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaMailSender.class);
    private static final Properties PROPERTIES = new Properties();
    private final MailConfiguration configuration;
    private Session session;

    /**
     * @param configuration mail configuration
     */
    public NebulaMailSenderImpl(MailConfiguration configuration) {
        checkNotNull(configuration);
        this.configuration = configuration;
        setProperties();
    }

    public MailConfiguration getConfiguration() {
        return configuration;
    }

    private void setProperties() {
        LOGGER.debug("Setting configuration.");
        LOGGER.debug(configuration.toString());

        PROPERTIES.putAll(configuration.getProperties());


        session = Session.getDefaultInstance(PROPERTIES, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        PROPERTIES.get(configuration.getUsernameKey()).toString()
                        , PROPERTIES.get(configuration.getPasswordKey()).toString());
            }
        });
    }


    /**
     * Sends a mail with the given item.
     *
     * @param item MailItem to be send.
     */
    public void sendMail(MailItem item) {
        try {
            checkNotNull(item.getReceivers());
            checkNotNull(item.getReceivers().get(0));
            checkNotNull(item.getTitle());
            checkNotNull(item.getBody());

            //If sender is empty send with the account sender.
            Message msg = new MimeMessage(session);

            if (item.getSender() == null || item.getSender().length() == 0) {
                item.setSender(configuration.getProperties().get(configuration.getUsernameKey()));
            }

            InternetAddress from = new InternetAddress(item.getSender());
            msg.setFrom(from);

            InternetAddress[] to = new InternetAddress[item.getReceivers().size()];
            for (int i = 0; i < item.getReceivers().size(); i++) {
                to[i] = new InternetAddress(item.getReceivers().get(i));
            }
            msg.setRecipients(Message.RecipientType.TO, to);

            msg.setSubject(item.getTitle());

            MimeBodyPart body = new MimeBodyPart();
            body.setContent(item.getBody(), "text/html; charset=UTF-8");

            Multipart content = new MimeMultipart();
            content.addBodyPart(body);

            if (item.getAttachments() != null && item.getAttachments().size() > 0) {
                for (DataSource attachment : item.getAttachments()) {
                    BodyPart itemBodyPart = new MimeBodyPart();
                    itemBodyPart.setDataHandler(new DataHandler(attachment));
                    itemBodyPart.setFileName(attachment.getName());
                    content.addBodyPart(itemBodyPart);
                }
            }
            msg.setContent(content);
            msg.setSentDate(new Date());
            msg.saveChanges();

            MailPacket mailPacket = new MailPacket(item, msg);

            MailManager.addQueue(mailPacket);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
