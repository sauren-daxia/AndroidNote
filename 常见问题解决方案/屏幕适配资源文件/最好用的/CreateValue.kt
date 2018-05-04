import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Created by marti on 2018/3/1.
 */

fun main(args: Array<String>) {
    inputXml(File("").absolutePath)
}

fun inputXml(directoryPath: String) {
    listOf("values" to 1f, "values-xxhdpi" to 2f, "values-xxxhdpi" to 3f).forEach { item ->
        var directory = File(directoryPath, item.first)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        var valueFile = File(directory.absolutePath, "dimens.xml")
        if (!valueFile.exists()) {
            valueFile.createNewFile()
        }
        BufferedWriter(FileWriter(valueFile)).use {
            var sb = StringBuilder()
            sb
                    .append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                    .append("\n")
                    .append("<resources>")
                    .append("\n")

            (1..1920).forEach {
                sb.append("\t<dimen name=\"px${it}\">${String.format("%.1f", (it / item.second))}dp</dimen>").append("\n")
            }
            sb.append("</resources>")
            println(sb.toString())
            it.write(sb.toString())
            it.flush()
        }
    }
}