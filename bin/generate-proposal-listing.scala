//> using scala "3.2.2"
//> using lib "com.vladsch.flexmark:flexmark-all:0.64.0"

import java.io.PrintWriter
import java.io.File
import java.nio.file.Paths
import java.util.Collection

import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.{Parser as FlexParser}
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension

import scala.jdk.CollectionConverters.*
import scala.io.Source

/** This is a https://scala-cli.virtuslab.org/ script to iterate through all
  *  the proposals, grab their front matter and then generate the
  *  proposals/README.md file.
  */
@main def generate() =
  val proposalDir = Paths.get(".", "proposals").toFile

  assert(
    proposalDir.isDirectory,
    """|Looks like you're not in the root of this project.
       |Run scala-cli from the root.
       |
       |scala-cli run bin/""".stripMargin
  )

  for
    file <- proposalDir.listFiles.sorted
    if file.isFile &&
      file.getName.endsWith(".md") &&
      file.getName.startsWith("0")
  do
    val contents = Source
      .fromFile(file)
      .getLines
      .mkString(Printer.newline)
    val document = Parser.parser.parse(contents)
    val (date, accepted, updates, status) =
      Parser.retrieveMetaData(document, file)
    Printer.printName(file)
    Printer.printDate(file, date)
    Printer.printAccepted(file, accepted)
    Printer.printUpdates(file, updates)
    Printer.printStatus(file, status)
  end for

  Printer.printWarning()
  Printer.close()

end generate

object Printer:
  private val pw = new PrintWriter(new File("proposals", "README.md"))

  def close() = pw.close()

  // Just for sanity, let's force unix and not use System.lineSeparator or
  // anything like that.
  val newline = "\n"
  val tab = "\t"

  def printName(file: File) =
    pw.write(s"# [${file.getName}](./${file.getName})$newline")

  def printDate(file: File, date: Option[String]) =
    date match
      case Some(d) => pw.write(s"* Date proposed: $d$newline")
      case _       => println(s"""Missing "date" for ${file.getName}""")

  def printAccepted(file: File, accepted: Option[String]) =
    accepted match
      case Some("true" | "yes") => pw.write(s"* Accepted: yes$newline")
      case Some("false" | "no") => pw.write(s"* Accepted: no$newline")
      case Some(_) =>
        println("Invalid value for accepted. Use either true, yes, false, no")
      case _ => println(s"""Missing "accepted" value for ${file.getName}""")

  def printUpdates(file: File, updates: Option[List[String]]) =
    updates match
      case Some(u) =>
        pw.write(s"* Updates:$newline")
        u.foreach(update => pw.write(s"$tab* $update$newline"))
      case None => println(s"No updates found for ${file.getName}")

  def printStatus(file: File, status: Option[String]) =
    status match
      case Some(s) => pw.write(s"* Status: **$s**$newline")
      case _       => println(s"""Missing "status" for ${file.getName}""")

  def printWarning() =
    pw.write(
      s"${newline}_This file is auto-generated. Don't edit here, instead run scala-cli run bin/ to regenerate._"
    )

end Printer

object Parser:
  val rawExtensions: List[Extension] = List(
    YamlFrontMatterExtension.create()
  )

  val extensions: Collection[Extension] = rawExtensions.asJava

  val options =
    new MutableDataSet()
      .set(FlexParser.EXTENSIONS, extensions)

  val parser = FlexParser.builder(options).build()

  enum AcceptedKey(key: String):
    case date extends AcceptedKey("date")
    case accepted extends AcceptedKey("accepted")
    case updates extends AcceptedKey("updates")
    case status extends AcceptedKey("status")
  end AcceptedKey

  def retrieveMetaData(node: Document, file: File) =
    val visitor = new AbstractYamlFrontMatterVisitor()
    visitor.visit(node)
    val data = visitor.getData.asScala

    val metadata: Map[AcceptedKey, List[String]] = visitor
      .getData()
      .asScala
      .toMap
      .foldLeft(Map.empty) { case (mapping, (key, value)) =>
        if value.asScala.toList.nonEmpty && AcceptedKey.values
            .map(_.toString.toLowerCase)
            .contains(key.toLowerCase)
        then mapping + (AcceptedKey.valueOf(key) -> value.asScala.toList)
        else if value.asScala.toList.nonEmpty then
          println(
            s"""|!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                |"$key" in file: ${file.getName} is not an accepted front matter.
                |!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                |
                |The accepted values are:
                |  ${AcceptedKey.values.mkString(", ")}
                |
                |""".stripMargin
          )
          mapping
        else mapping

      }

    // Keep in mind the YAML front matter only supports subset of YAML
    // https://github.com/vsch/flexmark-java/wiki/Extensions#yaml-front-matter
    val date = metadata.get(AcceptedKey.date).flatMap(_.headOption)
    val accepted = metadata.get(AcceptedKey.accepted).flatMap(_.headOption)
    val updates = metadata.get(AcceptedKey.updates)
    val status = metadata.get(AcceptedKey.status).flatMap(_.headOption)

    (date, accepted, updates, status)
  end retrieveMetaData
end Parser
