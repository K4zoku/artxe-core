package me.kazoku.artxe.configuration.path;

import me.kazoku.artxe.configuration.general.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.comments.Commentable;

import java.util.EnumMap;
import java.util.Optional;

/**
 * A commentable config path
 *
 * @param <T> the type of the value
 */
public class CommentablePath<T> implements ConfigPath<T> {
  private final ConfigPath<T> originalPath;
  private final EnumMap<CommentType, String> defaultCommentMap = new EnumMap<>(CommentType.class);

  /**
   * Create a config path
   *
   * @param originalPath    the original config path
   * @param defaultComments the default comments
   */
  public CommentablePath(@NotNull final ConfigPath<T> originalPath, @NotNull final String... defaultComments) {
    this.originalPath = originalPath;

    if (defaultComments.length > 0)
      defaultCommentMap.put(CommentType.BLOCK, String.join("\n", defaultComments));
  }

  @Override
  public @NotNull T getValue() {
    return originalPath.getValue();
  }

  @Override
  public void setValue(@Nullable final T value) {
    originalPath.setValue(value);
  }

  @Override
  public @NotNull String getPath() {
    return originalPath.getPath();
  }

  @Override
  public @Nullable Config getConfig() {
    return originalPath.getConfig();
  }

  @Override
  public void setConfig(@NotNull final Config config) {
    originalPath.setConfig(config);
    Optional.of(config.getConfig())
        .filter(Commentable.class::isInstance)
        .map(Commentable.class::cast)
        .ifPresent(configuration ->
            defaultCommentMap.forEach((type, comment) -> {
              if (configuration.getComment(getPath(), type) == null)
                configuration.setComment(getPath(), comment, type);
            })
        );
  }

  /**
   * Get the block comment
   *
   * @return the comment
   */
  @Nullable
  public String getComment() {
    return getComment(CommentType.BLOCK);
  }

  /**
   * Set the block comment
   *
   * @param comment the comment
   */
  public void setComment(@Nullable final String comment) {
    setComment(CommentType.BLOCK, comment);
  }

  /**
   * Get the comment
   *
   * @param commentType the comment type
   * @return the comment
   */
  @Nullable
  public String getComment(@NotNull final CommentType commentType) {
    return Optional.ofNullable(getConfig())
        .map(Config::getConfig)
        .filter(Commentable.class::isInstance)
        .map(Commentable.class::cast)
        .map(config -> config.getComment(getPath(), commentType))
        .orElse(defaultCommentMap.get(commentType));
  }

  /**
   * Set the comment
   *
   * @param commentType the comment type
   * @param comment     the comment
   */
  public void setComment(@NotNull final CommentType commentType, @Nullable final String comment) {
    Optional.ofNullable(getConfig())
        .map(Config::getConfig)
        .filter(Commentable.class::isInstance)
        .map(Commentable.class::cast)
        .ifPresent(config -> config.setComment(getPath(), comment, commentType));
  }
}
