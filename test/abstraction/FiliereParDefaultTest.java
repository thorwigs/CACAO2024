package abstraction;

import org.junit.Test;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.FiliereParDefaut;;

public class FiliereParDefaultTest {

	@Test
	public void testNext() {
		
		int seed = 0;
		if (System.getProperty("seed") != null) 
			seed = Integer.parseInt(System.getProperty("seed"));
		
		Filiere.LA_FILIERE = new FiliereParDefaut(seed);

		Filiere.LA_FILIERE.initialiser();

		for (int i=0; i<300; i++)
			Filiere.LA_FILIERE.next();
	}

}
