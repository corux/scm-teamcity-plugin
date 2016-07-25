package de.corux.scm.plugins.teamcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import de.corux.scm.plugins.teamcity.TeamCityConfiguration;
import de.corux.scm.plugins.teamcity.TeamCityContext;
import de.corux.scm.plugins.teamcity.TeamCityGlobalConfiguration;
import sonia.scm.repository.Repository;
import sonia.scm.store.Store;
import sonia.scm.store.StoreFactory;

public class TeamCityContextTest
{
    private TeamCityContext context;

    @SuppressWarnings("unchecked")
    @Before
    public void initTeamCityContext() throws IOException
    {
        Store<TeamCityGlobalConfiguration> store = mock(Store.class);
        StoreFactory storeFactory = mock(StoreFactory.class);
        when(storeFactory.getStore(Matchers.any(Class.class), Matchers.anyString())).thenReturn(store);

        context = new TeamCityContext(storeFactory);
    }

    @Test
    public void testGetTeamCityVcsRootsEmpty() throws IOException
    {
        // arrange
        Repository repository = mock(Repository.class);

        // act
        String result = context.getTeamCityVcsRoot(repository);

        // assert
        assertNull(result);
    }

    @Test
    public void testGetTeamCityVcsRootGetConfiguredVcsRoot() throws IOException
    {
        // arrange
        String vcsRoot = "vcsRoot1";
        Repository repository = mock(Repository.class);
        when(repository.getProperty(Matchers.eq(TeamCityConfiguration.PROPERTY_TEAMCITY_VCS_ROOT))).thenReturn(vcsRoot);

        // act
        String result = context.getTeamCityVcsRoot(repository);

        // assert
        assertEquals(vcsRoot, result);
    }

    @Test
    public void testGetTeamCityVcsRootDefaultRepoName() throws IOException
    {
        // arrange
        String repoName = "ScmRepoName";
        Repository repository = mock(Repository.class);
        TeamCityGlobalConfiguration globalConfiguration = mock(TeamCityGlobalConfiguration.class);
        when(globalConfiguration.useRepositoryNameAsDefault()).thenReturn(true);
        when(repository.getName()).thenReturn(repoName);

        // act
        context.setGlobalConfiguration(globalConfiguration);
        String result = context.getTeamCityVcsRoot(repository);

        // assert
        assertEquals(repoName, result);
    }

    @Test
    public void testGetTeamCityVcsRootDoNotGetDefaultIfTeamCityVcsRootIsConfigured() throws IOException
    {
        // arrange
        String repoName = "ScmRepoName";
        String vcsRoot = "vcsRoot1";
        Repository repository = mock(Repository.class);
        TeamCityGlobalConfiguration globalConfiguration = mock(TeamCityGlobalConfiguration.class);
        when(globalConfiguration.useRepositoryNameAsDefault()).thenReturn(true);
        when(repository.getName()).thenReturn(repoName);
        when(repository.getProperty(Matchers.eq(TeamCityConfiguration.PROPERTY_TEAMCITY_VCS_ROOT))).thenReturn(vcsRoot);

        // act
        context.setGlobalConfiguration(globalConfiguration);
        String result = context.getTeamCityVcsRoot(repository);

        // assert
        assertEquals(vcsRoot, result);
    }
}
