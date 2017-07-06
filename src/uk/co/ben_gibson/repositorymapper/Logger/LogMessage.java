package uk.co.ben_gibson.repositorymapper.Logger;

/**
 * Represents a log message.
 */
public class LogMessage
{
    private final String message;
    private final Type type;

    public enum Type {
        NOTICE,
        ERROR
    }

    /**
     * Constructor.
     *
     * @param message  The log message.
     * @param type     The log type.
     */
    private LogMessage(String message, Type type)
    {
        this.message = message;
        this.type    = type;
    }

    /**
     * Create a notice message.
     */
    static LogMessage notice(String message)
    {
        return new LogMessage(message, Type.NOTICE);
    }

    /**
     * Create a error message.
     */
    public static LogMessage error(String message)
    {
        return new LogMessage(message, Type.ERROR);
    }

    @Override
    public String toString() {
        return this.message;
    }

    /**
     * Does this represent a notice.
     */
    public boolean isNotice()
    {
        return this.type.equals(Type.NOTICE);
    }

    /**
     * Does this represent a error.
     */
    public boolean isError()
    {
        return this.type.equals(Type.ERROR);
    }
}
