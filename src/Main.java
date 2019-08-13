import java.util.ArrayList;

import processing.core.*;

public class Main extends PApplet {

	ArrayList<Blob> blobs = new ArrayList<Blob>();
	ArrayList<Blob> blobsDie = new ArrayList<Blob>();
	ArrayList<Blob> blobsBorn = new ArrayList<Blob>();
	ArrayList<Food> foods = new ArrayList<Food>();
	ArrayList<Food> foodsAdd = new ArrayList<Food>();
	ArrayList<Food> foodsEaten = new ArrayList<Food>();

	int speedMult = 1;
	int oldSpeedMult = 0;
	boolean stopped = false;

	public static void main(String[] args) {
		PApplet.main("Main");
	}

	public void settings() {
		Config.MAIN = this;
		size(600, 600);

		for (int i = 0; i < random(Config.BLOB_MIN_NUMBER, Config.BLOB_MAX_NUMBER); i++) {
			addRandomBlob();
		}

		for (int i = 0; i < random(Config.BLOB_MIN_NUMBER, Config.BLOB_MAX_NUMBER); i++) {
			addRandomFood();
		}
	}

	public void setup() {

	}

	public void mousePressed() {
		if (mouseButton == LEFT) {
			addFood(mouseX, mouseY, Config.FOOD_MAX_SIDE);
		} else if(mouseButton == RIGHT) {
			addBlob(mouseX, mouseY, Config.BLOB_START_HEALTH, null);
		}
	}

	public void keyPressed() {
		if (key == '+') {
			speedMult++;
		} else if (key == '-') {
			if (speedMult > 1)
				speedMult--;
		} else if (key == 's' || key == 'S') {
			stopped = !stopped;
			if (stopped) {
				oldSpeedMult = speedMult;
				speedMult = 0;
			} else {
				speedMult = oldSpeedMult;
			}
		}else if(key == 'r' || key == 'R') {
			speedMult = 1;
		}
	}

	public void draw() {
		if (stopped) {
			background(60);
			showAll();
		}else {
			for (int i = 0; i < 1 * speedMult; i++) {
				background(60);

				updateBlobs();
				updateFoods();
				
				showAll();
			}
		}
	}

	void updateBlobs() {
		for (Blob b : blobsDie) {
			b.die();
			blobs.remove(b);
		}
		blobsDie.clear();

		for (Blob b : blobsBorn) {
			blobs.add(b);
		}
		blobsBorn.clear();

		for (Blob b : blobs) {
			b.update();
			b.move();
		}

	}

	void updateFoods() {
		if (random(1) < Config.FOOD_GEN_PROB)
			addRandomFood();

		for (Food f : foodsEaten) {
			foods.remove(f);
		}
		foodsEaten.clear();

		for (Food f : foods) {
			f.update();
		}

	}

	void showBlobs() {
		for (Blob b : blobs) {
			b.show();
		}
	}

	void showFoods() {
		for (Food f : foods) {
			f.show();
		}
	}

	void showAll() {
		showFoods();
		showBlobs();
		showInfo();
	}
	
	void showInfo() {
		stroke(255);
		fill(0);
		textSize(16);
		textAlign(LEFT);
		text("Total Blobs: " + blobs.size() + "\nTotal Foods: " + foods.size(), 25, 25);

		textAlign(RIGHT);
		if (stopped)
			text("Speed: STOP", width - 100, 25);
		else
			text("Speed: " + speedMult, width - 25, 25);
		text("Use + and - to increase and decrease speed.\n"
				+ "R to reset it to 1\n"
				+ "Left Click to add Food\n"
				+ "Right Click to add random Blob", width - 25, 50);

	}

	void addBlob(float x, float y, float health, DNA dna) {
		blobs.add(new Blob(x, y, health, dna));
	}

	void addBlob(float x, float y, DNA dna) {
		blobs.add(new Blob(x, y, dna));
	}

	void addRandomBlob() {
		addBlob(random(width), random(height), null);
	}

	void addFood(float x, float y) {
		addFood(x, y, random(Config.FOOD_MIN_SIDE, Config.FOOD_MAX_SIDE));
	}

	void addFood(float x, float y, float side) {
		foods.add(new Food(x, y, constrain(side, Config.FOOD_MIN_SIDE, Config.FOOD_MAX_SIDE)));
	}

	void addRandomFood() {
		float r = random(Config.FOOD_MIN_SIDE, Config.FOOD_MAX_SIDE);
		addFood(random(r, width - r), random(r, height - r), r);
	}

}
