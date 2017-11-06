package uk.co.ben_gibson.git.link.Logger;

/**
 * Represents a log message.
 */
public class LogMessage
{
    private final String message;
    private final Type type;

    public enum Type {
        NOTICE,
        ERROR,
        WARNING
    }

    private LogMessage(String message, Type type)
    {
        this.message = message;
        this.type    = type;
    }

    static LogMessage notice(String message)
    {
        return new LogMessage(message, Type.NOTICE);
    }

    static LogMessage warning(String message)
    {
        return new LogMessage(message, Type.WARNING);
    }

    public static LogMessage error(String message)
    {
        return new LogMessage(message, Type.ERROR);
    }

    public String toString() {
        return this.message;
    }

    public boolean notice()
    {
        return this.type.equals(Type.NOTICE);
    }

    public boolean error()
    {
        return this.type.equals(Type.ERROR);
    }

    public boolean warning()
    {
        return this.type.equals(Type.ERROR);
    }
}
