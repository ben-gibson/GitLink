package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.RemoteRepositoryMapperException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * An interface for remote repository url factories.
 */
public interface UrlFactory
{

    /**
     * Get a remote repository url from a context.
     *
     * @param context The context to create a url from.
     *
     * @return URL
     */
    @NotNull
    URL getUrlFromContext(@NotNull Context context) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException, RemoteRepositoryMapperException;
}
