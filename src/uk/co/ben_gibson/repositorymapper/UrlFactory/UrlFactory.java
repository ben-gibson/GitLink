package uk.co.ben_gibson.repositorymapper.UrlFactory;

import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.UrlFactory.Exception.ProjectNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creates Urls from a context for a git host.
 */
public interface UrlFactory
{

    /**
     * Create the Url.
     *
     * @param context   The context to create a Url from.
     * @param forceSSL  Should we enforce SSL if the HTTP protocol is not used in origin?.
     *
     * @return URL
     */
    @NotNull
    URL createUrl(@NotNull Context context, boolean forceSSL) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException, RemoteNotFoundException, ProjectNotFoundException;
}
