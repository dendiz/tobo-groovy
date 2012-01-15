package util

import core.config

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: May 27, 2009
 * Time: 6:26:36 PM
 */

public class Log {
    def clazz

    static getLogger(def clazz) {
        return new Log(clazz.getCanonicalName())
    }

    public Log(def cname) {
        clazz = cname
    }

    private isPrintable(lvl) {
        def filter = false
        def defaultFilter = false
        if (config.classFilterLevel.containsKey(clazz)) {
            if (lvl >= config.classFilterLevel[clazz]) {
                filter = true
            }
        } else {
            if (config.loglevel >= lvl) {
                defaultFilter = true
            }
        }
        return filter || defaultFilter


    }

    public info(def message) {

        if (!isPrintable(config.LOGLEVEL_INFO)) return
        msg("info", message)
    }

    public debug(def message) {
        if (!isPrintable(config.LOGLEVEL_ALL)) return
        msg("debug", message)
    }

    public warn(def message) {
        if (!isPrintable(config.LOGLEVEL_WARN)) return
        msg("warn", message)
    }

    public error(def message) {
        msg("error", message)
    }



    private msg(def level, def message) {
        def logstr = "[" + new Date() + "]\t[${clazz}] (${level}): " + message
		new File("tobo.log") << logstr << "\n"
		println logstr
    }
}
