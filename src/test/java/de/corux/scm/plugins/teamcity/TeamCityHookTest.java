package de.corux.scm.plugins.teamcity;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

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
        verify(context, never()).getTeamCityVcsRoot(any(Repository.class));
        verify(teamCityClient, never()).triggerCheckForChanges(anyString());
    }

    @Test
    public void testOnEventRepositoryNotLinkedToTeamCity() throws IOException
    {
        // arrange
        when(context.getTeamCityVcsRoot(any(Repository.class))).thenReturn(null);

        // act
        teamCityHook.onEvent(hookEvent);

        // assert
        verify(context).getTeamCityVcsRoot(repository);
        verify(teamCityClient, never()).triggerCheckForChanges(anyString());
    }
}
