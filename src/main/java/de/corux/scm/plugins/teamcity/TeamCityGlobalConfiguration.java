package de.corux.scm.plugins.teamcity;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import sonia.scm.Validateable;
import sonia.scm.util.Util;

/**
 * The global configuration object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "teamcity-config")
public class TeamCityGlobalConfiguration implements Validateable
{
    @XmlElement(name = "use-repository-name-as-default")
    private boolean useRepositoryNameAsDefault = false;

    private String url;
    private String username;
    private String password;

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

    public void setUrl(final String url)
    {
        this.url = url;
    }

    @Override
    public boolean isValid()
    {
        return Util.isNotEmpty(url);
    }

    public boolean useRepositoryNameAsDefault()
    {
        return useRepositoryNameAsDefault;
    }

    public void setUseRepositoryNameAsDefault(final boolean useRepositoryNameAsDefault)
    {
        this.useRepositoryNameAsDefault = useRepositoryNameAsDefault;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
