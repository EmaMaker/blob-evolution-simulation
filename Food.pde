class Food{
  
  float x, y, side, energy, mass;
  
  public Food(float x_, float y_, float side_){
    x = x_;
    y = y_;
    side = side_;
    mass = side*side;
    
    energy = mass*FOOD_MASS_ENERGY_RATIO;
  }
  
  void update(){
    for(Blob b : blobs){
      float fcx = x + side / 2;
      float fcy = y + side / 2;
      
      if( distanceFromBlob(b) < b.radius ){
        //food eaten by the blob
        foodsEaten.add(this);
        b.eat(this);
        break;
      }
    }
  }
  
  void show(){
    fill(255, 0, 0);
    stroke(255, 0, 0);
    rect(x, y, side, side);
  }
  
  Blob getNearestBlob(){
    Blob f1 = null;
    float d = Float.MAX_VALUE;
    
    for(Blob f : blobs){
      if(distanceFromBlob(f) < d) {
        d = distanceFromBlob(f);
        f1 = f;
      }
    }
    
    return f1;
  }
  
  
  float distanceFromBlob(Blob b){
    return sqrt( pow(b.x - x, 2) + pow(b.y - y, 2) );
  }
  
}
