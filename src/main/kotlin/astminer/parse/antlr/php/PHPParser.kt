package astminer.parse.antlr.php

import astminer.common.model.Parser
import astminer.parse.ParsingException
import astminer.parse.antlr.AntlrNode
import astminer.parse.antlr.convertAntlrTree
import me.vovak.antlr.parser.CaseChangingCharStream
import me.vovak.antlr.parser.PhpLexer
import me.vovak.antlr.parser.PhpParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.InputStream

class PHPParser: Parser<AntlrNode> {
    override fun parseInputStream(content: InputStream): AntlrNode {
        return try {
            val stream = CharStreams.fromStream(content)
            // Php keywords are case-insensitive, so case changing stream must be used
            // Tokens won't be in lower case in resulting tree
            val lexer = PhpLexer(CaseChangingCharStream(stream, false))
            lexer.removeErrorListeners()
            val tokens = CommonTokenStream(lexer)
            val parser = PhpParser(tokens)
            parser.removeErrorListeners()
            val context = parser.htmlDocument()
            convertAntlrTree(context, PhpParser.ruleNames, PhpParser.VOCABULARY)
        } catch (e: Exception) {
            throw ParsingException("ANTLR", "PHP", e.message)
        }
    }

}