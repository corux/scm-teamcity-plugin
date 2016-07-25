package de.corux.scm.plugins.teamcity;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

import sonia.scm.security.Role;

/**
 * Handles the global configuration resource.
 */
@Path("plugins/teamcity/global-config")
public class TeamCityGlobalResource
{
    private final TeamCityContext context;

    /**
     * Constructor.
     *
     * @param context
     *            the TeamCity context
     */
    @Inject
    public TeamCityGlobalResource(final TeamCityContext context)
    {
        Subject subject = SecurityUtils.getSubject();

        subject.checkRole(Role.ADMIN);
        this.context = context;
    }

    /**
     * Gets the global configuration.
     *
     * @return the global configuration
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public TeamCityGlobalConfiguration getConfiguration()
    {
        return context.getGlobalConfiguration();
    }

    /**
     * Sets the global configuration.
     *
     * @param configuration
     *            the new configuration
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void setConfiguration(final TeamCityGlobalConfiguration configuration)
    {
        context.setGlobalConfiguration(configuration);
    }
}
