package uk.co.ben_gibson.git.link.Exception;

public class Codes
{
    public static final int URL_COMPONENT = 0x0002 << 24;
    public static final int GIT_COMPONENT = 0x0001 << 24;

    // Git component
    public static final int GIT_REPOSITORY_NOT_FOUND               = GIT_COMPONENT | 0x0004;
    public static final int GIT_COULD_NOT_FIND_URL_FOR_REMOTE      = GIT_COMPONENT | 0x0003;
    public static final int GIT_COULD_NOT_FETCH_BRANCH_FROM_REMOTE = GIT_COMPONENT | 0x0002;
    public static final int GIT_COULD_NOT_FIND_REMOTE              = GIT_COMPONENT | 0x0001;

    // URL component
    public static final int URL_INVALID_AFTER_MODIFICATION = URL_COMPONENT | 0x0004;
    public static final int URL_CANNOT_CREATE_URL          = URL_COMPONENT | 0x0003;
    public static final int URL_UNSUPPORTED_REMOTE_HOST    = URL_COMPONENT | 0x0002;
    public static final int URL_CANNOT_BE_HANDLED          = URL_COMPONENT | 0x0001;
}
