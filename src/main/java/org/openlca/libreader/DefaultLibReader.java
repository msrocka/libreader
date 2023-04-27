package org.openlca.libreader;

import java.util.EnumMap;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.openlca.core.database.IDatabase;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.npy.Array2d;

/**
 * The default implementation of the {@link LibReader} interface that caches
 * data that were already read from files.
 */
class DefaultLibReader implements LibReader {

	private final IDatabase db;
	private final Library lib;

	// cached values
	private TechIndex _techIndex;
	private EnviIndex _enviIndex;
	private ImpactIndex _impactIndex;
	private double[] _costs;

	private final EnumMap<LibMatrix, MatrixReader> _matrices;
	private final EnumMap<LibMatrix, double[]> _diagonals;
	private final EnumMap<LibMatrix, TIntObjectHashMap<double[]>> _columns;

	private DefaultLibReader(Library lib, IDatabase db) {
		this.db = db;
		this.lib = lib;
		_matrices = new EnumMap<>(LibMatrix.class);
		_diagonals = new EnumMap<>(LibMatrix.class);
		_columns = new EnumMap<>(LibMatrix.class);
	}

	static LibReader of(Library lib, IDatabase db) {
		return new DefaultLibReader(lib, db);
	}

	@Override
	public Library library() {
		return lib;
	}

	@Override
	public TechIndex techIndex() {
		if (_techIndex == null) {
			_techIndex = lib.syncTechIndex(db).orElse(null);
		}
		return _techIndex;
	}

	@Override
	public EnviIndex enviIndex() {
		if (_enviIndex == null) {
			_enviIndex = lib.syncEnviIndex(db).orElse(null);
		}
		return _enviIndex;
	}

	@Override
	public ImpactIndex impactIndex() {
		if (_impactIndex == null) {
			_impactIndex = lib.syncImpactIndex(db).orElse(null);
		}
		return _impactIndex;
	}

	@Override
	public MatrixReader matrixOf(LibMatrix matrix) {
		return _matrices.computeIfAbsent(
			matrix, m -> lib.getMatrix(m).orElse(null)
		);
	}

	@Override
	public double[] costs() {
		if (_costs == null) {
			_costs = lib.getCosts().orElse(null);
		}
		return _costs;
	}

	@Override
	public double[] diagonalOf(LibMatrix matrix) {
		return _diagonals.computeIfAbsent(matrix, m -> {
			var f = matrixFileOf(matrix);
			if (f.isEmpty())
				return null;
			return f.hasMatrix()
				? f.matrix().diag()
				: Array2d.readDiag(f.file())
				.asDoubleArray()
				.data();
		});
	}

	@Override
	public double[] columnOf(LibMatrix matrix, int j) {
		var cache = _columns.computeIfAbsent(
			matrix, m -> new TIntObjectHashMap<>());
		var column = cache.get(j);
		if (column != null)
			return column;

		var f = matrixFileOf(matrix);
		if (f.hasMatrix()) {
			column = f.matrix().getColumn(j);
		} else if (f.hasFile()) {
			column = Array2d.readColumn(f.file(), j)
				.asDoubleArray()
				.data();
		}

		if (column == null)
			return null;
		cache.put(j, column);
		return column;
	}

	private MatrixFile matrixFileOf(LibMatrix m) {
		var matrix = _matrices.get(m);
		if (matrix != null)
			return new MatrixFile(null, matrix);
		var file = MatrixFile.of(lib, m);
		if (file.hasMatrix()) {
			_matrices.put(m, file.matrix());
		}
		return file;
	}
}
