package ValueObjects;

public class StatsFlags {
    private boolean print;
    private boolean save;
    private boolean email;

    public StatsFlags() {
        this.print = false;
        this.save = false;
        this.email = false;
    }

    public boolean shouldPrint() {
        return print;
    }

    public StatsFlags setPrint(boolean print) {
        this.print = print;
        return this;
    }

    public boolean shouldSave() {
        return save;
    }

    public StatsFlags setSave(boolean save) {
        this.save = save;
        return this;
    }

    public boolean shouldSendEmail() {
        return email;
    }

    public StatsFlags setEmail(boolean email) {
        this.email = email;
        return this;
    }
}
