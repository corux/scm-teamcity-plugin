package de.corux.scm.plugins.teamcity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

    @Override
    public boolean isValid()
    {
        return Util.isNotEmpty(url);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(final String url)
    {
        this.url = url;
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
