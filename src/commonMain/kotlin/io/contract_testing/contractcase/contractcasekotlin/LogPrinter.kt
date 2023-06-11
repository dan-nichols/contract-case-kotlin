package io.contract_testing.contractcase.contractcasekotlin

// TODO: Use a non-JVM library for this
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import io.contract_testing.contractcase.contractcasekotlin.boundary.toBoundaryFailure
import io.contract_testing.contractcase.case_boundary.*

class LogPrinter : ILogPrinter, IResultPrinter {

    private data class Format(val typeColour: AnsiFormat, val messageColour: AnsiFormat)

    override fun log(
        level: String,
        timestamp: String,
        version: String,
        typeString: String,
        location: String,
        message: String,
        additional: String
    ): BoundaryResult = try {
        val (typeColour, messageColour) = when (level) {
            "error" -> Format(typeColour = brightRed, messageColour = brightRed)
            "warn" -> Format(typeColour = brightYellow, messageColour = brightYellow)
            "debug" -> Format(typeColour = cyan, messageColour = cyan)
            "maintainerDebug" -> Format(typeColour = magentaBackground, messageColour = magenta)
            "deepMaintainerDebug" -> Format(typeColour = blueBack, messageColour = blue)
            else -> Format(typeColour = brightRed, messageColour = white)
        }

        val suffix = if (additional.isBlank()) "" else "\n${messageColour.format(additional)}"

        println(
            "$timestamp ${whiteBright.format(version)}${typeColour.format(typeString)} ${blue.format(location)}: ${
                messageColour.format(message)
            }$suffix"
        )
        BoundarySuccess()
    } catch (exception: Exception) {
        exception.toBoundaryFailure()
    }

    override fun printMatchError(description: PrintableMatchError): BoundaryResult = try {
        with(description) {
            println(
                "${redBack.format(" $kind ")} ${whiteBright.format(location)} ${whiteBright.format(message)}"
                    .indentBy(6) +
                        "\n" +
                        "Expected something like:\n${green.format(expected).indentBy(3)}".indentBy(9) +
                        "\n" +
                        "Actual:\n${red.format(actual).indentBy(3)}".indentBy(9) +
                        "\n\n" +
                        white.format(" - $location [$errorTypeTag]").indentBy(12)
            )
        }
        BoundarySuccess()
    } catch (exception: Exception) {
        exception.toBoundaryFailure()
    }

    override fun printMessageError(description: PrintableMessageError): BoundaryResult = try {
        with(description) {
            println(
                "${redBack.format(" $kind ")} ${whiteBright.format(location)} ${whiteBright.format(message)}"
                    .indentBy(6) +
                        "\n\n" +
                        white.format(" - $location [$errorTypeTag]").indentBy(12)
            )
        }
        BoundarySuccess()
    } catch (exception: Exception) {
        exception.toBoundaryFailure()
    }

    override fun printTestTitle(titleDetails: PrintableTestTitle): BoundaryResult = try {
        with(titleDetails) {
            val titleColour = if (kind == "SUCCESS") green else red

            println("${titleColour.format(icon)} ${whiteBright.format(title)}\n$additionalText".indentBy(3))
        }
        BoundarySuccess()
    } catch (exception: Exception) {
        exception.toBoundaryFailure()
    }

    private fun String.indentBy(num: Int): String =
        replace("\n", "\n" + " ".repeat(0.coerceAtLeast(num)))

    companion object {
        private val brightRed = AnsiFormat(Attribute.BRIGHT_RED_TEXT())
        private val brightYellow = AnsiFormat(Attribute.BRIGHT_YELLOW_TEXT())
        private val redBack = AnsiFormat(Attribute.RED_BACK(), Attribute.WHITE_TEXT())
        private val red = AnsiFormat(Attribute.RED_TEXT())
        private val cyan = AnsiFormat(Attribute.CYAN_TEXT())
        private val magentaBackground = AnsiFormat(Attribute.MAGENTA_BACK(), Attribute.BLACK_TEXT())
        private val magenta = AnsiFormat(Attribute.MAGENTA_TEXT())
        private val white = AnsiFormat(Attribute.WHITE_TEXT())
        private val whiteBright = AnsiFormat(Attribute.BRIGHT_WHITE_TEXT())
        private val blue = AnsiFormat(Attribute.BRIGHT_BLUE_TEXT())
        private val blueBack = AnsiFormat(Attribute.BRIGHT_BLUE_BACK(), Attribute.BLACK_TEXT())
        private val green = AnsiFormat(Attribute.GREEN_TEXT())
    }
}
