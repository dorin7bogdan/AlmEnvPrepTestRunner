package com.opentext.automation.ado;

public abstract class AbstractTask {
    private final String PasswordPrefix = "pass:";

    public AbstractTask() {
    }

    public abstract void parseArgs(String[] var1) throws Exception;

    public abstract void execute() throws Throwable;

    protected String extractPasswordFromParameter(String arg) {
        return this.extractvalueFromParameter(arg, "pass:");
    }

    protected String extractvalueFromParameter(String arg, String prefix) {
        int pl = prefix.length();
        if (arg != null && !arg.isEmpty() && arg.length() >= pl && arg.substring(0, pl).toLowerCase().equals(prefix)) {
            return arg.substring(pl);
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected String GetStringParameter(String[] args, String prefix) {
        String[] var3 = args;
        int var4 = args.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String parameter = var3[var5];
            if (parameter.contains(prefix)) {
                return parameter.substring(prefix.length());
            }
        }

        return "";
    }

    protected boolean GetBoolParameter(String[] args, String prefix, boolean defaultValue) {
        String[] var4 = args;
        int var5 = args.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String parameter = var4[var6];
            if (parameter.contains(prefix)) {
                return Boolean.parseBoolean(args[10].toLowerCase());
            }
        }

        return defaultValue;
    }
}
