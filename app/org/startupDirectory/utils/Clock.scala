package org.startupDirectory.utils
import java.sql.Timestamp
import java.util.Date

class Clock {
  def currentTime: Timestamp = {
   val date = new Date()
	 new Timestamp(date.getTime())
  }
}
