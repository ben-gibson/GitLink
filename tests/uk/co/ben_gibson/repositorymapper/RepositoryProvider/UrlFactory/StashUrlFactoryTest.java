package uk.co.ben_gibson.repositorymapper.RepositoryProvider.UrlFactory;

import com.intellij.testFramework.Parameterized;
import com.intellij.testFramework.UsefulTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.UrlFactory.StashUrlFactory;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests the Stash Url factory.
 */
@RunWith(Parameterized.class)
public class StashUrlFactoryTest extends UsefulTestCase
{

    private Context context;
    private String expectedUrl;

    /**
     * Constructor.
     *
     * @param context      The context.
     * @param expectedUrl  The expected url for the context.
     *
     */
    public StashUrlFactoryTest(Context context, String expectedUrl)
    {
        this.context     = context;
        this.expectedUrl = expectedUrl;
    }


    @Test
    public void failMe()
    {

    }


    @Parameterized.Parameters
    public static Collection contexts()
    {
        return Arrays.asList(new Object[][] {
            { 2, true },
            { 6, false },
            { 19, true },
            { 22, false },
            { 23, true }
        });
    }


    /**
     * Get the Stash url factory.
     *
     * @return StashUrlFactory
     */
    public StashUrlFactory getStashUrlFactory()
    {
        return new StashUrlFactory();
    }
}
