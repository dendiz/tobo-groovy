package core

import org.quartz.Job
import org.quartz.JobExecutionContext
import util.database
import util.database
import util.Log
import org.jivesoftware.smack.packet.Message

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 4, 2009
 * Time: 12:10:55 AM
 * To change this template use File | Settings | File Templates.
 */

public class ReminderJob implements Job{

	public void execute(JobExecutionContext jobExecutionContext) {
		def d = database.getInstance()
		def reminders = []
		def t = Tobo.getInstance()
		def log = Log.getLogger(ReminderJob.class)
		log.debug "Running reminder job"
		if (!t.connected) {
			log.debug "Tobo is not connected. Exiting."
			return
		}
		d.rows("select * from reminders where timestampdiff(minute, rdate, now()) = 0").each {
			log.debug  "gonna send reminder " + it.id
			d.execute "update reminders set processed = 1 where id = ${it.id}"
			reminders << it.todo_id
		}

		reminders.each { todoid ->
			d.rows("select jid, todo from todos where id = ${todoid}").each {
				Message message = new Message()
				message.setTo(it.jid)
				message.setSubject("reminder");
				message.setBody("Reminder: "+it.todo);
				t.connection.sendPacket(message);
			}
		}
	}
}