package org.ming.chat.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SessionStore {
    private static Logger log = LoggerFactory.getLogger(SessionStore.class);

    private static SessionStore instance;
    private static Lock lock = new ReentrantLock();

    public static SessionStore buildSessionStore() {
        if (instance == null) {
            lock.lock();

            try {
                if (instance == null) {
                    instance = new SessionStore();
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    private SessionStore() {
        this.map = new ConcurrentHashMap<>();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    protected Map<String, Session> map;
    protected ReadWriteLock readWriteLock;

    public Session get(String k) {
        try {
            readWriteLock.readLock().lock();

            return this.map.get(k);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void put(String k, Session s) {
        try {
            readWriteLock.writeLock().lock();

            this.map.put(k, s);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void remove(String k) {
        try {
            readWriteLock.writeLock().lock();
            this.map.remove(k);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public boolean containsKey(String k) {
        return this.map.containsKey(k);
    }

    public void send(Session s, String msg) {
        RemoteEndpoint.Basic basicRemote = s.getBasicRemote();

        try {
            basicRemote.sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
