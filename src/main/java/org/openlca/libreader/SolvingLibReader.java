package org.openlca.libreader;

import java.util.Objects;

import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.core.matrix.solvers.Factorization;
import org.openlca.core.matrix.solvers.MatrixSolver;

public class SolvingLibReader implements LibReader {

	private final CachingLibReader reader;
	private final MatrixSolver solver;
	private Factorization factorization;

	private SolvingLibReader(CachingLibReader reader, MatrixSolver solver) {
		this.reader = Objects.requireNonNull(reader);
		this.solver = Objects.requireNonNull(solver);
	}

	@Override
	public Library library() {
		return reader.library();
	}

	@Override
	public TechIndex techIndex() {
		return reader.techIndex();
	}

	@Override
	public EnviIndex enviIndex() {
		return reader.enviIndex();
	}

	@Override
	public ImpactIndex impactIndex() {
		return reader.impactIndex();
	}

	@Override
	public MatrixReader matrixOf(LibMatrix m) {
		return switch (m) {
			case INV -> fullInverse();
			case M -> fullIntensities();
			default -> reader.matrixOf(m);
		};
	}

	@Override
	public double[] costs() {
		return reader.costs();
	}

	@Override
	public double[] diagonalOf(LibMatrix m) {
		if (m == LibMatrix.INV) {
			var matrix = fullInverse();
			return matrix != null ? matrix.diag() : null;
		}
		if (m == LibMatrix.M) {
			var matrix = fullIntensities();
			return matrix != null ? matrix.diag() : null;
		}
		return reader.diagonalOf(m);
	}

	@Override
	public double[] columnOf(LibMatrix m, int j) {
		return switch (m) {
			case INV -> solutionOf(j);
			case M -> intensitiesOf(j);
			default -> reader.columnOf(m, j);
		};
	}

	private MatrixReader fullInverse() {
		var cached = reader.cache().matrixOf(LibMatrix.INV);
		if (cached != null)
			return cached;
		var techMatrix = reader.matrixOf(LibMatrix.A);
		if (techMatrix == null)
			return null;
		var inv = solver.invert(techMatrix);
		reader.cache().put(LibMatrix.INV, inv);
		return inv;
	}

	private MatrixReader fullIntensities() {
		var cached = reader.cache().matrixOf(LibMatrix.M);
		if (cached != null)
			return cached;
		var enviMatrix = reader.matrixOf(LibMatrix.B);
		if (enviMatrix == null)
			return null;
		var inv = fullInverse();
		if (inv == null)
			return null;
		var intensities = solver.multiply(enviMatrix, inv);
		reader.cache().put(LibMatrix.M, intensities);
		return intensities;
	}

	private double[] solutionOf(int j) {

	}

	private double[] intensitiesOf(int j) {

	}

	@Override
	public void dispose() {
		if (factorization != null) {
			factorization.dispose();
			factorization = null;
		}
	}
}
