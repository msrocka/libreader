package org.openlca.libreader;

import java.util.Optional;

import org.openlca.core.database.IDatabase;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public class DefaultLibReader implements LibReader {

	private final IDatabase db;
	private final Library lib;

	// cached values
	private TechIndex _techIndex;
	private EnviIndex _enviIndex;
	private ImpactIndex _impactIndex;
	private double[] _costs;

	private final EnumMap<LibMatrix, MatrixReader> _matrices;
	private final EnumMap<LibMatrix, double[]> _diagonals;
	private final EnumMap<LibMatrix, TIntObjectHashMap<double[]> _columns;

	private DefaultLibReader(IDatabase db, Library lib) {
		this.db = db;
		this.lib = lib;
		_matrices = new EnumHashMap<>(LibMatrix.class);
		_diagonals = new EnumHashMap<LibMatrix.class>;
		_columns = new EnumHashMap<LibMatrix.class>;
	}

	public static LibReader of(IDatabase db, Library lib) {
		return new DefaultLibReader(db, lib);
	}

	@Override
	public TechIndex techIndex() {
		if (_techIndex == null) {
			_techIndex = lib.syncTechIndex(db).orElseNull();
		}
		return _techIndex;
	}

	@Override
	public EnviIndex enviIndex() {
		if (_enviIndex == null) {
			_enviIndex = lib.syncEnviIndex(db).orElseNull();
		}
		return _enviIndex;
	}

	@Override
	public ImpactIndex impactIndex() {
		if (_impactIndex == null) {
			_impactIndex = lib.syncImpactIndex(db).orElseNull();
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
			_costs = lib.getCosts();
		}
		return _costs;
	}

	@Override
	public double[] diagonalOf(LibMatrix matrix) {
		return _diagonals.computeIfAbsent(matrix, m -> {
				// when the full-matrix was already loaded,
				// read it from that matrix in-memory
				var fullMatrix = _matrices.get(matrix);
				if (fullMatrix != null) {
					return fullMatrix.diag();
				}

				// if the matrix is sparse then read the
				// full matrix, cache it, and extract the
				// diagonal
				var file = MatrixFile.of(dir, matrix);
				if (file.isEmpty())
					return null;
				if (file.isSparse()) {
					fullMatrix = file.readFull();
					_matrices.put(matrix, fullMatrix);
					return fullMatrix.diag();
				}

				// finally, read it from a dense array
				// on disk
				return Array2d.readDiag(file.file())
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

	// check if the matrix is cached already
	var fullMatrix = _matrices.get(matrix);
	if (fullMatrix != null) {
		column = fullMatrix.column(j);
		cache.put(j, column);
		return column;
	}

	// read & cache the full matrix in case
	// of a sparce matrix file
	var file = MatrixFile.of(lib, matrix);
	if (file.isEmpty())
		return null;
	if (file.isSparse()) {
		fullMatrix = file.readFull();
		_matrices.
			}


}

	private MatrixFile fullIfSparse


}
