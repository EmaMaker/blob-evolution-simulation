ArrayList<Blob> blobs = new ArrayList();
ArrayList<Blob> blobsDie = new ArrayList(); 
ArrayList<Blob> blobsBorn = new ArrayList(); 
ArrayList<Food> foods = new ArrayList(); 
ArrayList<Food> foodsEaten = new ArrayList(); 

void setup(){
  size(600, 600);
  
  for(int i = 0; i < random(BLOB_MIN_NUMBER, BLOB_MAX_NUMBER); i++){
    addRandomBlob();
  }  
  
  for(int i = 0; i < random(FOOD_MIN_NUMBER, FOOD_MAX_NUMBER); i++){
    addRandomFood();
  }
}

void addBlob(float x, float y, float radius, float health){
  blobs.add(new Blob(x, y, constrain(radius, BLOB_MIN_RADIUS, BLOB_MAX_RADIUS), health));  
}

void addBlob(float x, float y, float radius){
  addBlob(x, y, radius, BLOB_START_HEALTH);
}

void addRandomBlob(){
  float r = random(BLOB_MIN_RADIUS, BLOB_MAX_RADIUS);
  addBlob(random(r, width-r), random(r, height-r), r);
}


void addFood(float x, float y, float radius){
  foods.add(new Food(x, y, constrain(radius, FOOD_MIN_SIDE, FOOD_MAX_SIDE)));  
}

void addRandomFood(){
  float r = random(FOOD_MIN_SIDE, FOOD_MAX_SIDE);
  addFood(random(r, width-r), random(r, height-r), r);
}


void mousePressed(){
  if(mouseButton == LEFT){
    addFood(mouseX, mouseY, FOOD_MAX_SIDE);
  }

}


void updateBlobs(){
  //Blobs death
  for(Blob b : blobsDie){
    b.die();
    blobs.remove(b);
  }
  blobsDie.clear();
  
  //Blobs birth
  for(Blob b : blobsBorn){
    blobs.add(b);
  }
  blobsBorn.clear();
  
  //Blobs update
  for(Blob b : blobs){
    b.move();
    b.update();
  }
}

void updateFoods(){
  
  //Foods update
  for(Food f : foods){
    f.update();
  }
  
  //Foods eating
  for(Food f : foodsEaten){
    foods.remove(f);
  }
  foodsEaten.clear();
}

void draw(){
  background(50);
  
  //(random(1) < 0.1) addRandomFood();
  
  updateFoods();
  updateBlobs();
    
  
  //Foods showing
  for(Food f : foods){
    f.show();
  }
  
  //Blobs showing
  for(Blob b: blobs){
    b.show();
  }
}
