package com.tr.nebula.mail;

import com.tr.nebula.mail.queue.InMemoryMailQueue;
import com.tr.nebula.mail.queue.MailQueue;
import com.tr.nebula.mail.sender.NebulaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Transport;

/**
 * A queue and consumer thread implementation.
 * Sender thread wakes up on every item insert and works until queue goes empty.
 * Every item in queue will be send only once. If any error occurs it is the developers responsibility to handle it. {@link MailEvent}.
 */
public class MailManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailManager.class);
    private static final MailQueue<MailPacket> queue = new InMemoryMailQueue();
    private static NebulaMailSender sender;
    private static Thread consumerThread = new Thread();
    private static Runnable consumer = new Runnable() {
        @Override
        public void run() {
            LOGGER.debug("Starting mail thread.");

            // Check queue and continue until it is empty.
            while (!queue.isEmpty()) {

                // Take first and remove from queue
                MailPacket packet = queue.poll();
                LOGGER.info("Sending: " + packet.getMailItem().getId() + " (" + queue.size() + ")");

                // Decide to call events.
                boolean hasEvent = packet.getMailItem().getEvent() != null;
                if (hasEvent) {
                    packet.getMailItem().getEvent().before(packet.getMailItem());
                }
                try {
                    // Send it!
                    Transport.send(packet.getMessage());
                    LOGGER.info("Success: " + packet.getMailItem().getId() + " (" + queue.size() + ")");
                    if (hasEvent) {
                        packet.getMailItem().getEvent().success(packet.getMailItem());
                    }
                } catch (Exception e) {
                    LOGGER.info("Failed: " + packet.getMailItem().getId() + " (" + queue.size() + ")");
                    LOGGER.debug(packet.toString(), e);
                    if (hasEvent) {
                        packet.getMailItem().getEvent().error(packet.getMailItem(), e);
                    }
                }
            }
            LOGGER.debug("Stopping mail thread.");
        }
    };


    /**
     * Takes the mail item into the queue and manages the mail sender thread.
     * If thread is alive it will send the mail at the end of current thread queue.
     * Else a new thread will be created and started.
     *
     * @param item MailItem to send.
     * @return true if item added to the queue successfully.
     */
    public static boolean addQueue(MailPacket item) {
        LOGGER.debug("Mail : " + item.toString());
        boolean result = queue.add(item);
        LOGGER.info("Adding mail to queue. Queue size: " + queue.size());

        // If thread is alive leave the job to it
        // Else create new thread and start.
        if (!consumerThread.isAlive()) {
            consumerThread = new Thread(consumer);
            consumerThread.start();
        }
        return result;
    }
}