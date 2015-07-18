package org.geogebra.common.kernel.prover;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Path;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.algos.SymbolicParameters;
import org.geogebra.common.kernel.algos.SymbolicParametersAlgo;
import org.geogebra.common.kernel.algos.SymbolicParametersBotanaAlgoAre;
import org.geogebra.common.kernel.arithmetic.ExpressionNodeEvaluator;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoConic;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoSegment;
import org.geogebra.common.kernel.geos.GeoVector;
import org.geogebra.common.kernel.prover.polynomial.Polynomial;
import org.geogebra.common.kernel.prover.polynomial.Variable;
import org.geogebra.common.util.debug.Log;

/**
 * Decides if two objects are congruent. Currently only segments are
 * implemented.
 *
 * @author Zoltan Kovacs <zoltan@geogebra.org>
 */
public class AlgoAreCongruent extends AlgoElement implements
		SymbolicParametersBotanaAlgoAre, SymbolicParametersAlgo {

	private GeoElement inputElement1; // input
	private GeoElement inputElement2; // input

	private GeoBoolean outputBoolean; // output
	private Polynomial[] polynomials;
	private Polynomial[][] botanaPolynomials;

	/**
	 * Creates a new AlgoAreCongruent function
	 * 
	 * @param cons
	 *            the Construction
	 * @param label
	 *            the name of the boolean
	 * @param a
	 *            the first segment
	 * @param b
	 *            the second segment
	 */
	public AlgoAreCongruent(final Construction cons, final String label,
			final GeoElement a, final GeoElement b) {
		super(cons);
		this.inputElement1 = a;
		this.inputElement2 = b;

		outputBoolean = new GeoBoolean(cons);

		setInputOutput();
		compute();
	}

	@Override
	public Commands getClassName() {
			return Commands.AreCongruent;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[2];
		input[0] = inputElement1;
		input[1] = inputElement2;

		super.setOutputLength(1);
		super.setOutput(0, outputBoolean);
		setDependencies(); // done by AlgoElement
	}

	/**
	 * Returns the result of the test
	 * 
	 * @return true if the three points lie on one line, false otherwise
	 */
	public GeoBoolean getResult() {
		return outputBoolean;
	}

	@Override
	public final void compute() {
		// Segments are congruent if they are of equal length:
		if (inputElement1 instanceof GeoSegment && inputElement2 instanceof GeoSegment) {
			outputBoolean.setValue(ExpressionNodeEvaluator.evalEquals(kernel,
				inputElement1, inputElement2).getBoolean());
			return;
		}
		// Lines/points are always congruent:
		if ((inputElement1 instanceof GeoLine && inputElement2 instanceof GeoLine) ||
				(inputElement1 instanceof GeoPoint && inputElement2 instanceof GeoPoint)) {
			outputBoolean.setValue(true);
			return;
		}
		// Conics: 
		if (inputElement1 instanceof GeoConic && inputElement2 instanceof GeoConic) {
			// Circles are congruent if their radius are of equal length:
			if (((GeoConic)inputElement1).isCircle() && ((GeoConic)inputElement2).isCircle()) {
				if (((GeoConic)inputElement1).getCircleRadius() == ((GeoConic)inputElement2).getCircleRadius()) {
					outputBoolean.setValue(true);
					return;
				}
				outputBoolean.setValue(false);
				return;
			}
			// Two parabolas are congruent if they have the same distance between the focus and directrix:
			if (((GeoConic)inputElement1).isParabola() && ((GeoConic)inputElement2).isParabola()) {
				GeoElement[] ge = (inputElement1.getParentAlgorithm().input);
				if (ge.length == 2) {
					// Easy case: definition by focus and directrix:
					GeoPoint F = (GeoPoint) ge[0];
					GeoLine d = (GeoLine) ge[1];
					double d1 = getKernel().getAlgoDispatcher()
							.getNewAlgoClosestPoint(cons, (Path) d, F).getP()
							.distance(F);

					ge = (inputElement2.getParentAlgorithm().input);
					F = (GeoPoint) ge[0];
					d = (GeoLine) ge[1];
					double d2 = getKernel().getAlgoDispatcher()
							.getNewAlgoClosestPoint(cons, (Path) d, F).getP()
							.distance(F);
					outputBoolean.setValue(d1 == d2);
					return;
					}
				// TODO: Handle the other case(s).
				}
			}
		if (inputElement1.isEqual(inputElement2)) {
			outputBoolean.setValue(true);
			return;
		}
		outputBoolean.setUndefinedProverOnly(); // Don't use this.
		// FIXME: Implement all missing cases.
	}

	public SymbolicParameters getSymbolicParameters() {
		return new SymbolicParameters(this);
	}

	public void getFreeVariables(HashSet<Variable> variables)
			throws NoSymbolicParametersException {
		if ((inputElement1 instanceof GeoSegment)
				|| (inputElement2 instanceof GeoSegment)) {
			throw new NoSymbolicParametersException();
		}
		if (inputElement1 != null && inputElement2 != null) {
			if (((inputElement1 instanceof GeoPoint) && (inputElement2 instanceof GeoPoint))
					|| ((inputElement1 instanceof GeoLine) && (inputElement2 instanceof GeoLine))
					|| ((inputElement1 instanceof GeoVector) && (inputElement2 instanceof GeoVector))) {
				((SymbolicParametersAlgo) inputElement1)
						.getFreeVariables(variables);
				((SymbolicParametersAlgo) inputElement2)
						.getFreeVariables(variables);
				return;
			}
		}
		throw new NoSymbolicParametersException();
	}

	public int[] getDegrees() throws NoSymbolicParametersException {
		if ((inputElement1 instanceof GeoSegment)
				|| (inputElement2 instanceof GeoSegment)) {
			throw new NoSymbolicParametersException();
		}
		if (inputElement1 != null && inputElement2 != null) {
			if (((inputElement1 instanceof GeoPoint) && (inputElement2 instanceof GeoPoint))
					|| ((inputElement1 instanceof GeoLine) && (inputElement2 instanceof GeoLine))
					|| ((inputElement1 instanceof GeoVector) && (inputElement2 instanceof GeoVector))) {
				int[] degrees1 = ((SymbolicParametersAlgo) inputElement1)
						.getDegrees();
				int[] degrees2 = ((SymbolicParametersAlgo) inputElement2)
						.getDegrees();
				int[] degrees = new int[1];
				degrees[0] = Math.max(
						Math.max(degrees1[0] + degrees2[2], degrees2[0]
								+ degrees1[2]),
						Math.max(degrees1[1] + degrees2[2], degrees2[1]
								+ degrees1[2]));
				return degrees;
			}
		}
		throw new NoSymbolicParametersException();
	}

	public BigInteger[] getExactCoordinates(HashMap<Variable, BigInteger> values)
			throws NoSymbolicParametersException {
		if ((inputElement1 instanceof GeoSegment)
				|| (inputElement2 instanceof GeoSegment)) {
			throw new NoSymbolicParametersException();
		}
		if (inputElement1 != null && inputElement2 != null) {
			if (((inputElement1 instanceof GeoPoint) && (inputElement2 instanceof GeoPoint))
					|| ((inputElement1 instanceof GeoLine) && (inputElement2 instanceof GeoLine))
					|| ((inputElement1 instanceof GeoVector) && (inputElement2 instanceof GeoVector))) {
				BigInteger[] coords1 = ((SymbolicParametersAlgo) inputElement1)
						.getExactCoordinates(values);
				BigInteger[] coords2 = ((SymbolicParametersAlgo) inputElement2)
						.getExactCoordinates(values);
				BigInteger[] coords = new BigInteger[1];
				coords[0] = coords1[0]
						.multiply(coords2[2])
						.subtract(coords2[0].multiply(coords1[2]))
						.abs()
						.add(coords1[1].multiply(coords2[2])
								.subtract(coords2[1].multiply(coords1[2]))
								.abs());
				return coords;
			}
		}
		throw new NoSymbolicParametersException();
	}

	public Polynomial[] getPolynomials() throws NoSymbolicParametersException {
		Log.debug(polynomials);
		if (polynomials != null) {
			return polynomials;
		}
		if ((inputElement1 instanceof GeoSegment)
				|| (inputElement2 instanceof GeoSegment)) {
			throw new NoSymbolicParametersException();
		}
		if (inputElement1 != null && inputElement2 != null) {
			if (((inputElement1 instanceof GeoPoint) && (inputElement2 instanceof GeoPoint))
					|| ((inputElement1 instanceof GeoLine) && (inputElement2 instanceof GeoLine))
					|| ((inputElement1 instanceof GeoVector) && (inputElement2 instanceof GeoVector))) {
				Polynomial[] coords1 = ((SymbolicParametersAlgo) inputElement1)
						.getPolynomials();
				Polynomial[] coords2 = ((SymbolicParametersAlgo) inputElement2)
						.getPolynomials();
				polynomials = new Polynomial[2];
				polynomials[0] = coords1[0].multiply(coords2[2]).subtract(
						coords2[0].multiply(coords1[2]));
				polynomials[1] = coords1[1].multiply(coords2[2]).subtract(
						coords2[1].multiply(coords1[2]));
				return polynomials;
			}
		}
		throw new NoSymbolicParametersException();
	}

	public Polynomial[][] getBotanaPolynomials()
			throws NoSymbolicParametersException {
		if (botanaPolynomials != null) {
			return botanaPolynomials;
		}

		if (inputElement1 instanceof GeoPoint
				&& inputElement2 instanceof GeoPoint) {
			// Same as in AreEqual.
			botanaPolynomials = new Polynomial[2][1];

			Variable[] v1 = new Variable[2];
			Variable[] v2 = new Variable[2];
			v1 = ((GeoPoint) inputElement1).getBotanaVars(inputElement1); // A=(x1,y1)
			v2 = ((GeoPoint) inputElement2).getBotanaVars(inputElement2); // B=(x2,y2)

			// We want to prove: 1) x1-x2==0, 2) y1-y2==0
			botanaPolynomials[0][0] = new Polynomial(v1[0])
					.subtract(new Polynomial(v2[0]));
			botanaPolynomials[1][0] = new Polynomial(v1[1])
					.subtract(new Polynomial(v2[1]));
			return botanaPolynomials;
		}

		// Order is important here: a GeoSegment is also a GeoLine!
		if (inputElement1 instanceof GeoSegment
				&& inputElement2 instanceof GeoSegment) {
			// We check whether their length are equal.
			botanaPolynomials = new Polynomial[1][1];

			Variable[] v1 = new Variable[4];
			Variable[] v2 = new Variable[4];
			v1 = ((GeoSegment) inputElement1).getBotanaVars(inputElement1); // AB
			v2 = ((GeoSegment) inputElement2).getBotanaVars(inputElement2); // CD

			// We want to prove: d(AB)=d(CD) =>
			// (a1-b1)^2+(a2-b2)^2=(c1-d1)^2+(c2-d2)^2
			// => (a1-b1)^2+(a2-b2)^2-(c1-d1)^2-(c2-d2)^2
			Polynomial a1 = new Polynomial(v1[0]);
			Polynomial a2 = new Polynomial(v1[1]);
			Polynomial b1 = new Polynomial(v1[2]);
			Polynomial b2 = new Polynomial(v1[3]);
			Polynomial c1 = new Polynomial(v2[0]);
			Polynomial c2 = new Polynomial(v2[1]);
			Polynomial d1 = new Polynomial(v2[2]);
			Polynomial d2 = new Polynomial(v2[3]);
			botanaPolynomials[0][0] = ((Polynomial.sqr(a1.subtract(b1))
					.add(Polynomial.sqr(a2.subtract(b2)))).subtract(Polynomial
					.sqr(c1.subtract(d1)))).subtract(Polynomial.sqr(c2
					.subtract(d2)));

			return botanaPolynomials;
		}

		if (inputElement1 instanceof GeoLine
				&& inputElement2 instanceof GeoLine) {
			// Same as in AreEqual.
			botanaPolynomials = new Polynomial[2][1];

			Variable[] v1 = new Variable[4];
			Variable[] v2 = new Variable[4];
			v1 = ((GeoLine) inputElement1).getBotanaVars(inputElement1); // AB
			v2 = ((GeoLine) inputElement2).getBotanaVars(inputElement2); // CD

			// We want to prove: 1) ABC collinear, 2) ABD collinear
			botanaPolynomials[0][0] = Polynomial.collinear(v1[0], v1[1], v1[2],
					v1[3], v2[0], v2[1]);
			botanaPolynomials[1][0] = Polynomial.collinear(v1[0], v1[1], v1[2],
					v1[3], v2[2], v2[3]);
			return botanaPolynomials;
		}

		throw new NoSymbolicParametersException();
	}

	// TODO Consider locusequability

}
