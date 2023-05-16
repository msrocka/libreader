package org.openlca.libreader;

import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public class SimulatingLibReader implements LibReader {

	private final LibReader reader;

	private SimulatingLibReader(LibReader reader) {
		this.reader = reader;
	}

	public static LibReader of(LibReader reader) {
		return new SimulatingLibReader(reader);
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
