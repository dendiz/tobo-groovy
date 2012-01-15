import core.Tobo
import org.jivesoftware.smack.XMPPConnection
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.SimpleTrigger
import core.ReminderJob

//TOBO version 2.

//XMPPConnection.DEBUG_ENABLED = true

class ToboLauncher {
	public static void main(String[] argv) {
		def t1 = Tobo.getInstance()
		static conf = new ConfigSlurper().parse(new File('/etc/tobo.properties').toURL())
//		t1.connect("tobo.bot.test", "supersecretpassword")
		t1.connect(conf.xmppuser, conf.xmpppassword)
		assert t1.connected == true
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		JobDetail reminderJob = new JobDetail("reminderJob", Scheduler.DEFAULT_GROUP, ReminderJob.class);
		Trigger reminderTrigger = new SimpleTrigger("reminderTrigger", Scheduler.DEFAULT_GROUP, new Date(), null,
							SimpleTrigger.REPEAT_INDEFINITELY, 60000);
		scheduler.scheduleJob(reminderJob, reminderTrigger);

		while(true) {
			sleep(1000)
		}
	}
}
