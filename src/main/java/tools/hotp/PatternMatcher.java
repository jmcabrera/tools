/**
 * 
 */
package tools.hotp;

import java.util.regex.Pattern;

public interface PatternMatcher {

  public static PatternMatcher empty = new PatternMatcher() {

    @Override
    public boolean matches(String candidate) {
      return true;
    }

    public PatternMatcher compose(PatternMatcher other) {
      return other;
    };
  };

  static PatternMatcher empty() {
    return empty;
  }

  static PatternMatcher of(String regexp) {
    return new SimplePatternMatcher(regexp);
  }

  default PatternMatcher compose(PatternMatcher other) {
    return new CompositePatternMatcher(this, other);
  }

  public boolean matches(String candidate);

  static class CompositePatternMatcher implements PatternMatcher {

    private final PatternMatcher left, right;

    public CompositePatternMatcher(PatternMatcher left, PatternMatcher right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public boolean matches(String candidate) {
      return left.matches(candidate) && right.matches(candidate);
    }

  }

  static class SimplePatternMatcher implements PatternMatcher {

    private final Pattern pattern;

    public SimplePatternMatcher(String regexp) {
      this.pattern = Pattern.compile(regexp);
    }

    @Override
    public boolean matches(String candidate) {
      return pattern.matcher(candidate).matches();
    }
  }
}