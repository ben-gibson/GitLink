package uk.co.ben_gibson.git.link.Exception;

public class Codes
{
    private static final int GENERIC_COMPONENT = 0x0003 << 24;
    private static final int URL_COMPONENT = 0x0002 << 24;
    private static final int GIT_COMPONENT = 0x0001 << 24;

    // Git component
    public static final int GIT_REPOSITORY_NOT_FOUND               = GIT_COMPONENT | 0x0004;
    public static final int GIT_COULD_NOT_FIND_URL_FOR_REMOTE      = GIT_COMPONENT | 0x0003;
    public static final int GIT_COULD_NOT_FETCH_BRANCH_FROM_REMOTE = GIT_COMPONENT | 0x0002;
    public static final int GIT_COULD_NOT_FIND_REMOTE              = GIT_COMPONENT | 0x0001;

    // URL component
    public static final int URL_INVALID_AFTER_MODIFICATION      = URL_COMPONENT | 0x0004;
    public static final int URL_FACTORY_CANNOT_CREATE_URL       = URL_COMPONENT | 0x0003;
    public static final int URL_FACTORY_UNSUPPORTED_REMOTE_HOST = URL_COMPONENT | 0x0002;

    // Generic component
    public static final int COULD_NOT_OPEN_URL_IN_BROWSER       = GENERIC_COMPONENT | 0x0001;
}
