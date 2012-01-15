package core
/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: May 19, 2009
 * Time: 6:58:59 PM
 */

public class config {
    static final LOGLEVEL_ALL = 0
    static final LOGLEVEL_INFO = 1
    static final LOGLEVEL_WARN = 2
    static final LOGLEVEL_ERROR = 3

    static loglevel = LOGLEVEL_ALL //default log level for all classes

    static classFilterLevel = [
            "core.Tobo": LOGLEVEL_ALL,
			"core.Engine": LOGLEVEL_ALL,
			"core.ReminderJob": LOGLEVEL_ALL
    ]
}
