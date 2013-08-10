package org.startupDirectory.util
import java.sql.Timestamp
import java.util.Date

class Clock {

  val date = new Date()

  def now: Timestamp = new Timestamp(date.getTime())
}
