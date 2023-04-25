package org.openlca.libreader;

record MatrixFile(File file, boolean isSparse) {

	static MatrixFile of(Library lib, LibMatrix matrix) {
		if (lib == null || matrix == null)
			return MatrixFile(null, false);

		var npy = new File(lib.folder(), matrix.name() + ".npy");
		if (npy.exists())
			return new MatrixFile(npy, false);

		var npz = new File(lib.folder(), matrix.name() + ".npz");
		return npz.exists()
			? new MatrixFile(npz, true)
			: new MatrixFile(null, false);
	}

	boolean isEmpty() {
		return file == null || !file.exists();
	}

	public MatrixReader readFull() {
		return isEmpty()
			? null
			: NpyMatrix.read(file);
	}
}
