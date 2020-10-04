package ro.sebastianrapa.atmapp.model;

import java.util.Date;

public class Log {

    private final String message;
    private final Date createdAt;

    public Log(String message) {
        this.message = message;
        this.createdAt = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
