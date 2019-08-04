class Blob{
  
  float x, tx = 0, y, ty = 0, radius, speed, addmx, addmy, addx, addy;
  float steeringMult = 0.05; 
  float foodChaseMult = 0.25;
  float oldxc = 0, oldyc = 0;
  long babyTime = 0;
  
  float health;
  
  public Blob(float x_, float y_, float radius_){
    this(x_, y_, radius_, BLOB_START_HEALTH);
  }
  
  
  public Blob(float x_, float y_, float radius_, float health_){
    this.x = x_;
    this.y = y_;
    this.radius = radius_;
    this.health = health_;
    
    this.speed = BLOB_RADIUS_SPEED_RATIO / radius;
        
    tx = random(width) - 1000;
    ty = random(height) + 10000;
  }
  
  void move(){
    //Movement with perlin noise
    addmx += map(noise(tx), 0, 1, -speed, speed);
    addmy += map(noise(ty), 0, 1, -speed, speed);
        
    tx += (random(1) / 100) % 10000;
    ty += (random(2) / 100) % 10000;
        
    //Wrap around for borders
    if (x > width + radius) x = 0;
    if (x < -radius) x = width;
    if (y > height + radius) y = 0;
    if (y < -radius) y = height;
    
    followFood();
  }
  
  
  void followFood(){
    //Stearing movement to reach food
    //first of all calculate the angle between blob and food using atan2
    Food f = getNearestFood();
    if(f != null){
      float dist = distanceFromFood(f);
      
      if(dist < radius * 3){
        float d = dist * steeringMult;
        float angle = (((atan2(y - f.y, x - f.x) * 180 / PI) - 90) + 360 ) % 360; 
        
        //Divide angle in x and y components
        float xc = cos(angle) * d;
        float yc = sin(angle) * d;
        
        xc = (xc + oldxc) / 2;
        yc = (yc + oldyc) / 2;        
        
        //Add them to the position
        x += map(xc, -1 * d, 1 * d, -speed * foodChaseMult, speed * foodChaseMult);
        y += map(yc, -1 * d, 1 * d, -speed * foodChaseMult, speed * foodChaseMult);
        
        oldxc = xc;
        oldyc = yc;
        
        addx = xc + addmx;
        addy = yc + addmy;
        
        x += addx;
        y += addy;
      }
    }
  }
  
  Food getNearestFood(){
    Food f1 = null;
    float d = Float.MAX_VALUE;
    
    for(Food f : foods){
      if(distanceFromFood(f) < d) {
        d = distanceFromFood(f);
        f1 = f;
      }
    }
    
    return f1;
  }
  
  float distanceFromFood(Food f){
    return sqrt( pow(f.x - x, 2) + pow(f.y - y, 2) );
  }
  
  void update(){
    health -= speed * 0.05;
    
    if(health < 0) {
      blobsDie.add(this);
    }else if(health >= BLOB_REPRODUCE_HEALTH && millis() - babyTime > 4000 && random(1) < 0.45){
      health /= 2;
      haveBaby();
    }
  }
  
  void eat(Food f){
    health += f.side;
  }
  
  void die(){
    addFood(x, y, radius * 0.75);  
  }
  
  void haveBaby(){
    babyTime = millis();
    blobsBorn.add(new Blob(x, y, radius, BLOB_START_HEALTH));
  }
  
  void show(){
    
    stroke(255);
    fill(0, map(health, 0, BLOB_REPRODUCE_HEALTH, 0, 255));
    ellipseMode(CENTER);
    ellipse(x, y, radius, radius);
    
    fill(0, 100);
    textSize(12);
    text( "H: " + (int)health, x, y - radius);
  }
}
