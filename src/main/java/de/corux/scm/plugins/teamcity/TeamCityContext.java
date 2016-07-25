package de.corux.scm.plugins.teamcity;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import sonia.scm.repository.Repository;
import sonia.scm.store.Store;
import sonia.scm.store.StoreFactory;

/**
 * Handles the saving and retrieving of the TeamCity configuration.
 */
@Singleton
public class TeamCityContext
{
    /** Store name for the {@link TeamCityGlobalConfiguration}. */
    private static final String STORE_NAME = "teamcity";

    /** Store for the {@link TeamCityGlobalConfiguration}. */
    private final Store<TeamCityGlobalConfiguration> store;

    /** Global configuration. */
    private TeamCityGlobalConfiguration globalConfiguration;

    /**
     * Constructor.
     *
     * @param storeFactory
     *            the store factory
     */
    @Inject
    public TeamCityContext(final StoreFactory storeFactory)
    {
        store = storeFactory.getStore(TeamCityGlobalConfiguration.class, STORE_NAME);
        globalConfiguration = store.get();

        if (globalConfiguration == null)
        {
            globalConfiguration = new TeamCityGlobalConfiguration();
        }
    }

    /**
     * Gets the configuration for the given repository.
     *
     * @param repository
     *            the repository
     * @return the configuration
     */
    public TeamCityConfiguration getConfiguration(final Repository repository)
    {
        TeamCityConfiguration configuration = new TeamCityConfiguration(repository);
        return configuration;
    }

    /**
     * Gets the global configuration.
     *
     * @return the global configuration
     */
    public TeamCityGlobalConfiguration getGlobalConfiguration()
    {
        return globalConfiguration;
    }

    /**
     * Sets the global configuration.
     *
     * @param globalConfiguration
     *            the new global configuration
     */
    public void setGlobalConfiguration(final TeamCityGlobalConfiguration globalConfiguration)
    {
        this.globalConfiguration = globalConfiguration;
        store.set(globalConfiguration);
    }

    /**
     * Gets the TeamCity VCS root.
     *
     * @param repository
     *            the repository
     * @return the TeamCity VCS root or <code>null</code>, if none is configured.
     */
    public String getTeamCityVcsRoot(final Repository repository)
    {
        TeamCityConfiguration configuration = getConfiguration(repository);
        if (StringUtils.isNotEmpty(configuration.getVcsRoot()))
        {
            return configuration.getVcsRoot();
        }

        if (globalConfiguration.useRepositoryNameAsDefault())
        {
            return repository.getName();
        }

        return null;
    }
}
