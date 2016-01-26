/**
 * 
 */
package tools.hotp;

public class OTPSpec {

  public static final class OTPSpecFluent {

    private final GeneratorSpec  gen;
    private final PatternMatcher pm;

    public OTPSpecFluent(GeneratorSpec gen, PatternMatcher pm) {
      this.gen = gen;
      this.pm = pm;
    }

    public OTPSpecFluent() {
      gen = GeneratorSpec.empty();
      pm = PatternMatcher.empty();
    }

    public OTPSpecFluent and(CharSpec cs, int weight) {
      return new OTPSpecFluent(gen.compose(GeneratorSpec.of(cs, weight)), pm);
    }

    public OTPSpecFluent matching(String regexp) {
      return new OTPSpecFluent(gen, pm.compose(PatternMatcher.of(regexp)));
    }

    public OTPSpec get(int seed) {
      return new OTPSpec(gen.get(seed), pm);
    }
  }

  private final Generator gen;
  private PatternMatcher  pm;

  private OTPSpec(Generator gen, PatternMatcher pm) {
    this.gen = gen;
    this.pm = pm;
  }

  String expand(int length, int attempts) {
    String candidate;
    char[] c = new char[length];
    for (int a = 0; a < attempts; a++) {
      for (int i = 0; i < c.length; i++) {
        c[i] = gen.next();
      }
      candidate = new String(c);
      if (pm.matches(candidate)) return candidate;
    }
    throw new RuntimeException("Could not expand to the give string");
  }

  public static OTPSpecFluent with(CharSpec cs, int weight) {
    return new OTPSpecFluent().and(cs, weight);
  }

}