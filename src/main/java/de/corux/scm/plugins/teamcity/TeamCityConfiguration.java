package de.corux.scm.plugins.teamcity;

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

    /** Repository property for the TeamCity VCS root. */
    public static final String PROPERTY_TEAMCITY_VCS_ROOT = "teamcity.vcsroot";

    /** Repository property for the TeamCity username. */
    public static final String PROPERTY_TEAMCITY_USERNAME = "teamcity.username";

    /** Repository property for the TeamCity password. */
    public static final String PROPERTY_TEAMCITY_PASSWORD = "teamcity.password";

    /** The TeamCity url. */
    private String url;

    /** The VCS root. */
    private String vcsRoot;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    private Repository repository;

    /**
     * Instantiates a new TeamCity configuration.
     *
     * @param url
     *            the url
     * @param vcsRoot
     *            the VCS root
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public TeamCityConfiguration(final String url, final String vcsRoot, final String username, final String password)
    {
        this.url = url;
        this.vcsRoot = vcsRoot;
        this.username = username;
        this.password = password;
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
        this.username = repository.getProperty(PROPERTY_TEAMCITY_USERNAME);
        this.password = repository.getProperty(PROPERTY_TEAMCITY_PASSWORD);
    }

    /**
     * Return <code>true</code>, if the configuration is valid.
     *
     * @return <code>true</code>, if the configuration is valid
     */
    @Override
    public boolean isValid()
    {
        return Util.isNotEmpty(vcsRoot);
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
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username)
    {
        this.username = username;
        repository.setProperty(PROPERTY_TEAMCITY_USERNAME, username);
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password)
    {
        this.password = password;
        repository.setProperty(PROPERTY_TEAMCITY_PASSWORD, password);
    }
}
