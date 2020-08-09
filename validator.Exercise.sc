case class User(name: String, age: Int)

type ErrorsOr[A] = Validated[List[String], A]

def getValue(key: String, m: Map[String, String]): Validated[List[String], String] =
  m.get(key) match {
    case Some(s) => s.valid[List[String]]
    case None    => List(s"missing key $key").invalid[String]
  }

def parseInt(s: String): Validated[List[String], Int] =
  Validated.catchOnly[NumberFormatException](s.toInt).leftMap(e => List(s""""${s}" is not a number"""))
def checkInts(i: Int): Validated[List[String], Int] = i.valid[List[String]].ensure(List(s"negative value $i"))(_ >= 0)

def nonBlank(s: String): Validated[List[String], String] =
  if (s != "") {
    s.valid[List[String]]
  } else {
    List("blank string").invalid[String]
  }
def readAge(m: Map[String, String]): Validated[List[String], Int] =
  getValue("age", m).andThen(v => parseInt(v)).andThen(i => checkInts(i))

def readName(m: Map[String, String]): Validated[List[String], String] =
  getValue("name", m).andThen(name => nonBlank(name))

def validator(m: Map[String, String]): Validated[List[String], User] =
  (readName(m), readAge(m)).mapN(User.apply)
