package org.openlca.libreader;

import java.util.Optional;

import org.openlca.core.matrix.index.EnviIndex;
import org.openlca.core.matrix.index.ImpactIndex;
import org.openlca.core.matrix.index.TechIndex;

public interface LibReader {

	Optional<TechIndex> techIndex();

	Optional<EnviIndex> enviIndex();

	Optional<ImpactIndex> impactIndex();
}
