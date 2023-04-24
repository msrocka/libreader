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

	private TechIndex techIndex;
	private EnviIndex enviIndex;
	private ImpactIndex impactIndex;

	private DefaultLibReader(IDatabase db, Library lib) {
		this.db = db;
		this.lib = lib;
	}

	public static LibReader of(IDatabase db, Library lib) {
		return new DefaultLibReader(db, lib);
	}

	@Override
	public Optional<TechIndex> techIndex() {
		if (techIndex != null)
			return Optional.of(techIndex);
		var opt = lib.syncTechIndex(db);
		opt.ifPresent(techFlows -> techIndex = techFlows);
		return opt;
	}

	@Override
	public Optional<EnviIndex> enviIndex() {
		if (enviIndex != null)
			return Optional.of(enviIndex);
		var opt = lib.syncEnviIndex(db);
		opt.ifPresent(enviFlows -> enviIndex = enviFlows);
		return opt;
	}

	@Override
	public Optional<ImpactIndex> impactIndex() {
		if (impactIndex != null)
			return Optional.of(impactIndex);
		var opt = lib.syncImpactIndex(db);
		opt.ifPresent(impactDescriptors -> impactIndex = impactDescriptors);
		return opt;
	}
}
