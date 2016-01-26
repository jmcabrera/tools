/**
 * 
 */
package tools.hotp;

public interface GeneratorSpec {

  static final GeneratorSpec empty = new GeneratorSpec() {
    @Override
    public int weight() {
      return 0;
    }

    @Override
    public GeneratorSpec compose(GeneratorSpec g2) {
      return g2;
    }

    @Override
    public Generator get(int seed) {
      return Generator.empty();
    }
  };

  int weight();

  static GeneratorSpec of(CharSpec cs, int weight) {
    return new SimpleGeneratorSpec(cs, weight);
  }

  static GeneratorSpec empty() {
    return empty;
  }

  default GeneratorSpec compose(GeneratorSpec g2) {
    return new CompositeGeneratorSpec(this, g2);
  }

  Generator get(int seed);

  static final class SimpleGeneratorSpec implements GeneratorSpec {

    public final int       weight;
    private final CharSpec cs;

    SimpleGeneratorSpec(CharSpec cs, int weight) {
      this.cs = cs;
      this.weight = weight;
    }

    @Override
    public int weight() {
      return weight;
    }

    @Override
    public Generator get(int seed) {
      return Generator.of(cs, weight, seed);
    }

  }

  static final class CompositeGeneratorSpec implements GeneratorSpec {

    private final int           weight;
    private final GeneratorSpec left, right;

    CompositeGeneratorSpec(GeneratorSpec g1, GeneratorSpec g2) {
      this.left = g1;
      this.right = g2;
      this.weight = g1.weight() + g2.weight();
    }

    @Override
    public int weight() {
      return weight;
    }

    @Override
    public Generator get(int seed) {
      return left.get(seed).compose(right.get(seed), seed);
    }

  }

}