package org.openlca.libreader;

import org.openlca.core.database.IDatabase;
import org.openlca.core.library.LibMatrix;
import org.openlca.core.library.Library;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

import javax.crypto.SecretKey;
import java.util.Objects;

public class DecryptingLibReader implements LibReader {

	private final IDatabase db;
	private final SecretKey key;
	private final LibReader reader;

	private DecryptingLibReader(SecretKey key, LibReader reader, IDatabase db) {
		this.db = Objects.requireNonNull(db);
		this.key = Objects.requireNonNull(key);
		this.reader = Objects.requireNonNull(reader);
	}

	public static LibReader of(SecretKey key, LibReader reader, IDatabase db) {
		return new DecryptingLibReader(key, reader, db);
	}

	@Override
	public Library library() {
		return reader.library();
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

	@Override
	public void dispose() {
		LibReader.super.dispose();
	}
}
