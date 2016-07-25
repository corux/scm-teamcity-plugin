package de.corux.scm.plugins.teamcity.client;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.corux.scm.plugins.teamcity.TeamCityContext;
import de.corux.scm.plugins.teamcity.TeamCityGlobalConfiguration;
import sonia.scm.net.ahc.AdvancedHttpClient;
import sonia.scm.net.ahc.AdvancedHttpRequestWithBody;
import sonia.scm.net.ahc.AdvancedHttpResponse;
import sonia.scm.util.UrlBuilder;

/**
 * Simple client for the TeamCity REST API.
 * 
 * @see https://confluence.jetbrains.com/display/TCD10/REST+API
 */
public class TeamCityClient
{
    private final String baseUrl;
    private String username;
    private String password;
    private final AdvancedHttpClient client;

    /**
     * The logger for {@link TeamCityClient}.
     */
    private static final Logger logger = LoggerFactory.getLogger(TeamCityClient.class);

    @Inject
    public TeamCityClient(final TeamCityContext context, final AdvancedHttpClient client)
    {
        TeamCityGlobalConfiguration configuration = context.getGlobalConfiguration();
        this.client = client;
        this.baseUrl = configuration.getUrl();
    }

    /**
     * Sets the REST API credentials.
     *
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public void setCredentials(final String username, final String password)
    {
        this.username = username;
        this.password = password;
    }

    /**
     * Triggers a check for changes on the given VCS root.
     *
     * @param vcsRoot
     *            the VCS root to update.
     * @return <code>true</code>, if successful; otherwise <code>false</code>.
     * @throws IOException
     *             thrown, when an API call was unsuccessful.
     */
    public boolean triggerCheckForChanges(String vcsRoot) throws IOException
    {
        // request
        String url = new UrlBuilder(baseUrl)
                .append(String.format("/app/rest/debug/vcsCheckingForChangesQueue?locator=vcsRoot:%s", vcsRoot))
                .toString();
        AdvancedHttpRequestWithBody request = client.put(url);
        request.basicAuth(username, password);

        // response
        AdvancedHttpResponse response = request.request();
        int statusCode = response.getStatus();

        if (statusCode == 200)
        {
            return true;
        }
        else
        {
            logger.warn("TeamCity hook failed with statusCode {}", statusCode);
            return false;
        }
    }
}
