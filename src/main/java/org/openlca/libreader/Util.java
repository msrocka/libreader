package org.openlca.libreader;

import org.openlca.core.library.Library;

import java.io.File;
import java.util.List;

interface Util {

	static boolean hasUncertainty(Library lib) {
		if (lib == null)
			return false;
		for (var m : List.of("A", "B", "C")) {
			var prefix = m + "_utype.";
			for (var suffix : List.of("npy", "npz")) {
				var file = new File(lib.folder(), prefix + suffix);
				if (file.exists())
					return true;
			}
		}
		return false;
	}
}
