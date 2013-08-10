Database Design
===============

## Login Info ("login")

  id 
  name 
  email 
 
  loginType {password, fb, gmail, linkedId}
  loginSecret 

  lastLogin  [timestamp]
  dateJoined [timestamp]
 
  columnType {person, business, investor}

## Person ("person")

  id
  age
  location
  description
  webpage
  fb, twitter, g+, lkdn

  lastUpdated
 
  jobHistory (??)
 
## Business ("business")

  id
  age
  location
  description
  webpage
  fb, twitter, g+, lkdn

  lastUpdated
 
  financing

  employers -> ["toMany"]
  investors -> ["toMany"]
 
## Investors ("investors")

  id
  age
  location
  description
  webpage
  fb, twitter, g+, lkdn

  lastUpdated

  capital

  employers -> ["toMany"]
  business  -> ["toMany"]

#### Unified type ("entity")

  id
  age
  location
  description
  webpage
  jobType {
    person -> "all type of jobs"
    business -> {startup, business}
    investor -> {angel, VC, bank}
  }
  fb, twitter, g+, lkdn

  lastUpdated

  capital
  entityType {person, business, investor}
  jobHistory (??)

  externalEntities -> ["toMany"]

## Old design

{
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def email = column[String]("email")
  def description = column[Option[String]]("email")

  /* index */
  def idx = index("idx_id", id, unique=true)

  def workStatus = column[Int]("work_status") // -1
  def size = column[Int]("size")
  def age = column[Int]("age")

  def isActive = column[Int]("is_active")
  def isStaff = column[Int]("is_staff")

  /* social */
  def webPage = column[Option[String]]("web_page")
  def github = column[Option[String]]("github_url")
  def fb = column[Option[String]]("fb_url")
  def fbId = column[Option[String]]("fb_id")
  def gPlus = column[Option[String]]("g_plus_url")

  def lastLogin = column[Timestamp]("last_login")
  def lastUpdated  = column[Timestamp]("last_updated")
  def dateJoined = column[Timestamp]("date_joined")

  def contactMe = column[Int]("contact_me")
  def emailMe = column[Int]("email_me")
}
