/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package site.nicklas.log;

import org.apache.log4j.*;


/**
 * Log appender for Log4j
 * 
 * @author Nicklas Gavelin
 */
public class Log4JLogger implements Logging.Appender
{
    private static final String FROM = Logging.class.getName();
    private Logger logger;
    
    /* (non-Javadoc)
     * @see com.intel.bluetooth.DebugLog.LoggerAppender#appendLog(int, java.lang.String, java.lang.Throwable)
     */
    protected Log4JLogger( )
    {
        logger = Logger.getLogger( Logging.class.getCanonicalName() );
        logger.addAppender( new ConsoleAppender( new PatternLayout( "[ %C ][ %d ][ %p ] %m\n" ) ) );
    }
    
    @Override
    public void log( Logging.Level l, String message, Throwable t )
    {
        switch( l )
        {
            case DEBUG:
                this.logger.log( FROM, Level.DEBUG, message, t ); 
            break;
            case ERROR:
                this.logger.log( FROM, Level.ERROR, message, t ); 
            break;
            case INFO:
                this.logger.log( FROM, Level.INFO, message, t );
            break;
            case WARN:
                this.logger.log( FROM, Level.WARN, message, t );
            break;
        }
    }
    
    /**
     * Returns the current log status
     * 
     * @param level The level to check for
     * @return True if enabled for the given level, false otherwise
     */
    @Override
    public boolean isLogEnabled( Logging.Level level )
    {
        switch( level )
        {
            case DEBUG:
                return this.logger.isDebugEnabled();
            case ERROR:
                return this.logger.isEnabledFor( Level.ERROR );
            case INFO:
                return this.logger.isEnabledFor( Level.INFO );
            case WARN:
                return this.logger.isEnabledFor( Level.WARN );
            default:
                return false;
        }
    }
}
