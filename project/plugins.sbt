resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

resolvers += "Henkelmann" at "http://maven.henkelmann.eu/"

resolvers += Classpaths.typesafeResolver

libraryDependencies += "eu.henkelmann" % "junit_xml_listener" % "0.2"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")