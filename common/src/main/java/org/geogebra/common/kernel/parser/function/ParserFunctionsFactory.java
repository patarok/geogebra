package org.geogebra.common.kernel.parser.function;

import org.geogebra.common.plugin.Operation;

import com.himamis.retex.editor.share.util.Unicode;

/**
 * Creates ParserFunctions.
 */
public class ParserFunctionsFactory {

	private static final String SINGLE_ARG = "( <x> )";

	private static final String[] TRANSLATABLE_1_VAR = new String[] { "sin", "cos",
			"tan", "cot", "csc", "sec", "sinh", "cosh", "tanh", "coth", "csch",
			"sech", "asin", "acos", "atan", "asind", "acosd", "atand", "asinh",
			"acosh", "atanh", "real", "imaginary", "conjugate",
			"fractionalPart" };

	private final boolean addExtra;

	private ParserFunctionsFactory(boolean addExtra) {
		this.addExtra = addExtra;
	}

	/**
	 * Creates a ParserFunctions with the default functions.
	 *
	 * @return parser functions
	 */
	public static ParserFunctionsFactory createParserFunctionsFactory() {
		return new ParserFunctionsFactory(true);
	}

	/**
	 * Creates a ParserFunctions for the Graphing app.
	 *
	 * @return parser functions
	 */
	public static ParserFunctionsFactory createGraphingParserFunctionsFactory() {
		return new ParserFunctionsFactory(false);
	}

	public ParserFunctions createParserFunctions() {
		return createParserFunctions(false);
	}

	public ParserFunctions createInputBoxParserFunctions() {
		return createParserFunctions(true);
	}

	private ParserFunctions createParserFunctions(boolean inputBox) {
		ParserFunctionsImpl parserFunctions = new ParserFunctionsImpl();
		addFunctions(parserFunctions);
		if (addExtra) {
			addExtraFunctions(parserFunctions);
		}
		if (!inputBox) {
			addInputBoxExcludedFunctions(parserFunctions);
		}

		addReservedFunctions(parserFunctions);
		addTranslatable(parserFunctions);

		return parserFunctions;
	}

	private static void addFunctions(ParserFunctionsImpl pf) {
		put(pf, 1, "sin", Operation.SIN, null);
		put(pf, 1, "cos", Operation.COS, null);
		put(pf, 1, "tan", Operation.TAN, null);
		put(pf, 1, "csc", Operation.CSC, null);
		put(pf, 1, "sec", Operation.SEC, null);
		put(pf, 1, "cot", Operation.COT, null);

		put(pf, 1, "sinh", Operation.SINH, null);
		put(pf, 1, "cosh", Operation.COSH, null);
		put(pf, 1, "tanh", Operation.TANH, null);
		put(pf, 1, "csch", Operation.CSCH, null);
		put(pf, 1, "sech", Operation.SECH, null);
		put(pf, 1, "coth", Operation.COTH, null);

		put(pf, 1, "asind", Operation.ARCSIND, null);
		put(pf, 1, "arcsind", Operation.ARCSIND);

		put(pf, 1, "acosd", Operation.ARCCOSD, null);
		put(pf, 1, "arccosd", Operation.ARCCOSD);

		put(pf, 1, "atand", Operation.ARCTAND, null);
		put(pf, 1, "arctand", Operation.ARCTAND);

		put(pf, 2, "atan2d", Operation.ARCTAN2D, "( <y>, <x> )");
		put(pf, 2, "arctan2d", Operation.ARCTAN2D, "( <y>, <x> )");

		put(pf, 1, "arcsin", Operation.ARCSIN);
		put(pf, 1, "arccos", Operation.ARCCOS);
		put(pf, 1, "arctan", Operation.ARCTAN);

		put(pf, 1, "arsinh", Operation.ASINH);
		put(pf, 1, "arcsinh", Operation.ASINH);

		put(pf, 1, "arcosh", Operation.ACOSH);
		put(pf, 1, "arccosh", Operation.ACOSH);

		put(pf, 1, "artanh", Operation.ATANH);
		put(pf, 1, "arctanh", Operation.ATANH);

		put(pf, 2, "atan2", Operation.ARCTAN2, "( <y>, <x> )");
		put(pf, 2, "arctan2", Operation.ARCTAN2, "( <y>, <x> )");

		put(pf, 1, "erf", Operation.ERF);

		put(pf, 1, "psi", Operation.PSI);

		put(pf, 2, "polygamma", Operation.POLYGAMMA, "( <m>, <x> )");

		put(pf, 1, "exp", Operation.EXP);

		put(pf, 1, "LambertW", Operation.LAMBERTW);
		put(pf, 2, "LambertW", Operation.LAMBERTW);

		put(pf, 1, "ln", Operation.LOG);

		put(pf, 2, "log", Operation.LOGB, "( <b> , <x> )");
		put(pf, 2, "ln", Operation.LOGB, null);

		put(pf, 1, "log", Operation.LOG10);

		put(pf, 1, "zeta", Operation.ZETA);

		put(pf, 2, "beta", Operation.BETA, "( <a>, <b> )");

		put(pf, 3, "beta", Operation.BETA_INCOMPLETE, "( <a>, <b>, <x> )");

		put(pf, 3, "betaRegularized", Operation.BETA_INCOMPLETE_REGULARIZED,
				"( <a>, <b>, <x> )");
		put(pf, 3, "ibeta", Operation.BETA_INCOMPLETE_REGULARIZED, null);

		put(pf, 1, "gamma", Operation.GAMMA);

		put(pf, 2, "gamma", Operation.GAMMA_INCOMPLETE, "( <x>, <y> )");

		put(pf, 2, "gammaRegularized", Operation.GAMMA_INCOMPLETE_REGULARIZED);

		put(pf, 1, "cosIntegral", Operation.CI);
		put(pf, 1, "sinIntegral", Operation.SI);
		put(pf, 1, "expIntegral", Operation.EI);

		// functions that come from CAS / Giac
		put(pf, 2, "gGbInTeGrAl", Operation.INTEGRAL, null);
		put(pf, 2, "gGbSuBsTiTuTiOn", Operation.SUBSTITUTION, null);
		put(pf, 4, "gGbSuM", Operation.SUM, null);
		put(pf, 2, "gGbIfElSe", Operation.IF, null);
		put(pf, 3, "gGbIfElSe", Operation.IF_ELSE, null);

		put(pf, 1, "arbint", Operation.ARBINT);

		put(pf, 1, "arbconst", Operation.ARBCONST);

		put(pf, 1, "arbcomplex", Operation.ARBCOMPLEX);

		put(pf, 1, "sqrt", Operation.SQRT);
		put(pf, 1, "cbrt", Operation.CBRT);
		put(pf, 1, "abs", Operation.ABS);

		put(pf, 1, "sgn", Operation.SGN);
		put(pf, 1, "sign", Operation.SGN);
		put(pf, 1, "floor", Operation.FLOOR);
		put(pf, 1, "ceil", Operation.CEIL);
		put(pf, 1, "round", Operation.ROUND);
		put(pf, 2, "round", Operation.ROUND2, "( <x>, <y> )");
		put(pf, 1, "conjugate", Operation.CONJUGATE, null);

		put(pf, 0, "random", Operation.RANDOM, "()");
		put(pf, 1, "x", Operation.XCOORD, null);
		put(pf, 1, "y", Operation.YCOORD, null);
		put(pf, 1, "z", Operation.ZCOORD, null);

		put(pf, 2, "nroot", Operation.NROOT, null);
		put(pf, 2, "NRoot", Operation.NROOT, null);

		put(pf, 1, "fractionalpart", Operation.FRACTIONAL_PART, null);
		put(pf, 1, "fractionalPart", Operation.FRACTIONAL_PART, null);

		put(pf, 2, "ggbdiff", Operation.DIFF, null);
		put(pf, 3, "ggbdiff", Operation.DIFF, null);

		put(pf, 1, "vectorize", Operation.MATRIXTOVECTOR, null);

		put(pf, 2, "nPr", Operation.NPR, "( <n>, <r> )");
	}

	private static void addExtraFunctions(ParserFunctionsImpl pf) {
		put(pf, 1, "arg", Operation.ARG);
		put(pf, 1, "alt", Operation.ALT, "( (x, y, z) )");
	}

	private static void addInputBoxExcludedFunctions(ParserFunctionsImpl pf) {
		put(pf, 1, "atanh", Operation.ATANH, null);
		put(pf, 1, "acosh", Operation.ACOSH, null);
		put(pf, 1, "asinh", Operation.ASINH, null);
		put(pf, 1, "atan", Operation.ARCTAN, null);
		put(pf, 1, "acos", Operation.ARCCOS, null);
		put(pf, 1, "asin", Operation.ARCSIN, null);

		put(pf, 1, "cosech", Operation.CSCH);
		put(pf, 1, "cotanh", Operation.COTH);
		put(pf, 1, "cosec", Operation.CSC);
		put(pf, 1, "cotan", Operation.COT);

		put(pf, 1, "ld", Operation.LOG2);
		put(pf, 1, "log2", Operation.LOG2);

		put(pf, 1, "lg", Operation.LOG10);
		put(pf, 1, "log10", Operation.LOG10);

		put(pf, 1, "real", Operation.REAL, null);
		put(pf, 1, "imaginary", Operation.IMAGINARY, null);
	}

	private static void put(ParserFunctionsImpl pf, int size, String name,
							Operation op) {
		put(pf, size, name, op, SINGLE_ARG);
	}

	private static void put(ParserFunctionsImpl pf, int size, String name,
							Operation op, String arg) {
		pf.add(name, size, arg, op);
	}

	private static void addReservedFunctions(ParserFunctionsImpl pf) {
		pf.addReserved(Unicode.IMAGINARY + "");
		pf.addReserved(Unicode.EULER_STRING);
		pf.addReserved(Unicode.EULER_GAMMA_STRING);
		pf.addReserved(Unicode.pi + "");
		pf.addReserved("freehand");
		pf.addReserved("deg");
	}

	private static void addTranslatable(ParserFunctionsImpl pf) {
		for (String fn: TRANSLATABLE_1_VAR) {
			pf.addTranslatable(fn, SINGLE_ARG);
		}
		pf.addTranslatable("nroot", 2, "( <x>, <n> )", Operation.NROOT);
		pf.addTranslatable("nPr", 2, "( <n>, <r> )", Operation.NPR);
	}
}
