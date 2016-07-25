package de.corux.scm.plugins.teamcity;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.corux.scm.plugins.teamcity.client.TeamCityClient;
import sonia.scm.plugin.ext.Extension;
import sonia.scm.repository.PostReceiveRepositoryHook;
import sonia.scm.repository.Repository;
import sonia.scm.repository.RepositoryHookEvent;

/**
 * The commit hook to send the TeamCity VCS root update requests.
 */
@Extension
public class TeamCityHook extends PostReceiveRepositoryHook
{
    /**
     * The logger for {@link TeamCityHook}.
     */
    private static final Logger logger = LoggerFactory.getLogger(TeamCityHook.class);

    private final Provider<TeamCityClient> clientProvider;
    private final TeamCityContext context;

    @Inject
    public TeamCityHook(final Provider<TeamCityClient> clientProvider, final TeamCityContext context)
    {
        this.clientProvider = clientProvider;
        this.context = context;
    }

    @Override
    public void onEvent(final RepositoryHookEvent event)
    {
        Repository repository = event.getRepository();
        TeamCityClient client = clientProvider.get();

        if (repository != null)
        {
            String vcsRoot = context.getTeamCityVcsRoot(repository);
            String commonMsg = String.format("SCM repository: %s, TeamCity VCS root: %s", repository.getName(),
                    vcsRoot);
            if (vcsRoot != null)
            {
                String errorMsg = "Failed to execute TeamCity hook. " + commonMsg;
                logger.debug("Executing TeamCity hook. {}", commonMsg);
                try
                {
                    boolean result = client.triggerCheckForChanges(vcsRoot);
                    if (result)
                    {
                        logger.info("Successfully executed TeamCity hook. {}", commonMsg);
                    }
                    else
                    {
                        logger.error(errorMsg);
                    }

                }
                catch (Exception e)
                {
                    logger.error(errorMsg, e);
                }
            }
        }
        else
        {
            logger.error("received hook without repository");
        }
    }
}
