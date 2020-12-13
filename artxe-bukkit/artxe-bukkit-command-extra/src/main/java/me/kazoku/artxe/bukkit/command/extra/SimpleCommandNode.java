package me.kazoku.artxe.bukkit.command.extra;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A simple implementation of {@link CommandNode}
 */
public class SimpleCommandNode implements CommandNode {

    private List<String> aliases;
    private List<String> permissions;
    private Predicate<String> match;
    private CommandExecutor execute;
    private String label;
    private CommandFeedback feedback;
    private List<CommandNode> subcommands;

    /**
     * Create a {@link CommandNode} in simple way
     *
     * @param match       the match predicate
     * @param permissions the permissions
     * @param execute     the execute predicate
     */
    public SimpleCommandNode(Predicate<String> match, List<String> permissions, CommandExecutor execute) {
        this.label = "";
        this.match = match;
        this.permissions = permissions;
        this.aliases = Collections.emptyList();
        this.execute = execute;
        this.feedback = new CommandFeedback();
        this.subcommands = Collections.emptyList();
    }

    public SimpleCommandNode(Predicate<String> match, String permission, CommandExecutor execute) {
        this(match, Collections.singletonList(permission), execute);
    }

    public SimpleCommandNode(Predicate<String> match, CommandExecutor execute) {
        this(match, Collections.emptyList(), execute);
    }

    /**
     * Create a {@link CommandNode} in simple way
     *
     * @param label       the label
     * @param aliases     the aliases
     * @param permissions the permissions
     * @param execute     the execute predicate
     */
    public SimpleCommandNode(String label, List<String> aliases, List<String> permissions, CommandExecutor execute) {
        this.label = label;
        this.match = matchLabel -> MATCH_LABEL.test(this, matchLabel) || MATCH_ALIASES.test(this, matchLabel);
        this.permissions = permissions;
        this.aliases = aliases;
        this.execute = execute;
        this.feedback = new CommandFeedback();
        this.subcommands = Collections.emptyList();
    }

    public SimpleCommandNode(String label, List<String> aliases, String permission, CommandExecutor execute) {
        this(label, aliases, Collections.singletonList(permission), execute);
    }

    public SimpleCommandNode(String label, String alias, String permission, CommandExecutor execute) {
        this(label, Collections.singletonList(alias), Collections.singletonList(permission), execute);
    }

    public SimpleCommandNode(String label, String permission, CommandExecutor execute) {
        this(label, Collections.emptyList(), permission, execute);
    }

    public SimpleCommandNode(String label, CommandExecutor execute) {
        this(label, Collections.emptyList(), Collections.emptyList(), execute);
    }

    @Override
    public String label() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    @Override
    public List<String> permissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean match(String label) {
        boolean matched = match.test(label);
        this.label = matched ? label : this.label;
        return matched;
    }

    public void setMatch(Predicate<String> match) {
        this.match = match;
    }

    @Override
    public CommandFeedback feedback() {
        return this.feedback;
    }

    public void setFeedback(CommandFeedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String... args) {
        return execute.execute(sender, label, args);
    }

    public void setExecutor(CommandExecutor execute) {
        this.execute = execute;
    }

    @Override
    public List<CommandNode> subCommands() {
        return subcommands;
    }

    public void setSubcommands(List<CommandNode> subcommands) {
        this.subcommands = subcommands;
    }
}
