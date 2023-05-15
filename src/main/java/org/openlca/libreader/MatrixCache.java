package org.openlca.libreader;

import java.util.EnumMap;
import java.util.function.Supplier;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.matrix.format.MatrixReader;

class MatrixCache {

  private final EnumMap<LibMatrix, MatrixReader> matrices;
	private final EnumMap<LibMatrix, double[]> diagonals;
	private final EnumMap<LibMatrix, TIntObjectHashMap<double[]>> columns;

	MatrixCache() {
		matrices = new EnumMap<>(LibMatrix.class);
		diagonals = new EnumMap<>(LibMatrix.class);
		columns = new EnumMap<>(LibMatrix.class);
	}

	MatrixReader matrixOf(LibMatrix m) {
		return matrices.get(m);
	}

	void put(LibMatrix m, MatrixReader matrix) {
		matrices.put(m, matrix);
	}

	double[] diagonalOf(LibMatrix m) {
		var cached = diagonals.get(m);
		if (cached != null)
			return cached;
		var matrix = matrices.get(m);
		if (matrix == null)
			return null;
		var diag = matrix.diag();
		diagonals.put(m, diag);
		return diag;
	}

	double[] diagonalOf(LibMatrix m, Supplier<double[]> fn) {
		var cached = diagonalOf(m);
		if (cached != null)
			return cached;
		var diag = fn.get();
		diagonals.put(m, diag);
		return diag;
	}

	double[] columnOf(LibMatrix m, int j) {
		var cache = columns.computeIfAbsent(
			m, $ -> new TIntObjectHashMap<>());
		var cached = cache.get(j);
		if (cached != null)
			return cached;
		var matrix = matrices.get(m);
		if (matrix == null)
			return null;
		var column = matrix.getColumn(j);
		cache.put(j, column);
		return column;
	}

	double[] columnOf(LibMatrix m, int j, Supplier<double[]> fn) {
		var cached = columnOf(m, j);
		if (cached != null)
			return cached;
		var column = fn.get();
		var cache = columns.computeIfAbsent(
			m, $ -> new TIntObjectHashMap<>());
		cache.put(j, column);
		return column;
	}

	void putColumn(LibMatrix m, int j, double[] column) {
		var cache = columns.computeIfAbsent(
			m, $ -> new TIntObjectHashMap<>());
		cache.put(j, column);
	}
}
