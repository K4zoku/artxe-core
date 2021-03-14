package me.kazoku.artxe.utils.function;

public interface Catcher {
  default <T extends Throwable> void catcher(T throwable) {

  }
}
