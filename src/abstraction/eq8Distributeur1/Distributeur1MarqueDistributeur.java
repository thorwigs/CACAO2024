package abstraction.eq8Distributeur1;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IMarqueChocolat;

public class Distributeur1MarqueDistributeur extends Distributeur1AcheteurAppelOffre implements IMarqueChocolat{

	public List<String> getMarquesChocolat() {
		LinkedList<String> choc = new LinkedList<String>();
		choc.add("Chocoflow");
		return choc;
	}

}
