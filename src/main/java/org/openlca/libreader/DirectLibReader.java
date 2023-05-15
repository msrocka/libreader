package org.openlca.libreader;

import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public class DirectLibReader implements LibReader {

	@Override
	public Library library() {
		return null;
	}

	@Override
	public TechIndex techIndex() {
		return null;
	}

	@Override
	public EnviIndex enviIndex() {
		return null;
	}

	@Override
	public ImpactIndex impactIndex() {
		return null;
	}

	@Override
	public MatrixReader matrixOf(LibMatrix matrix) {
		return null;
	}

	@Override
	public double[] costs() {
		return new double[0];
	}

	@Override
	public double[] diagonalOf(LibMatrix matrix) {
		return new double[0];
	}

	@Override
	public double[] columnOf(LibMatrix matrix, int col) {
		return new double[0];
	}
}
