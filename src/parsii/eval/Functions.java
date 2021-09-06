/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import java.util.List;

/**
 * Contains a set of predefined standard functions.
 * <p>
 * Provides mostly functions defined by {@link Math}
 */
public class Functions {

    /**
     * Provides access to {@link Math#sin(Complex)}
     */
    public static final Function SIN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.sin();
        }
    };

    /**
     * Provides access to {@link Math#sinh(Complex)}
     */
    public static final Function SINH = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.sinh();
        }
    };

    /**
     * Provides access to {@link Math#cos(Complex)}
     */
    public static final Function COS = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.cos();
        }
    };

    /**
     * Provides access to {@link Math#cosh(Complex)}
     */
    public static final Function COSH = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.cosh();
        }
    };

    /**
     * Provides access to {@link Math#tan(Complex)}
     */
    public static final Function TAN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.tan();
        }
    };

    /**
     * Provides access to {@link Math#tanh(Complex)}
     */
    public static final Function TANH = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.tanh();
        }
    };

    /**
     * Provides access to {@link Math#abs(Complex)}
     */
    public static final Function ABS = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return new Complex(a.abs(), 0.0);
        }
    };

    /**
     * Provides access to {@link Math#asin(Complex)}
     */
    public static final Function ASIN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.asin();
        }
    };

    /**
     * Provides access to {@link Math#acos(Complex)}
     */
    public static final Function ACOS = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.acos();
        }
    };

    /**
     * Provides access to {@link Math#atan(Complex)}
     */
    public static final Function ATAN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.atan();
        }
    };

    /**
     * Provides access to {@link Math#atan2(Complex, Complex)}
     */
    public static final Function ATAN2 = new BinaryFunction() {
        @Override
        protected Complex eval(Complex a, Complex b) {
            return Complex.atan2(a, b);
        }
    };

    /**
     * Provides access to {@link Math#round(Complex)}
     */
    public static final Function ROUND = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.round();
        }
    };

    /**
     * Provides access to {@link Math#floor(Complex)}
     */
    public static final Function FLOOR = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.floor();
        }
    };

    /**
     * Provides access to {@link Math#ceil(Complex)}
     */
    public static final Function CEIL = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.ceil();
        }
    };

    /**
     * Provides access to {@link Math#pow(Complex, Complex)}
     */
    public static final Function POW = new BinaryFunction() {
        @Override
        protected Complex eval(Complex a, Complex b) {
            return a.pow(b);
        }
    };

    /**
     * Provides access to {@link Math#sqrt(Complex)}
     */
    public static final Function SQRT = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.sqrt();
        }
    };

    /**
     * Provides access to {@link Math#exp(Complex)}
     */
    public static final Function EXP = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.exp();
        }
    };

    /**
     * Provides access to {@link Math#log(Complex)}
     */
    public static final Function LN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.log();
        }
    };

    /**
     * Provides access to {@link Math#log10(Complex)}
     */
    public static final Function LOG = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.log10();
        }
    };

    /**
     * Provides access to {@link Math#min(Complex, Complex)}
     */
    public static final Function MIN = new BinaryFunction() {
        @Override
        protected Complex eval(Complex a, Complex b) {
            return Complex.min(a, b);
        }
    };

    /**
     * Provides access to {@link Math#max(Complex, Complex)}
     */
    public static final Function MAX = new BinaryFunction() {
        @Override
        protected Complex eval(Complex a, Complex b) {
            return Complex.max(a, b);
        }
    };

    /**
     * Provides access to {@link Math#random()} which will be multiplied by the given argument.
     */
    public static final Function RND = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return new Complex(Math.random() * a.getReal(), Math.random() * a.getImage());
        }
    };

    /**
     * Provides access to {@link Math#signum(Complex)}
     */
    public static final Function SIGN = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.signum();
        }
    };

    /**
     * Provides access to {@link Math#toDegrees(Complex)}
     */
    public static final Function DEG = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.toDegrees();
        }
    };

    /**
     * Provides access to {@link Math#toRadians(Complex)}
     */
    public static final Function RAD = new UnaryFunction() {
        @Override
        protected Complex eval(Complex a) {
            return a.toRadians();
        }
    };

    /**
     * Provides an if-like function
     * <p>
     * It expects three arguments: A condition, an expression being evaluated if the condition is non zero and an
     * expression which is being evaluated if the condition is zero.
     */
    public static final Function IF = new IfFunction();

    private Functions() {
    }

    private static class IfFunction implements Function {
        @Override
        public int getNumberOfArguments() {
            return 3;
        }

        @Override
        public Complex eval(List<Expression> args) {
            Complex check = args.get(0).evaluate();
            if (check.isNaN()) {
                return check;
            }
            if (check.abs() > 0) {
                return args.get(1).evaluate();
            } else {
                return args.get(2).evaluate();
            }
        }

        @Override
        public boolean isNaturalFunction() {
            return false;
        }
    }
}
