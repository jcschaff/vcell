package org.vcell.sybil.models.sbbox.factories;

/*   SpeciesFactory  --- by Oliver Ruebenacker, UCHC --- June to December 2009
 *   A factory for specieses
 */

import org.vcell.sybil.models.sbbox.SBBox;
import org.vcell.sybil.models.sbbox.imp.SpeciesImp;
import org.vcell.sybil.rdf.schemas.SBPAX;

import com.hp.hpl.jena.rdf.model.Resource;

@SuppressWarnings("serial")
public class SpeciesFactory extends ThingFactory<SBBox.MutableSpecies> {

	public SpeciesFactory(SBBox box) { super(box, SBPAX.Species); }
	@Override
	public SBBox.MutableSpecies newThing(Resource node) { return new SpeciesImp(box, node); }
	@Override
	public String baseLabel() { return "Species"; }

}
