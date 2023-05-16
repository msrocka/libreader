package org.openlca.libreader;

import org.openlca.core.database.IDatabase;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public interface LibReader {

	static LibReader defaultOf(Library lib, IDatabase db) {
		var direct = DirectLibReader.of(lib, db);
		return CachingLibReader.of(direct);
	}

	Library library();

	TechIndex techIndex();

	EnviIndex enviIndex();

	ImpactIndex impactIndex();

	MatrixReader matrixOf(LibMatrix matrix);

	double[] costs();

	double[] diagonalOf(LibMatrix matrix);

	double[] columnOf(LibMatrix matrix, int col);

	default void dispose(){
	}
}
