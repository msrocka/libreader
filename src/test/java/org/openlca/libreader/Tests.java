package org.openlca.libreader;

import org.openlca.core.database.Derby;
import org.openlca.core.database.IDatabase;

class Tests {

	private static final IDatabase db = Derby.createInMemory();

	static IDatabase db() {
		return db;
	}
}
