package core

import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import util.Log
import util.database
import com.mdimension.jchronic.Chronic
import com.mdimension.jchronic.utils.Span
import java.util.logging.*
import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.packet.Packet

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 2, 2009
 * Time: 5:19:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class Engine implements MessageListener , PacketListener{

	public void processPacket(Packet packet) {
		def chat = Tobo.getInstance().connection.get
	}

	def log = Log.getLogger(Engine.class)
	def db
	public void processMessage(Chat chat, Message message) {
		assert db != null
		if (message.getType() == Message.Type.error) return

		def match
		def rulesMap = [
		        "add"     : /(?i)^add\s\"(.*)\"/,
				"delete"  : /(?i)^del\s([0-9]+)/,
				"getlist" : /(?i)^getlist\s*(done|complete|incomplete|all|pending|reminders)*/,
				"help"    : /(?i)^help\s*(.*)/,
				"complete": /(?i)^complete\s([0-9]+)/,
				"done"	  : /(?i)^done\s([0-9]+)/,
				"search"  : /(?i)^search\s*(.*)/,
				"reminder": /(?i)^(remind|reminder)\s([0-9]*)\s*(.*)/,
				"deldone" : /(?i)^deldone.*$/
		]
		def response = "Unknown command. Please visit http://tobo.dendiz.com for available commands and usage information.";
		def messageBody = message.getBody()
		if (messageBody == null) return
		log.debug("Received message:"+messageBody)
		for (cmd in rulesMap) {
			match = messageBody =~ cmd.value
			if (match.size() > 0) {
				log.debug("command was "+match[0])
				def participant = chat.getParticipant()
				if (participant.indexOf('/') > 0) {
					participant = participant[0..participant.indexOf('/')-1]
				}
				response = "${cmd.key}"(match[0], participant)
				break;
			}
		}
		chat.sendMessage(response)
	}

	def add(param, jid) {
		Logger.getLogger("groovy.sql").level = Level.FINE
		log.debug "(${jid}) adding item: "+param[1]
		def row = db.firstRow("select max(pos) as maxpos from todos where jid = ${jid}")
		if (row.maxpos == null) row.maxpos = 1
		log.debug "Max position is " + row.maxpos
		Span span = Chronic.parse(param[1] as String)
		def sqldate
		if (span != null) {
			def c = span.getBeginCalendar()
			def date = c.get(Calendar.DAY_OF_MONTH)
			def month = c.get(Calendar.MONTH) + 1
			def year = c.get(Calendar.YEAR)
			def hour = c.get(Calendar.HOUR_OF_DAY)
			def minute = c.get(Calendar.MINUTE)
			log.debug("found a date for item, adding a reminder. ${date}/${month}/${year} ${hour}:${minute}" )
			if (hour < 10) hour = "0"+hour
			if (minute < 10) minute = "0"+minute
			if (date < 10) date = "0"+date
			if (month < 10) month = "0"+month
			sqldate = year+"-"+month+"-"+date+" "+hour+":"+minute+":00"
			param[1] += " (${sqldate})"
 		}
		db.execute "insert into todos (pos,jid,todo) values (?,?,?)", [row.maxpos+1, jid, param[1]]
		if (span != null) {
			row = db.firstRow("select max(id) as maxid from todos where jid = ${jid}")
			log.debug("Reminder for todo id: "+ row.maxid +  " sql date: " + sqldate)
			db.execute "insert into reminders (todo_id, rdate) values (?,?)", [row.maxid, sqldate]
			log.debug("inserted reminder.")
			return "Ok. Added reminder for ${sqldate} too."
		}
		return "Ok."
	}

	def search(param, jid) {
		def st = param[1]
		log.debug "(${jid}) searching for item "+st
		def list = []
		db.rows("""select * from todos where jid = ${jid}""").each {
			if (it.todo =~ st)
				list << "${it.pos} : (${it.status}) ${it.todo} "
		}
		log.debug("search list " + list)
		return list.join("\n")

	}

	def getlist(param, jid) {
		log.debug("(${jid}) getlist.")
		def sql = "select * from todos where jid = ? "
		def condition = " order by pos asc"
		def list = []
		if (param[1] != null) {
			if (param[1] == "done") condition = " and status = 'C'  order by pos asc"
			if (param[1] == "pending") condition = " and status = 'I'  order by pos asc"
			if (param[1] == "reminders") sql =
				"""select pos,todo,status,rdate from todos t, reminders r where r.todo_id = t.id and t.jid = ? and r.processed = 0 """
		}
		db.rows(sql + condition, [jid]).each {
			list << "${it.pos} : (${it.status}) ${it.todo} " + (it.containsKey("rdate") ? " Reminder:"+it.rdate : "")
		}
		if (list.size() == 0 ) return "No items in list."
		return list.join("\n")
	}

	def done(param, jid) {
		return complete(param, jid)
	}
	def complete(param, jid) {
		log.debug "(${jid}) completed for item "+param[1]
		db.execute("update todos set status = 'C' where jid = ${jid} and pos = ${param[1]}")
		return "ok."
	}

	def delete(param, jid) {
		log.debug("(${jid}) delete "+param[1])
		db.execute("insert into deleted_todos (select * from todos where jid = ${jid} and pos = ${param[1]})")
		db.execute("delete from todos where jid = ${jid} and pos = ${param[1]}")
		return "Ok."

	}

	def help(param, jid) {
		def h = "Please visit http://tobo.dendiz.com for available commands and usage information\n"
		h += "tobo understands: add, getlist, complete, done, search, remind, deldone"
		return h
	}

	def reminder(param, jid) {
		log.debug "(${jid}) reminder "+param[1]
		if (param[2] == "remove") {

		}
		return "reminder for item "+param[2]

	}

	def deldone(param, jid) {
		log.debug("(${jid}) deldone")
		db.rows("select * from todos where status='C' and jid = ${jid}").each {
			delete([null, it.pos], jid)
		}
		return "ok. "
	}
}
