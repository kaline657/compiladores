import java.util.*;
import java.util.stream.Collectors;

class Command {
    public enum Type { ADD, SUB, PUSH, POP, PRINT }
    public Type type;
    public String arg = "";

    public Command(String[] command) {
        type = Type.valueOf(command[0].toUpperCase());
        if (command.length > 1) arg = command[1];
    }

    public String toString() {
        return type.name() + " " + arg;
    }
}

public class Interpretador {
    List<String[]> commands;
    Stack<Integer> stack = new Stack<>();
    Map<String, Integer> variables = new HashMap<>();

    public Interpretador(String input) {
        commands = Arrays.stream(input.split("\n"))
                         .map(String::strip)
                         .filter(s -> !s.isEmpty() && !s.startsWith("//"))
                         .map(s -> s.split(" "))
                         .collect(Collectors.toList());
    }

    public boolean hasMoreCommands() {
        return !commands.isEmpty();
    }

    public Command nextCommand() {
        return new Command(commands.remove(0));
    }

    public void run() {
        while (hasMoreCommands()) {
            var command = nextCommand();
            switch (command.type) {
                case ADD:
                    stack.push(stack.pop() + stack.pop());
                    break;
                case SUB:
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a - b);
                    break;
                case PUSH:
                    stack.push(variables.getOrDefault(command.arg, Integer.parseInt(command.arg)));
                    break;
                case POP:
                    variables.put(command.arg, stack.pop());
                    break;
                case PRINT:
                    System.out.println(stack.pop());
                    break;
            }
        }
    }
}
