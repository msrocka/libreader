package org.openlca.libreader;

import java.util.HashMap;
import java.util.Map;

import org.openlca.core.database.IDatabase;
import org.openlca.core.library.LibraryDir;

public class LibraryDirReader {

	private final LibraryDir libDir;
	private final IDatabase db;
	private final Map<String, LibReader> readers = new HashMap<>();

	private LibraryDirReader(LibraryDir libDir, IDatabase db) {
		this.libDir = libDir;
		this.db = db;
	}

	public static LibraryDirReader of(LibraryDir libDir, IDatabase db) {
		return new LibraryDirReader(libDir, db);
	}

	/**
	 * Registers a specific reader for the given library ID. If no
	 * specific reader is registered for a library, a default reader
	 * is loaded for a library.
	 */
	public void register(String libraryId, LibReader reader) {
		readers.put(libraryId, reader);
	}


}
