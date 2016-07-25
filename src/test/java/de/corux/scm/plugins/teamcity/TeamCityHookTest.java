package de.corux.scm.plugins.teamcity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import de.corux.scm.plugins.teamcity.TeamCityContext;
import de.corux.scm.plugins.teamcity.TeamCityGlobalConfiguration;
import de.corux.scm.plugins.teamcity.TeamCityHook;
import de.corux.scm.plugins.teamcity.client.TeamCityClient;
import sonia.scm.repository.Repository;
import sonia.scm.repository.RepositoryHookEvent;

public class TeamCityHookTest
{
    private TeamCityContext context;
    private TeamCityClient teamCityClient;
    private TeamCityHook teamCityHook;
    private Repository repository;
    private RepositoryHookEvent hookEvent;

    @Before
    public void initTeamCityHook() throws IOException
    {
        context = mock(TeamCityContext.class);
        TeamCityGlobalConfiguration configuration = mock(TeamCityGlobalConfiguration.class);
        when(context.getGlobalConfiguration()).thenReturn(configuration);

        @SuppressWarnings("unchecked")
        Provider<TeamCityClient> teamCityClientProvider = mock(Provider.class);
        teamCityClient = mock(TeamCityClient.class);
        when(teamCityClientProvider.get()).thenReturn(teamCityClient);

        teamCityHook = new TeamCityHook(teamCityClientProvider, context);

        repository = mock(Repository.class);
        hookEvent = mock(RepositoryHookEvent.class);
        when(hookEvent.getRepository()).thenReturn(repository);
    }

    @Test
    public void testOnEventNoRepositoryInEvent() throws IOException
    {
        // arrange
        RepositoryHookEvent hookEventWithoutRepository = mock(RepositoryHookEvent.class);

        // act
        teamCityHook.onEvent(hookEventWithoutRepository);

        // assert
        verify(context, times(0)).getTeamCityVcsRoot(Matchers.any(Repository.class));
        verify(teamCityClient, times(0)).triggerCheckForChanges(Matchers.anyString());
    }

    @Test
    public void testOnEventRepositoryNotLinkedToTeamCity() throws IOException
    {
        // arrange
        when(context.getTeamCityVcsRoot(Matchers.any(Repository.class))).thenReturn(null);

        // act
        teamCityHook.onEvent(hookEvent);

        // assert
        verify(context, times(1)).getTeamCityVcsRoot(Matchers.eq(repository));
        verify(teamCityClient, times(0)).triggerCheckForChanges(Matchers.anyString());
    }
}
