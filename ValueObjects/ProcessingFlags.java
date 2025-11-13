package ValueObjects;

public class ProcessingFlags {
    private boolean isNotificationEnabled;
    private boolean email;
    private boolean pdf;

    public ProcessingFlags() {
        this.isNotificationEnabled = false;
        this.email = false;
        this.pdf = false;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public ProcessingFlags setNotificationEnabled(boolean isNotificationEnabled) {
        this.isNotificationEnabled = isNotificationEnabled;
        return this;
    }

    public ProcessingFlags setEmail(boolean email) {
        this.email = email;
        return this;
    }

    public ProcessingFlags setPdf(boolean pdf) {
        this.pdf = pdf;
        return this;
    }

    public boolean isEmail() {
        return email;
    }

    public boolean isPdf() {
        return pdf;
    }
}