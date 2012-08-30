package pt.ist.fenixframework.backend.infinispan;

import org.apache.log4j.Logger;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.ConfigError;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.core.IdentityMap;

/**
 * This is the infinispan configuration manager used by the fenix-framework-backend-infinispan
 * project.
 * 
 * @see Config
 *
 */
public class InfinispanConfig extends Config {
    private static final Logger logger = Logger.getLogger(InfinispanDomainObject.class);

    /**
     * This enumeration lists the possible options for the behaviour of the domain object's {@link
     * IdentityMap}.
     */
    public static enum MapType {
        /**
         * A global {@link IdentityMap}.  An object instance is shared application-wide, i.e. if any
         * two references to a {@link DomainObject} have the same identifier (OID), then they refer
         * the same object (they are <code>==</code>).  The same object instance will not exist more
         * than once in memory.
         */
        SHARED,
            /**
             * A transaction-local {@link IdentityMap}.  Each transaction that accesses a given
             * domain object will get a different copy of that object.  The identity map assotiated
             * with the transaction will be destroyed at the end of the transaction.
             */
            LOCAL };

    /**
     * This <strong>optional</strong> parameter specifies whether the object identity map to use
     * should have either a global (<code>SHARED</code>) or a transaction-local (<code>LOCAL</code>)
     * scope.  The default value for this parameter is {@link MapType#SHARED}.
     */
    protected MapType identityMap = MapType.SHARED;

    /**
     * This <strong>required</strong> parameter specifies the location of the XML file used to
     * configure Infinispan.  This file should be available in the application's classpath.
     */
    protected String ispnConfigFile = null;

    protected final InfinispanBackEnd backEnd;

    public InfinispanConfig() {
        this.backEnd = new InfinispanBackEnd();
    }

    // process this config's parameters

    protected void identityMapFromString(String value) {
        String cleanValue = value.trim().toUpperCase();
        try {
            identityMap = MapType.valueOf(cleanValue);
        } catch (IllegalArgumentException e) {
            String message = "Unknown value for configuration property 'identityMap': " + value;
            logger.fatal(message);
            throw new ConfigError(message, e);
        }
    }

    public String getIspnConfigFile() {
        return this.ispnConfigFile;
    }

    @Override
    protected void init() {
        this.backEnd.configInfinispan(this);
        // DomainClassInfo.initializeClassInfos(FenixFramework.getDomainModel(), 0);
    }

    @Override
    public InfinispanBackEnd getBackEnd() {
        return this.backEnd;
    }

}
