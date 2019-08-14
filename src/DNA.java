
import java.util.HashMap;
import java.util.Random;

public class DNA {

	HashMap<String, Float> genes = new HashMap<String, Float>();
	Random rand = new Random();
	float mutationFactor;

	void setGene(String s, float f) {
		if(f <= 0.1) f = 0.1f;
		genes.put(s, f);
	}

	float getGene(String s) {
		return genes.get(s);
	}
	

	DNA applyMutation() {
		DNA newDna = new DNA();

		for (String s : genes.keySet()) {

			// big mutation happening
			if (Config.MAIN.random(1) < 0.85f)
				mutationFactor = 0.1f;
			else
				mutationFactor = 0.95f;

			if (rand.nextBoolean()) {
				newDna.setGene(s,
						getGene(s) + getGene(s) * (rand.nextInt(10) % 3) * mutationFactor);
			} else {
				newDna.setGene(s,
						getGene(s) - getGene(s) * (rand.nextInt(10) % 3) * mutationFactor);
			}
		}
		return newDna;
	}

}
