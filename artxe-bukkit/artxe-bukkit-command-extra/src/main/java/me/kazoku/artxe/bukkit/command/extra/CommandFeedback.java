package me.kazoku.artxe.bukkit.command.extra;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Feedback for {@link CommandNode}
 */
public class CommandFeedback implements Cloneable {
  /**
   * Unknown command
   */
  public final Feedback INVALID_COMMAND;

  /**
   * Only the player can use the command
   */
  public final Feedback ONLY_PLAYER;

  /**
   * No permission
   */
  public final Feedback NO_PERMISSION;

  /**
   * Too many arguments for the command
   */
  public final Feedback TOO_MANY_ARGUMENTS;

  /**
   * Too few arguments for the command (Optional)
   */
  public final Feedback TOO_FEW_ARGUMENTS;

  /**
   * Command success (Optional)
   */
  public final Feedback COMMAND_SUCCESS;

  /**
   * Command failure (Optional)
   */
  public final Feedback COMMAND_FAILURE;

  public CommandFeedback() {
    this.INVALID_COMMAND = new Feedback("§cUnknown command");
    this.ONLY_PLAYER = new Feedback("§cOnly player can use this command");
    this.NO_PERMISSION = new Feedback("§cYou don't have permission to do this");
    this.TOO_MANY_ARGUMENTS = new Feedback("§cToo many arguments");
    this.TOO_FEW_ARGUMENTS = new Feedback("§cToo few arguments");
    this.COMMAND_SUCCESS = new Feedback("");
    this.COMMAND_FAILURE = new Feedback("");
  }

  @SafeVarargs
  public CommandFeedback(Supplier<String>... suppliers) {
    this();
    Field[] fields = getClass().getDeclaredFields();
    for (int i = 0; i < suppliers.length; i++) {
      try {
        ((Feedback) fields[i].get(this)).setFeedback(suppliers[i]);
      } catch (IllegalAccessException ignore) {
      }
    }
//        switch (suppliers.length) {
//            case 0: throw new NullPointerException();
//            default:
//            case 7: COMMAND_FAILED.setFeedback(suppliers[6]);
//            case 6: COMMAND_SUCCESS.setFeedback(suppliers[5]);
//            case 5: TOO_FEW_ARGUMENTS.setFeedback(suppliers[4]);
//            case 4: TOO_MANY_ARGUMENTS.setFeedback(suppliers[3]);
//            case 3: NO_PERMISSION.setFeedback(suppliers[2]);
//            case 2: ONLY_PLAYER.setFeedback(suppliers[1]);
//            case 1: INVALID_COMMAND.setFeedback(suppliers[0]);
//        }
  }
}
