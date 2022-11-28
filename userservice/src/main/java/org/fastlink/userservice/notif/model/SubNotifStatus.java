package org.fastlink.userservice.notif.model;

public enum SubNotifStatus
{
    UNREAD("unread"),
    READ("read");

    private final String status;

    SubNotifStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return this.status;
    }
}
