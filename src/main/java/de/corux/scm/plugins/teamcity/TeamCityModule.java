package de.corux.scm.plugins.teamcity;

import com.google.inject.AbstractModule;

import de.corux.scm.plugins.teamcity.client.TeamCityClient;
import sonia.scm.plugin.ext.Extension;

@Extension
public class TeamCityModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(TeamCityContext.class);
        bind(TeamCityClient.class);
    }
}
