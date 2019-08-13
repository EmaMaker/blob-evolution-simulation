import java.util.HashMap;
import java.util.Random;

public class DNA {

	HashMap<String, Float> genes = new HashMap<String, Float>();
	Random rand = new Random();
	float mutationFactor;

	void addGene(String s, float f) {
		genes.put(s, f);
	}

	float getGene(String s) {
		return genes.get(s);
	}

	DNA applyMutation() {
		DNA newDna = new DNA();

		for (String s : genes.keySet()) {
			
			//big mutation happening
			if(Config.MAIN.random(1) < 0.85f) mutationFactor = 0.01f;
			else mutationFactor = 0.95f;
			
			if (rand.nextBoolean()) {
				newDna.addGene(s, genes.get(s) + (float) (((int)Config.MAIN.random(1, 3)) * mutationFactor ));
			} else {	
				newDna.addGene(s, genes.get(s) - (float) (((int)Config.MAIN.random(1, 3)) * mutationFactor ));
			}
		}
		return newDna;
	}

}
