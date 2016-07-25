package de.corux.scm.plugins.teamcity;

/**
 * The exception used by the TeamCity plugin.
 */
public class TeamCityException extends RuntimeException
{
    private static final long serialVersionUID = -6916438890792326995L;

    /**
     * Instantiates a new TeamCity exception.
     *
     * @param cause
     *            the cause
     */
    public TeamCityException(Throwable cause)
    {
        super(cause);
    }
}
