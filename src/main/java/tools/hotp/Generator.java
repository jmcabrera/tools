/**
 * 
 */
package tools.hotp;

import java.util.Random;

public interface Generator {

  static final Generator empty = new Generator() {
    @Override
    public char next() {
      return '\0';
    }

    @Override
    public int weight() {
      return 0;
    }

    @Override
    public Generator compose(Generator g2, int seed) {
      return g2;
    }
  };

  char next();

  int weight();

  static Generator of(CharSpec cs, int weight, int seed) {
    return new SimpleGenerator(cs, weight, seed);
  }

  static Generator empty() {
    return empty;
  }

  default Generator compose(Generator g2, int seed) {
    return new CompositeGenerator(this, g2, seed);
  }

  static final class SimpleGenerator implements Generator {

    public final int       weight;
    private final CharSpec cs;
    private final Random   rand;

    SimpleGenerator(CharSpec cs, int weight, int seed) {
      this.cs = cs;
      this.weight = weight;
      this.rand = new Random(seed);
    }

    @Override
    public char next() {
      return cs.charAt(rand.nextInt(cs.length()));
    }

    @Override
    public int weight() {
      return weight;
    }

  }

  static final class CompositeGenerator implements Generator {

    private final int       weight;
    private final Random    rand;
    private final Generator left, right;

    CompositeGenerator(Generator g1, Generator g2, int seed) {
      this.left = g1;
      this.right = g2;
      this.weight = g1.weight() + g2.weight();
      this.rand = new Random(seed);
    }

    @Override
    public char next() {
      return rand.nextInt(weight) < left.weight() ? left.next() : right.next();
    }

    @Override
    public int weight() {
      return weight;
    }

  }
}