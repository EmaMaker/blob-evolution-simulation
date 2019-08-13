public class Food {

	float x, y, side, energy, mass;

	public Food(float x_, float y_, float side_) {
		x = x_;
		y = y_;
		side = side_;
		mass = side * side;

		energy = mass * Config.FOOD_MASS_ENERGY_RATIO;
	}

	void update() {
		for (Blob b : Config.MAIN.blobs) {

			if (distanceFromBlob(b) < b.dna.getGene("radius")) {
				// food eaten by the blob
				Config.MAIN.foodsEaten.add(this);
				b.eat(this);
				break;
			}
		}
	}

	void show() {
		Config.MAIN.fill(255, 0, 0);
		Config.MAIN.stroke(255, 0, 0);
		Config.MAIN.rect(x, y, side, side);
	}

	Blob getNearestBlob() {
		Blob f1 = null;
		float d = Float.MAX_VALUE;

		for (Blob f : Config.MAIN.blobs) {
			if (distanceFromBlob(f) < d) {
				d = distanceFromBlob(f);
				f1 = f;
			}
		}

		return f1;
	}

	float distanceFromBlob(Blob b) {
		float fcx = x + side / 2;
		float fcy = y + side / 2;
		return (float) Math.sqrt(Math.pow(b.x - fcx, 2) + Math.pow(b.y - fcy, 2));
	}

}