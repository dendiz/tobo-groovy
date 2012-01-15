package core

import core.Engine
import org.jivesoftware.smack.Roster
import org.jivesoftware.smack.RosterListener
import org.jivesoftware.smack.XMPPConnection
import util.Log
import util.database
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.filter.PacketFilter
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.Message
import com.mdimension.jchronic.utils.Span
import org.jivesoftware.smack.Chat
import util.database
import org.jivesoftware.smack.ChatManagerListener

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 2, 2009
 * Time: 5:19:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class Tobo {
	def connection
	def log = Log.getLogger(Tobo.class)
	def welcome = "Hi, this is Tobo. Check out http://tobo.dendiz.com for a list of what I can do for you, or type help for a quick list."
	static instance
	def connected = false
	def db = database.getInstance()
	static getInstance() {
		if (instance == null) {
			instance = new Tobo()
		}
		return instance
	}
	
	def connect(username, password) {
		connection = new XMPPConnection("gmail.com");
		connection.connect();
		log.info("Connecting to XMPP Server. ")
		log.info("Authenticating.")
		connection.login(username+"@gmail.com", password, "toboclientv2");
		log.info("Setting Roster Accept mode to auto.")
		Roster roster = connection.getRoster();

		connection.getRoster().getEntries().each {
			log.info "Roster Entry: "+it
		}
		roster.addRosterListener([
            presenceChanged:{presence ->
		    	log.debug "$presence.from available: $presence.available"
		    },
			entriesAdded: { entry->
				log.info "new user request: " + entry[0]
				def d = db.getInstance()
				d.execute "insert into users (jid) values (${entry[0]})"
				log.info "new user ${entry[0]}added to database."
				def chat = connection.getChatManager().createChat(entry[0], new Engine(db: db.getInstance()))
				chat.sendMessage(welcome)
				roster.reload()

			},
			entriesDeleted: {entry ->
				def d = db.getInstance()
				d.execute "update users set active = 0 where jid = ${entry[0]}"
				log.info "user removed: " + entry[0]
				roster.reload()

			}
		] as RosterListener)
        roster.reload()
		connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all)

		connection.getChatManager().addChatListener( {chat ,local ->
			chat.addMessageListener(new Engine(db: database.getInstance()))
		} as ChatManagerListener)
		connected = true

	}
	def disconnect() {
		connection.disconnect();
	}
}