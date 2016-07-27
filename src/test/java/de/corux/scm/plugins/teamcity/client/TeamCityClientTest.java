package de.corux.scm.plugins.teamcity.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.mockito.Matchers;

import de.corux.scm.plugins.teamcity.TeamCityConfiguration;
import de.corux.scm.plugins.teamcity.TeamCityContext;
import de.corux.scm.plugins.teamcity.TeamCityGlobalConfiguration;
import sonia.scm.net.ahc.AdvancedHttpClient;
import sonia.scm.net.ahc.AdvancedHttpResponse;
import sonia.scm.net.ahc.BaseHttpRequest;

public class TeamCityClientTest
{
    private TestAdvancedHttpClient httpClient;
    private TeamCityClient teamCityClient;
    private final String url = "http://test.url/teamcity";
    private TestAdvancedHttpRequest expectedRequest;

    private abstract class TestAdvancedHttpClient extends AdvancedHttpClient
    {
        private AdvancedHttpResponse response;
        private BaseHttpRequest<?> request;

        public void setResponse(final AdvancedHttpResponse response)
        {
            this.response = response;
        }

        public BaseHttpRequest<?> getRequest()
        {
            return request;
        }

        @Override
        protected AdvancedHttpResponse request(BaseHttpRequest<?> request) throws IOException
        {
            this.request = request;
            return response;
        }
    }

    private class TestAdvancedHttpRequest extends BaseHttpRequest<TestAdvancedHttpRequest>
    {
        public TestAdvancedHttpRequest(AdvancedHttpClient client, String method, String url)
        {
            super(client, method, url);
        }

        @Override
        protected TestAdvancedHttpRequest self()
        {
            return this;
        }
    }

    public void initMockTeamCityClient() throws IOException
    {
        String username = "test-username";
        String password = "test-password";
        TeamCityConfiguration repoConfiguration = new TeamCityConfiguration(url, "Test", username, password);

        TeamCityGlobalConfiguration globalConfiguration = mock(TeamCityGlobalConfiguration.class);
        when(globalConfiguration.getUrl()).thenReturn(url);

        initMockTeamCityClient(repoConfiguration, globalConfiguration);
        expectedRequest = new TestAdvancedHttpRequest(httpClient, null, url);
        expectedRequest.basicAuth(username, password);
    }

    public void initMockTeamCityClient(TeamCityConfiguration repoConfiguration,
            TeamCityGlobalConfiguration globalConfiguration) throws IOException
    {
        TeamCityContext context = mock(TeamCityContext.class);
        httpClient = mock(TestAdvancedHttpClient.class);
        doCallRealMethod().when(httpClient).post(Matchers.anyString());
        doCallRealMethod().when(httpClient).request(Matchers.any(BaseHttpRequest.class));
        doCallRealMethod().when(httpClient).setResponse(Matchers.any(AdvancedHttpResponse.class));
        doCallRealMethod().when(httpClient).getRequest();

        when(context.getGlobalConfiguration()).thenReturn(globalConfiguration);

        teamCityClient = new TeamCityClient(context, httpClient);
        teamCityClient.updateConfigFromRepository(repoConfiguration);
    }

    @Test
    public void testIndexRepositoryValidStatusCode() throws IOException
    {
        // arrange
        initMockTeamCityClient();
        AdvancedHttpResponse response = mock(AdvancedHttpResponse.class);
        when(response.getStatus()).thenReturn(200);
        httpClient.setResponse(response);

        // assert status code 200
        boolean result200 = teamCityClient.triggerCheckForChanges("TestVCS");
        assertTrue(result200);
        assertTrue(areEqual(expectedRequest, httpClient.getRequest()));
    }

    @Test
    public void testIndexRepositoryInvalidStatusCode() throws IOException
    {
        // arrange
        initMockTeamCityClient();
        AdvancedHttpResponse response = mock(AdvancedHttpResponse.class);
        when(response.getStatus()).thenReturn(401);
        httpClient.setResponse(response);

        // assert failed status code
        boolean result = teamCityClient.triggerCheckForChanges("TestVCS");
        assertFalse(result);
    }

    @Test
    public void testUpdateConfigFromRepositoryOverwritesGlobalConfig() throws IOException
    {
        // arrange
        String username = "test-username";
        String password = "test-password";
        String url = "http://custom-url.example.com:9000/context-path";
        TeamCityConfiguration repoConfiguration = new TeamCityConfiguration(url, "Test", username, password);

        TeamCityGlobalConfiguration globalConfiguration = new TeamCityGlobalConfiguration();
        globalConfiguration.setUrl("http://global-url.example.com");
        globalConfiguration.setUsername("username");
        globalConfiguration.setPassword("password");

        initMockTeamCityClient(repoConfiguration, globalConfiguration);

        expectedRequest = new TestAdvancedHttpRequest(httpClient, null, url);
        expectedRequest.basicAuth(username, password);

        AdvancedHttpResponse response = mock(AdvancedHttpResponse.class);
        when(response.getStatus()).thenReturn(401);
        httpClient.setResponse(response);

        // act + assert
        teamCityClient.triggerCheckForChanges("Test");
        assertTrue(areEqual(expectedRequest, httpClient.getRequest()));
    }

    private boolean areEqual(BaseHttpRequest<?> a, BaseHttpRequest<?> b)
    {
        if (!b.getUrl().startsWith(a.getUrl()))
        {
            return false;
        }

        // check headers
        if (a.getHeaders() != null)
        {
            for (String header : a.getHeaders().keySet())
            {
                if (!b.getHeaders().containsKey(header)
                        || !areHeaderEqual(a.getHeaders().get(header), b.getHeaders().get(header)))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean areHeaderEqual(Collection<String> a, Collection<String> b)
    {
        if (a.size() != b.size())
        {
            return false;
        }

        Iterator<String> aIterator = a.iterator();
        Iterator<String> bIterator = b.iterator();
        while (aIterator.hasNext())
        {
            if (!aIterator.next().equals(bIterator.next()))
            {
                return false;
            }
        }

        return true;
    }
}
