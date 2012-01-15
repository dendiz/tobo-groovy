import com.mdimension.jchronic.utils.Span
import com.mdimension.jchronic.Chronic
import util.database
import java.text.DateFormat
import java.text.SimpleDateFormat
import util.database

/**
 * Created by IntelliJ IDEA.
 * User: dendiz
 * Date: Jun 3, 2009
 * Time: 3:59:46 AM
 * To change this template use File | Settings | File Templates.
 */
Span span = Chronic.parse("pick up the milk and then go home proxy apache hyperion add move")
def c = span.getBeginCalendar()
println c

println new GregorianCalendar()
