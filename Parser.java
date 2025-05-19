public class Parser {
    private Scanner scan;
    private Token currentToken;
    private StringBuilder output = new StringBuilder();

    public Parser(byte[] input) {
        scan = new Scanner(input);
        currentToken = scan.nextToken();
    }

    private void nextToken() {
        currentToken = scan.nextToken();
    }

    private void match(TokenType t) {
        if (currentToken.type == t) {
            nextToken();
        } else {
            throw new Error("syntax error");
        }
    }

    public void parse() {
        statements();
    }

    private void statements() {
        while (currentToken.type != TokenType.EOF) {
            statement();
        }
    }

    private void statement() {
        if (currentToken.type == TokenType.LET) {
            letStatement();
        } else if (currentToken.type == TokenType.PRINT) {
            printStatement();
        } else {
            throw new Error("syntax error");
        }
    }

    private void letStatement() {
        match(TokenType.LET);
        String id = currentToken.lexeme;
        match(TokenType.IDENT);
        match(TokenType.EQ);
        expr();
        emit("pop " + id);
        match(TokenType.SEMICOLON);
    }

    private void printStatement() {
        match(TokenType.PRINT);
        expr();
        emit("print");
        match(TokenType.SEMICOLON);
    }

    private void expr() {
        term();
        oper();
    }

    private void oper() {
        while (currentToken.type == TokenType.PLUS || currentToken.type == TokenType.MINUS) {
            TokenType op = currentToken.type;
            match(op);
            term();
            emit(op == TokenType.PLUS ? "add" : "sub");
        }
    }

    private void term() {
        if (currentToken.type == TokenType.NUMBER) {
            emit("push " + currentToken.lexeme);
            match(TokenType.NUMBER);
        } else if (currentToken.type == TokenType.IDENT) {
            emit("push " + currentToken.lexeme);
            match(TokenType.IDENT);
        } else {
            throw new Error("syntax error");
        }
    }

    private void emit(String code) {
        output.append(code).append("\n");
    }

    public String output() {
        return output.toString();
    }
}
