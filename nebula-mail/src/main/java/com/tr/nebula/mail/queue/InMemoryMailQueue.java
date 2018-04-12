package com.tr.nebula.mail.queue;

import com.google.common.collect.Queues;
import com.tr.nebula.mail.MailPacket;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A wrapper class for {@link ConcurrentLinkedQueue}
 */
public class InMemoryMailQueue implements MailQueue<MailPacket> {
    private ConcurrentLinkedQueue<MailPacket> queue = Queues.<MailPacket>newConcurrentLinkedQueue();

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean add(MailPacket item) {
        return queue.add(item);
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public MailPacket peek() {
        return queue.peek();
    }

    @Override
    public MailPacket poll() {
        return queue.poll();
    }

    @Override
    public boolean remove(MailPacket item) {
        return queue.remove(item);
    }

    @Override
    public Iterator<MailPacket> iterator() {
        return queue.iterator();
    }

}
