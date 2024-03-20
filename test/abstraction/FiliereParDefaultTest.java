package abstraction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;

import org.junit.Test;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.FiliereParDefaut;;

public class FiliereParDefaultTest {

	@Test
	public void testNext() {
		Filiere.LA_FILIERE = null;
		Filiere.LA_FILIERE = new FiliereParDefaut();
		Filiere.random = new Random(LocalDate.now(ZoneId.of("Europe/Paris")).toEpochDay());

		Filiere.LA_FILIERE.initialiser();

		for (int i=0; i<300; i++)
			Filiere.LA_FILIERE.next();
	}

}
