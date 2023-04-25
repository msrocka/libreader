package org.openlca.libreader;

import java.util.Optional;

import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public interface LibReader {

	TechIndex techIndex();

	EnviIndex enviIndex();

	ImpactIndex impactIndex();

	MatrixReader matrixOf(LibMatrix matrix);

	double[] costs();

	double[] diagonalOf(LibMatrix matrix);

	double[] columnOf(LibMatrix matrix, int col);
}
