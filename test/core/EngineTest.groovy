package core

import util.database
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManager

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 2, 2009
 * Time: 9:58:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class EngineTest extends GroovyTestCase {
	def engine

	void setUp() {
		engine = new Engine(db: database.getInstance())
	}

	void testGetList() {
		println engine.getlist([], "deniz.dizman@gmail.com")
	}

	void testGetListReminders() {
		println engine.getlist([0,"reminders"], "deniz.dizman@gmail.com")
	}

	void testGetListDone() {
		println engine.getlist([0,"done"], "deniz.dizman@gmail.com")
	}


	void testGetListPending() {
		println engine.getlist([null, "pending"], "deniz.dizman@gmail.com")
	}
	void testAdd() {
		println engine.add([null,"testing tobo"], "deniz.dizman@gmail.com")
	}
	void testAddWithDate() {
		println engine.add([null,"testing tobo tomorrow"], "deniz.dizman@gmail.com")
	}
	void testComplete() {
		def d = database.getInstance()
		def inc = d.firstRow("select max(pos) as maxpos from todos where status='I'")
		engine.complete([0,inc.maxpos], "deniz.dizman@gmail.com")
	}

	void testDelete() {
		5.times {
			engine.add([null,"testing tobo"], "deniz.dizman@gmail.com")
		}
		def d = database.getInstance()
		def inc = d.firstRow("select pos from todos where jid = 'deniz.dizman@gmail.com' order by rand() limit 5")
		engine.delete([0,inc.pos], "deniz.dizman@gmail.com")
	}

	void testDelDone() {
		5.times {
			engine.add([null,"testing tobo"], "deniz.dizman@gmail.com")
			def d = database.getInstance()
			def inc = d.firstRow("select max(pos) as maxpos from todos where status='I'")
			engine.complete([0,inc.maxpos], "deniz.dizman@gmail.com")			
		}
		engine.deldone([], "deniz.dizman@gmail.com")
	}
	void testHelp() {
		engine.help(null, "deniz.dizman@gmail.com")
	}
	void testSearch() {
		println engine.search([null, "test"], "deniz.dizman@gmail.com")
	}
}