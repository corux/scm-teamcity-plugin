package de.corux.scm.plugins.teamcity;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import sonia.scm.Validateable;
import sonia.scm.repository.Repository;
import sonia.scm.util.Util;

/**
 * The repository configuration object.
 */
public class TeamCityConfiguration implements Validateable
{
    /** Repository property for the TeamCity server url */
    public static final String PROPERTY_TEAMCITY_URL = "teamcity.url";

    /**
     * Repository property for the TeamCity VCS root.
     */
    public static final String PROPERTY_TEAMCITY_VCS_ROOT = "teamcity.vcsroot";

    /** The TeamCity url. */
    private String url;

    /** The VCS root. */
    private String vcsRoot;

    private Repository repository;

    /**
     * Instantiates a new TeamCity configuration.
     *
     * @param url
     *            the url
     * @param vcsRoot
     *            the VCS root
     */
    public TeamCityConfiguration(final String url, final String vcsRoot)
    {
        this.url = url;
        this.vcsRoot = vcsRoot;
    }

    /**
     * Instantiates a new TeamCity configuration.
     * <p>
     * This constructor reads the properties from the repository and stores it in the configuration object.
     *
     * @param repository
     *            the repository to read the properties from.
     */
    public TeamCityConfiguration(final Repository repository)
    {
        this.repository = repository;
        this.url = repository.getProperty(PROPERTY_TEAMCITY_URL);
        this.vcsRoot = repository.getProperty(PROPERTY_TEAMCITY_VCS_ROOT);
    }

    /**
     * Gets the TeamCity VCS root.
     *
     * @return the VCS root
     */
    public String getVcsRoot()
    {
        return vcsRoot;
    }

    /**
     * Sets the TeamCity VCS root.
     *
     * @param vcsRoot
     *            the new VCS root
     */
    public void setVcsRoot(final String vcsRoot)
    {
        this.vcsRoot = vcsRoot;
        repository.setProperty(PROPERTY_TEAMCITY_VCS_ROOT, vcsRoot);
    }

    /**
     * Sets the TeamCity url.
     *
     * @param url
     *            the new url
     */
    public void setUrl(final String url)
    {
        this.url = url;
        repository.setProperty(PROPERTY_TEAMCITY_URL, url);
    }

    /**
     * Return the url of the TeamCity server.
     *
     * @return url of the TeamCity server
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Gets the url parsed as {@link URL}.
     *
     * @return the parsed url
     */
    public URL getUrlParsed()
    {
        if (StringUtils.isEmpty(getUrl()))
        {
            return null;
        }

        try
        {
            return new URL(getUrl());
        }
        catch (MalformedURLException e)
        {
            throw new TeamCityException(e);
        }
    }

    /**
     * Return true, if the configuration is valid.
     *
     * @return true, if the configuration is valid
     */
    @Override
    public boolean isValid()
    {
        return Util.isNotEmpty(url) && Util.isNotEmpty(vcsRoot);
    }
}
