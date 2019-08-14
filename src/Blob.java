
import java.util.Random;

import processing.core.PApplet;

public class Blob {

	float x, y, health, speed;
	float addx, addy, addmx, addmy, addfx, addfy, oldxc, oldyc, tx, ty, oldx, oldy, distanceTravelled = 0, oldmx, oldmy;
	long babyTime = 0, lastMeasure = 0;
	Food followedFood = null;
	DNA dna;
	static Random rand = new Random();
	
	public Blob(float x_, float y_) {
		this(x_, y_, Config.BLOB_START_HEALTH);
	}

	public Blob(float x_, float y_, float health_) {
		this(x_, y_, health_, setupDNA());
	}
	
	public Blob(float x_, float y_, DNA dna_) {
		this(x_, y_, Config.BLOB_START_HEALTH, dna_);
	}

	public Blob(float x_, float y_, float health_, DNA dna_) {
		this.x = x_;
		this.y = y_;
		this.oldmx = x_;
		this.oldmy = y_;
		
		this.health = health_;
		if (dna_ != null)
			this.dna = dna_;
		else
			this.dna = setupDNA();
		
		if (dna.getGene("radius") <= Config.BLOB_MIN_RADIUS)
			dna.setGene("radius", Config.BLOB_MIN_RADIUS);
		if (dna.getGene("radius") >= Config.BLOB_MAX_RADIUS)
			dna.setGene("radius", Config.BLOB_MAX_RADIUS);

		tx = Config.MAIN.random(1000);
		ty = Config.MAIN.random(1000, 10000);
		

		speed = Config.BLOB_RADIUS_SPEED_RATIO / dna.getGene("radius");
	}

	static DNA setupDNA() {
		DNA dna = new DNA();

		dna.setGene("radius", Config.MAIN.random(Config.BLOB_MIN_RADIUS, Config.BLOB_MAX_RADIUS));
		dna.setGene("babyTimeout", 2000 + rand.nextInt(4000));
		dna.setGene("steeringMult", (float) (rand.nextInt(15) * 0.01));
		dna.setGene("foodchaseMult", (float) (rand.nextInt(15) * 0.1));
		dna.setGene("foodchaseDist", (int) (rand.nextInt(10)));
		return dna;
	}

	void update() {
		health -= distanceTravelled * 0.025;

		
		/*Slowly the blobs that stays still for too long, so the moving ones can evolve*/
		if(System.currentTimeMillis() > lastMeasure + 2000) {
			if(Math.sqrt(Math.pow(oldmx - x, 2) + Math.pow(oldmy - y, 2)) < dna.getGene("radius") * 3){
				health *= 0.66;
				oldmx = x;
				oldmy = y;
				lastMeasure = System.currentTimeMillis();
			}
		}
		
		
		if (health < 0) {
			Config.MAIN.blobsDie.add(this);
		} else if (health >= Config.BLOB_REPRODUCE_HEALTH
				&& Config.MAIN.millis() - babyTime > dna.getGene("babyTimeout") && Config.MAIN.random(1) < 0.45) {
			health /= 2;
			haveBaby();
		}
		
	}

	void move() {
		// Movement with perlin Config.noise
		addmx = PApplet.map(Config.MAIN.noise(tx), 0, 1, -speed, speed);
		addmy = PApplet.map(Config.MAIN.noise(ty), 0, 1, -speed, speed);

		tx += (Config.MAIN.random(1) / 100) % 10000;
		ty += (Config.MAIN.random(2) / 100) % 10000;

		addx = addmx;
		addy = addmy;

		followFood();

		x += addx;
		y += addy;

		distanceTravelled = (float) Math.sqrt(Math.pow(x - oldx, 2) + Math.pow(y - oldy, 2));

		oldx = x;
		oldy = y;

		// Wrap around for borders
		if (x > Config.MAIN.width + dna.getGene("radius"))
			x = 0;
		if (x < -dna.getGene("radius"))
			x = Config.MAIN.width;
		if (y > Config.MAIN.height + dna.getGene("radius"))
			y = 0;
		if (y < -dna.getGene("radius"))
			y = Config.MAIN.height;
	}

	Food getNearestFood() {
		Food f1 = null;
		float d = Float.MAX_VALUE;

		for (Food f : Config.MAIN.foods) {
			if (distanceFromFood(f) < d) {

				x += addx;
				d = distanceFromFood(f);
				f1 = f;
			}
		}

		return f1;
	}

	float distanceFromFood(Food f) {
		if (f != null)
			return (float) (Math.sqrt(Math.pow(f.x - x, 2) + Math.pow(f.y - y, 2)));
		return Float.MAX_VALUE;
	}

	void followFood() {
		// Steering movement to reach food
		// first of all calculate the angle between blob and food using atan2
		if (followedFood == null) {
			followedFood = getNearestFood();
		}
		float dist = distanceFromFood(followedFood);

		if (dist < dna.getGene("radius") * dna.getGene("foodchaseDist")) {
			float d = dist * dna.getGene("steeringMult");
			float angle = (float) ((((Math.atan2(y - followedFood.y, x - followedFood.x) * 180 / Math.PI) - 90) + 360)
					% 360);

			// Divide angle in x and y components
			float xc = (float) (Math.cos(angle) * d);
			float yc = (float) (Math.sin(angle) * d);

			// just a little bit of tuning to not make too strict
			xc = (xc + oldxc) / 2;
			yc = (yc + oldyc) / 2;

			// Add them to the position
			x += PApplet.map(xc, -1 * d, 1 * d, -speed * dna.getGene("foodchaseMult"),
					speed * dna.getGene("foodchaseMult"));
			y += PApplet.map(yc, -1 * d, 1 * d, -speed * dna.getGene("foodchaseMult"),
					speed * dna.getGene("foodchaseMult"));

			oldxc = xc;
			oldyc = yc;

			addfx = xc;
			addfy = yc;

			// Adding to the other movement
			addx = (addmx + addfx) / 2;
			addy = (addmy + addfy) / 2;
		}

	}

	void eat(Food f) {
		health += f.side * 1.5;
	}

	void die() {
		Config.MAIN.addFood(x, y, (float) (dna.getGene("radius") * 0.75));
	}

	void haveBaby() {
		babyTime = Config.MAIN.millis();
		Config.MAIN.blobsBorn.add(new Blob(x, y, Config.BLOB_START_HEALTH, dna.applyMutation()));
	}

	void show() {
		Config.MAIN.stroke(255);
		Config.MAIN.fill(0, PApplet.map(health, 0, Config.BLOB_REPRODUCE_HEALTH, 0, 255));
		Config.MAIN.ellipse(x, y, dna.getGene("radius"), dna.getGene("radius"));

		Config.MAIN.fill(0, 100);
		Config.MAIN.textAlign(PApplet.LEFT);
		Config.MAIN.textSize(24);
		Config.MAIN.text("H: " + (int) health, x - dna.getGene("radius"), y - dna.getGene("radius"));
	}

}
