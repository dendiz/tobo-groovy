import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Roster
import util.Log

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 3, 2009
 * Time: 12:49:44 AM
 * To change this template use File | Settings | File Templates.
 */
log = Log.getLogger(this.class)
username = "tobo.bot.test"
password = "supersecretpassword"
connection = new XMPPConnection("gmail.com");
connection.connect();
log.info("Connecting to XMPP Server. ")
log.info("Authenticating.")
connection.login(username+"@gmail.com", password, "toboclientv2");
log.info("Setting Roster Accept mode to auto.")
Roster roster = connection.getRoster();
println "!!!! removing roster entires. !!!!!!!"
roster.getEntries().each {
	connection.getRoster().removeEntry(it)
}
