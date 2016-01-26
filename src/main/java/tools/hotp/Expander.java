/**
 * 
 */
package tools.hotp;

import static tools.hotp.CharSpec.*;

import tools.hotp.CharSpec.CharSpecs;
import tools.hotp.OTPSpec.OTPSpecFluent;

/**
 * @author a208220 - Juan Manuel CABRERA
 *
 */
public class Expander {

  public static void main(String[] args) {

    int otp = 1523644182;

    OTPSpecFluent specbuilder = OTPSpec//
        .with(CharSpecs.ALPHA_MAJ, 4) //
        .and(CharSpecs.ALPHA_MIN, 3) //
        .and(CharSpecs.DIGIT, 2) //
        .and(of(".?!§").and(of("'()@")), 1) //
        .matching("^[A-Z].*");

    OTPSpec spec = specbuilder.get(otp);

    System.out.println(spec.expand(100, 10));

  }

}
