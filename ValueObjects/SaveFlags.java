package ValueObjects;

public class SaveFlags {
    private boolean email;
    private boolean pdf;
    private boolean backup;

    public SaveFlags() {
        this.email = false;
        this.pdf = false;
        this.backup = false;
    }

    public boolean isEmail() {
        return email;
    }

    public SaveFlags setEmail(boolean email) {
        this.email = email;
        return this;
    }

    public boolean isPdf() {
        return pdf;
    }

    public SaveFlags setPdf(boolean pdf) {
        this.pdf = pdf;
        return this;
    }

    public boolean isBackup() {
        return backup;
    }

    public SaveFlags setBackup(boolean backup) {
        this.backup = backup;
        return this;
    }
}
