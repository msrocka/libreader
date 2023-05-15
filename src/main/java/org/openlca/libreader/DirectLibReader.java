package org.openlca.libreader;

import org.openlca.core.database.IDatabase;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.npy.Array2d;

/**
 * A library reader that directly reads the data elements
 * from files on each method call without caching anything.
 */
public class DirectLibReader implements LibReader {

	private final IDatabase db;
	private final Library lib;

	private DirectLibReader(Library lib, IDatabase db) {
		this.lib = lib;
		this.db = db;
	}

	public static LibReader of(Library lib, IDatabase db) {
		return new DirectLibReader(lib, db);
	}

	@Override
	public Library library() {
		return lib;
	}

	@Override
	public TechIndex techIndex() {
		return lib.syncTechIndex(db).orElse(null);
	}

	@Override
	public EnviIndex enviIndex() {
		return lib.syncEnviIndex(db).orElse(null);
	}

	@Override
	public ImpactIndex impactIndex() {
		return lib.syncImpactIndex(db).orElse(null);
	}

	@Override
	public MatrixReader matrixOf(LibMatrix m) {
		return lib.getMatrix(m).orElse(null);
	}

	@Override
	public double[] costs() {
		return lib.getCosts().orElse(null);
	}

	@Override
	public double[] diagonalOf(LibMatrix m) {
		var f = MatrixFile.of(lib, m);
		if (f.isEmpty())
			return null;
		if (f.hasMatrix())
			return f.matrix().diag();
		return Array2d.readDiag(f.file())
			.asDoubleArray()
			.data();
	}

	@Override
	public double[] columnOf(LibMatrix m, int j) {
		var f = MatrixFile.of(lib, m);
		if (f.isEmpty())
			return null;
		if (f.hasMatrix())
			return f.matrix().getColumn(j);
		return Array2d.readColumn(f.file(), j)
			.asDoubleArray()
			.data();
	}
}
