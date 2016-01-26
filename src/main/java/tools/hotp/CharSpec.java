/**
 * 
 */
package tools.hotp;

import java.util.function.IntFunction;

public interface CharSpec {
  int length();

  char charAt(int i);

  public static enum CharSpecs implements CharSpec {
    EMPTY(i -> (char) ('0' + i), 0) {
      @Override
      public CharSpec and(CharSpec other) {
        return other;
      }
    }, //
    ALPHA_MIN(i -> (char) ('a' + i), 26), //
    ALPHA_MAJ(i -> (char) ('A' + i), 26), //
    DIGIT(i -> (char) ('0' + i), 9);

    private final char[] chars;

    private CharSpecs(IntFunction<Character> f, int size) {
      this.chars = new char[size];
      for (int i = 0; i < chars.length; i++) {
        chars[i] = f.apply(i);
      }
    }

    @Override
    public char charAt(int i) {
      return chars[i];
    }

    @Override
    public int length() {
      return chars.length;
    }
  }

  static CharSpec empty() {
    return CharSpecs.EMPTY;
  }

  static CharSpec of(char... chars) {
    return new SimpleCharSpec(chars);
  }

  static CharSpec of(String chars) {
    return new SimpleCharSpec(chars.toCharArray());
  }

  default CharSpec and(CharSpec other) {
    return new CompositeCharSpec(this, other);
  }

  static final class SimpleCharSpec implements CharSpec {

    private char[] chars;

    SimpleCharSpec(char[] chars) {
      this.chars = chars;
    }

    @Override
    public int length() {
      return chars.length;
    }

    @Override
    public char charAt(int i) {
      return chars[i];
    }

  }

  static final class CompositeCharSpec implements CharSpec {

    private final CharSpec left, right;
    private final int      length, offset;

    CompositeCharSpec(CharSpec left, CharSpec right) {
      this.left = left;
      this.right = right;
      this.length = left.length() + right.length();
      this.offset = left.length();
    }

    @Override
    public int length() {
      return length;
    }

    @Override
    public char charAt(int i) {
      return i < offset ? left.charAt(i) : right.charAt(i - offset);
    }

  }
}