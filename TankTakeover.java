<<<<<<< HEAD
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
//Importing my stuff

class PlayerTank {
	int clazz, centerx, centery, angle, maxreload, currentreload, damage, currenthealth, bulletspeed, bulletsize, bulletpenetration;
	boolean turnleft, turnright, shooting;
	Circle base;
	Line barrel;
	Text healthdisplay;
	
	PlayerTank(int clazz, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int currenthealth, int bulletspeed, int bulletsize, int bulletpenetration, boolean turnleft, boolean turnright, boolean shooting, Circle base, Line barrel, Text healthdisplay) {
		this.clazz = clazz;
		this.centerx = centerx;
		this.centery = centery;
		this.angle = angle;
		this.maxreload = maxreload;
		this.currentreload = currentreload;
		this.damage = damage;
		this.currenthealth = currenthealth;
		this.bulletspeed = bulletspeed;
		this.bulletsize = bulletsize;
		this.bulletpenetration = bulletpenetration;
		this.turnleft = turnleft;
		this.turnright = turnright;
		this.shooting = shooting;
		this.base = base;
		this.barrel = barrel;
		this.healthdisplay = healthdisplay;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		healthdisplay.toFront();
		return;
	}
	
	public void moveLR() {
		angle += turnright ? 3 : (turnleft ? -3 : 0);
		Math.floorMod(angle, 360);
		return;
	}
	
	public void updateBarrel() {
		barrel.setEndX(100 + (80 * centerx) + 25 * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(60 + (80 * centery) - 25 * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(currenthealth));
		return;
	}
	
	public boolean fireBullet() {
		
		if (currentreload != 0) {
			currentreload--;
		}
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
}

class DroneTank {
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, maxhealth, currenthealth, bulletspeed, bulletsize, bulletpenetration, activetarget;
	boolean underattack, shooting, targeting;
	Circle base, ownerflag;
	Line barrel;
	Text healthdisplay;
	
	DroneTank(int clazz, int owner, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int maxhealth, int health, int bulletspeed, int bulletsize, int bulletpenetration, int activetarget, boolean underattack, boolean shooting, boolean targeting, Circle base, Circle ownerflag, Line barrel, Text healthdisplay) {
		this.clazz = clazz;
		this.owner = owner;
		this.centerx = centerx;
		this.centery = centery;
		this.angle = angle;
		this.maxreload = maxreload;
		this.currentreload = currentreload;
		this.damage = damage;
		this.maxhealth = maxhealth;
		this.currenthealth = currenthealth;
		this.bulletspeed = bulletspeed;
		this.bulletsize = bulletsize;
		this.bulletpenetration = bulletpenetration;
		this.activetarget = activetarget;
		this.underattack = underattack;
		this.shooting = shooting;
		this.targeting = targeting;
		this.base = base;
		this.ownerflag = ownerflag;
		this.barrel = barrel;
		this.healthdisplay = healthdisplay;
	}
	
	public void setDroneClass(int droneclass) {
		clazz = droneclass;
		return;
	}
	
	public void setClassAttributes() {
		
		switch (clazz) {
			case 0:
				maxreload = currentreload = 90;
				damage = 5;
				maxhealth = currenthealth = 50;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(20);
				ownerflag.setRadius(0.65 * base.getRadius());
				barrel.setStrokeWidth(6);
				barrel.setEndY(60 + (80 * centery) - 25);
				break;
			case 1:
				maxreload = currentreload = 180;
				damage = 15;
				maxhealth = currenthealth = 30;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 2;
				base.setRadius(15);
				ownerflag.setRadius(0.65 * base.getRadius());
				barrel.setStrokeWidth(4);
				barrel.setEndY(60 + (80 * centery) - 30);
				break;
			case 2:
				maxreload = currentreload = 5;
				damage = 1;
				maxhealth = currenthealth = 80;
				bulletspeed = 2;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(30);
				ownerflag.setRadius(0.65 * base.getRadius());
				barrel.setStrokeWidth(9);
				barrel.setEndY(60 + (80 * centery) - 31);
				break;
			case 3:
				maxreload = currentreload = 300;
				damage = 100;
				maxhealth = currenthealth = 150;
				bulletspeed = 1;
				bulletsize = 8; 
				bulletpenetration = 10;
				base.setRadius(30);
				ownerflag.setRadius(0.65 * base.getRadius());
				barrel.setStrokeWidth(18);
				barrel.setEndY(60 + (80 * centery) - 35);
		}
		
		return;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		ownerflag.toFront();
		healthdisplay.toFront();
		return;
	}
	
	public int getBarrelLength() {
		int barrellength = 25;
		
		switch (clazz) {
			case 0: barrellength = 25; break;
			case 1: barrellength = 30; break;
			case 2: barrellength = 31; break;
			case 3: barrellength = 35; break;
		}
		
		return barrellength;
	}
	
	public void updateBarrel(int barrellength) {
		barrel.setEndX(100 + (80 * centerx) + barrellength * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(60 + (80 * centery) - barrellength * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(currenthealth));
		return;
	}
	
	public void toTeam(int newowner) {
		
		if (owner == -1) {
			owner = newowner;
			ownerflag.setFill(newowner == 0 ? Color.BLUE : Color.RED);
			ownerflag.toFront();
			currenthealth = maxhealth;
			targeting = shooting = false;
			healthdisplay.toFront();
		} else {
			owner = -1;
			ownerflag.setFill(Color.GRAY);
			currenthealth = maxhealth;
			healthdisplay.toFront();
		}
		
		return;
	}
	
	public void findNewTarget(DroneTank[][] temptankdrones, PlayerTank[] temptankplayers) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		int i1;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (i1 = 0; i1 < 77; i1++) {
			
			if (temptankdrones[i1 / 7][i1 % 7] != null) {
			
				if (Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2) < closestsquaredistance && owner != temptankdrones[i1 / 7][i1 % 7].owner) {
					closestsquaredistance = Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2);
					closesttarget = i1;
					possibletargets.clear();
					possibletargets.add(i1);
				} else if (Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2) == closestsquaredistance && owner != temptankdrones[i1 / 7][i1 % 7].owner) {
					possibletargets.add(i1);
				}
				
			}
			
		}
		
		closesttarget = closestsquaredistance == 99999 ? (owner == 0 ? 73 : 3) : possibletargets.get(random.nextInt(possibletargets.size()));
		activetarget = closesttarget;
		targeting = true;
		return;
	}
	
	public void turnToTarget(int tempcenterx, int tempcentery) {
		int targetangle;
		double temptargetangle;
		temptargetangle = Math.atan2(tempcentery - centery, tempcenterx - centerx) + Math.PI / 2.0;
		targetangle = (int) Math.round(Math.toDegrees(temptargetangle));
		targetangle = Math.floorMod(targetangle, 360);
		angle += angle < targetangle ? bulletspeed : (angle > targetangle ? -bulletspeed : 0);
		angle = Math.floorMod(angle, 360);
		
		if (angle <= targetangle + bulletspeed && angle >= targetangle - bulletspeed) {
			angle = targetangle;
			shooting = true;
		}
		
	}
	
	public boolean fireBullet() {
		
		if (currentreload != 0) {
			currentreload--;
		}
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
}

class TankProjectile {
	int owner, angle, speed, size, damage, penetration;
	double centerx, centery;
	Circle body;
	
	TankProjectile(int owner, int angle, int speed, int size, int damage, int penetration, double centerx, double centery, Circle body) {
		this.owner = owner;
		this.angle = angle;
		this.speed = speed;
		this.size = size;
		this.damage = damage;
		this.penetration = penetration;
		this.centerx = centerx;
		this.centery = centery;
		this.body = body;
	}
	
	public void create(int tankowner, int tankcenterx, int tankcentery, int tankangle, int tankbulletspeed, int tankbulletsize, int tankdamage, int tankpenetration) {
		owner = tankowner;
		centerx = tankcenterx;
		centery = tankcentery;
		angle = tankangle;
		speed = tankbulletspeed;
		size = tankbulletsize;
		damage = tankdamage;
		penetration = tankpenetration;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
		body.toBack();
		
		return;
	}
	
	public void updatePosition() {
		centerx += speed * Math.sin(Math.toRadians((double) angle));
		centery -= speed * Math.cos(Math.toRadians((double) angle));
		
		body.setCenterX(centerx);
		body.setCenterY(centery);
		
		return;
	}
	
}

public class TankTakeover extends Application {
	
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }
 
	public static int STAGE_WIDTH = 1000;
	public static int STAGE_HEIGHT = 600;
	public static int DISTANCE_FROM_BORDER_X = 100;
	public static int DISTANCE_FROM_BORDER_Y = 60;
	public static int LENGTH_OF_GRID_SQUARE = 80;
	public static int LENGTH_OF_BARREL = 25;
	
	int i, j, k = 0;
	boolean inthelevel = true;
	boolean bullettoborder, bullettotank, bullettobullet = false;
	PlayerTank tankplayers[] = new PlayerTank[2];
	DroneTank tankdrones[][] = new DroneTank[11][7];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
 
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Tank Takeover");
		
		Group root = new Group();
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		
		tankplayers[0] = new PlayerTank(0, 0, 3, 0, 30, 30, 5, 100, 5, 3, 1, false, false, false, new Circle(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.BLUE), new Line(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		tankplayers[1] = new PlayerTank(0, 10, 3, 0, 30, 30, 5, 100, 5, 3, 1, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.RED), new Line(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		
		tankplayers[0].barrel.setStroke(Color.BLUE);
		tankplayers[1].barrel.setStroke(Color.RED);
		
		for (i = 0; i < 2; i++) {
			tankplayers[i].barrel.setStrokeWidth(6);
			tankplayers[i].healthdisplay.setText(Integer.toString(tankplayers[i].currenthealth));
			root.getChildren().addAll(tankplayers[i].base, tankplayers[i].barrel, tankplayers[i].healthdisplay);
		}
		
		for (i = 0; i < 11; i++) {
			
			for (j = 0; j < 7; j++) {
				
				if (!(i == 0 && j == 3) && !(i == 10 && j == 3)) {
					tankdrones[i][j] = new DroneTank(0, -1, i, j, 0, 90, 90, 5, 50, 50, 5, 3, 1, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 20, Color.GRAY), new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 13, Color.GRAY), new Line(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) + 4, ""));
					
					tankdrones[i][j].barrel.setStroke(Color.GRAY);
					tankdrones[i][j].setDroneClass(3);
					tankdrones[i][j].setClassAttributes();
					tankdrones[i][j].healthdisplay.setText(Integer.toString(tankdrones[i][j].currenthealth));
					root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthdisplay);
				}
				
			}
			
		}
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (inthelevel) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = true;
							break;
						case D:
							tankplayers[0].turnright = true;
							break;
						case W:
							tankplayers[0].shooting = true;
							break;
						case J:
							tankplayers[1].turnleft = true;
							break;
						case L:
							tankplayers[1].turnright = true;
							break;
						case I:
							tankplayers[1].shooting = true;
							break;
					}
					
				}
				
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (inthelevel) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = false;
							break;
						case D:
							tankplayers[0].turnright = false;
							break;
						case W:
							tankplayers[0].shooting = false;
							break;
						case J:
							tankplayers[1].turnleft = false;
							break;
						case L:
							tankplayers[1].turnright = false;
							break;
						case I:
							tankplayers[1].shooting = false;
							break;
					}
					
				}
				
			}
			
		});
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
			
            public void handle(long now) {
				
				for (i = 0; i < 2; i++) {
					tankplayers[i].moveLR();
					tankplayers[i].updateBarrel();
					tankplayers[i].updateHealthDisplay();
					
					if (tankplayers[i].fireBullet()) {
						allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
						allprojectiles.get(allprojectiles.size() - 1).create(i, DISTANCE_FROM_BORDER_X + (tankplayers[i].centerx * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (tankplayers[i].centery * LENGTH_OF_GRID_SQUARE), tankplayers[i].angle, tankplayers[i].bulletspeed, tankplayers[i].bulletsize, tankplayers[i].damage, tankplayers[i].bulletpenetration);
						root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
						
						tankplayers[i].moveToFront();
					}
					
				}
				
				for (i = 0; i < 77; i++) {
					
					if (tankdrones[i / 7][i % 7] != null) {
						tankdrones[i / 7][i % 7].updateHealthDisplay();
						
						if (!tankdrones[i / 7][i % 7].shooting && tankdrones[i / 7][i % 7].owner != -1) {
							
							if (!tankdrones[i / 7][i % 7].targeting) {
								tankdrones[i / 7][i % 7].findNewTarget(tankdrones, tankplayers);
							} else {
								
								if (tankdrones[i / 7][i % 7].activetarget != 3 && tankdrones[i / 7][i % 7].activetarget != 73) {
									tankdrones[i / 7][i % 7].turnToTarget(tankdrones[tankdrones[i / 7][i % 7].activetarget / 7][tankdrones[i / 7][i % 7].activetarget % 7].centerx, tankdrones[tankdrones[i / 7][i % 7].activetarget / 7][tankdrones[i / 7][i % 7].activetarget % 7].centery);
								} else {
									
									if (tankdrones[i / 7][i % 7].activetarget == 3) {
										tankdrones[i / 7][i % 7].turnToTarget(tankplayers[0].centerx, tankplayers[0].centery);
									} else {
										tankdrones[i / 7][i % 7].turnToTarget(tankplayers[1].centerx, tankplayers[1].centery);
									}
									
								}
								
								tankdrones[i / 7][i % 7].updateBarrel(tankdrones[i / 7][i % 7].getBarrelLength());
							}
							
						}
						
						if (tankdrones[i / 7][i % 7].fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(tankdrones[i / 7][i % 7].owner, DISTANCE_FROM_BORDER_X + (tankdrones[i / 7][i % 7].centerx * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (tankdrones[i / 7][i % 7].centery * LENGTH_OF_GRID_SQUARE), tankdrones[i / 7][i % 7].angle, tankdrones[i / 7][i % 7].bulletspeed, tankdrones[i / 7][i % 7].bulletsize, tankdrones[i / 7][i % 7].damage, tankdrones[i / 7][i % 7].bulletpenetration);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							
							tankdrones[i / 7][i % 7].moveToFront();
						}
						
					}
					
				}
				
				for (i = allprojectiles.size() - 1; i >= 0; i--) {
					allprojectiles.get(i).updatePosition();
					
					if (allprojectiles.get(i).centerx < 0 || allprojectiles.get(i).centerx > STAGE_WIDTH || allprojectiles.get(i).centery < 0 || allprojectiles.get(i).centery > STAGE_HEIGHT) {
						allprojectiles.get(i).body.setVisible(false);
						allprojectiles.remove(i);
						bullettoborder = true;
					}
					
					for (j = 0; j < 77 && !(bullettoborder || bullettotank); j++) {
						
						if (tankdrones[j / 7][j % 7] != null) {
						
							if (allprojectiles.get(i).body.getBoundsInParent().intersects(tankdrones[j / 7][j % 7].base.getBoundsInParent()) && allprojectiles.get(i).owner != tankdrones[j / 7][j % 7].owner) {
								tankdrones[j / 7][j % 7].currenthealth -= allprojectiles.get(i).damage;
								allprojectiles.get(i).body.setVisible(false);
								
								if (tankdrones[j / 7][j % 7].currenthealth <= 0) {
									tankdrones[j / 7][j % 7].toTeam(allprojectiles.get(i).owner);
									
									if (tankdrones[j / 7][j % 7].owner != -1) {
										
										for (k = 0; k < 77; k++) {
											
											if (tankdrones[k / 7][k % 7] != null) {
												
												if (tankdrones[k / 7][k % 7].activetarget == j) {
													tankdrones[k / 7][k % 7].targeting = false;
													tankdrones[k / 7][k % 7].shooting = false;
												}
												
											}
											
										}
										
									}
									
								}
								
								allprojectiles.remove(i);
								bullettotank = true;
							}
							
						}
					
					}
					
					for (j = 0; j < 2 && !(bullettoborder || bullettotank); j++) {
						
						if (allprojectiles.get(i).body.getBoundsInParent().intersects(tankplayers[j].base.getBoundsInParent()) && allprojectiles.get(i).owner != j) {
							tankplayers[j].currenthealth -= allprojectiles.get(i).damage;
							allprojectiles.get(i).body.setVisible(false);
							allprojectiles.remove(i);
							bullettotank = true;
						}
						
					}
					
					for (j = 0; j < allprojectiles.size() && !(bullettoborder || bullettotank || bullettobullet); j++) {
						
						if (allprojectiles.get(i).body.getBoundsInParent().intersects(allprojectiles.get(j).body.getBoundsInParent()) && allprojectiles.get(j).owner != allprojectiles.get(i).owner) {
							
							if (allprojectiles.get(i).penetration > allprojectiles.get(j).penetration) {
								allprojectiles.get(i).penetration -= allprojectiles.get(j).penetration;
								allprojectiles.get(j).body.setVisible(false);
								allprojectiles.remove(j);
							} else if (allprojectiles.get(i).penetration < allprojectiles.get(j).penetration) {
								allprojectiles.get(j).penetration -= allprojectiles.get(i).penetration;
								allprojectiles.get(i).body.setVisible(false);
								allprojectiles.remove(i);
							} else {
								allprojectiles.get(i).body.setVisible(false);
								allprojectiles.get(j).body.setVisible(false);
								
								if (i < j) {
									allprojectiles.remove(j);
									allprojectiles.remove(i);
								} else {
									allprojectiles.remove(i);
									allprojectiles.remove(j);
								}
								
							}
							
							bullettobullet = true;
						}
						
					}
					
					bullettoborder = bullettotank = bullettobullet = false;
				}
				
			}
			
		};
		timer.start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
=======
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
//Importing my stuff

class PlayerTank {
	int clazz, centerx, centery, angle, maxreload, currentreload, damage, health, bulletspeed, bulletsize, bulletpenetration;
	boolean turnleft, turnright, shooting;
	Circle base;
	Line barrel;
	Text healthdisplay;
	
	PlayerTank(int clazz, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int health, int bulletspeed, int bulletsize, int bulletpenetration, boolean turnleft, boolean turnright, boolean shooting, Circle base, Line barrel, Text healthdisplay) {
		this.clazz = clazz;
		this.centerx = centerx;
		this.centery = centery;
		this.angle = angle;
		this.maxreload = maxreload;
		this.currentreload = currentreload;
		this.damage = damage;
		this.health = health;
		this.bulletspeed = bulletspeed;
		this.bulletsize = bulletsize;
		this.bulletpenetration = bulletpenetration;
		this.turnleft = turnleft;
		this.turnright = turnright;
		this.shooting = shooting;
		this.base = base;
		this.barrel = barrel;
		this.healthdisplay = healthdisplay;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		healthdisplay.toFront();
		return;
	}
	
	public void moveLR() {
		angle += turnright ? 3 : (turnleft ? -3 : 0);
		Math.floorMod(angle, 360);
		return;
	}
	
	public void updateBarrel() {
		barrel.setEndX(100 + (80 * centerx) + 25 * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(60 + (80 * centery) - 25 * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(health));
		return;
	}
	
	public boolean fireBullet() {
		
		if (currentreload != 0) {
			currentreload--;
		}
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
}

class DroneTank {
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, health, bulletspeed, bulletsize, bulletpenetration, activetarget;
	boolean underattack, shooting, targeting;
	Circle base, ownerflag;
	Line barrel;
	Text healthdisplay;
	
	DroneTank(int clazz, int owner, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int health, int bulletspeed, int bulletsize, int bulletpenetration, int activetarget, boolean underattack, boolean shooting, boolean targeting, Circle base, Circle ownerflag, Line barrel, Text healthdisplay) {
		this.clazz = clazz;
		this.owner = owner;
		this.centerx = centerx;
		this.centery = centery;
		this.angle = angle;
		this.maxreload = maxreload;
		this.currentreload = currentreload;
		this.damage = damage;
		this.health = health;
		this.bulletspeed = bulletspeed;
		this.bulletsize = bulletsize;
		this.bulletpenetration = bulletpenetration;
		this.activetarget = activetarget;
		this.underattack = underattack;
		this.shooting = shooting;
		this.targeting = targeting;
		this.base = base;
		this.ownerflag = ownerflag;
		this.barrel = barrel;
		this.healthdisplay = healthdisplay;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		ownerflag.toFront();
		healthdisplay.toFront();
		return;
	}
	
	public void updateBarrel() {
		barrel.setEndX(100 + (80 * centerx) + 25 * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(60 + (80 * centery) - 25 * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(health));
		return;
	}
	
	public void toTeam(int newowner) {
		
		if (owner == -1) {
			owner = newowner;
			ownerflag.setFill(newowner == 0 ? Color.BLUE : Color.RED);
			ownerflag.toFront();
			health = 50;
			healthdisplay.toFront();
		} else {
			owner = -1;
			ownerflag.setFill(Color.GRAY);
			health = 50;
			healthdisplay.toFront();
		}
		
		return;
	}
	
	public void findNewTarget(DroneTank[][] temptankdrones, PlayerTank[] temptankplayers) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		int i1;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (i1 = 0; i1 < 77; i1++) {
			
			if (temptankdrones[i1 / 7][i1 % 7] != null) {
			
				if (Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2) < closestsquaredistance && owner != temptankdrones[i1 / 7][i1 % 7].owner) {
					closestsquaredistance = Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2);
					closesttarget = i1;
					possibletargets.clear();
					possibletargets.add(i1);
				} else if (Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2) == closestsquaredistance && owner != temptankdrones[i1 / 7][i1 % 7].owner) {
					possibletargets.add(i1);
				}
				
			}
			
		}
		
		closesttarget = closestsquaredistance == 99999 ? (owner == 0 ? 73 : 3) : possibletargets.get(random.nextInt(possibletargets.size()));
		activetarget = closesttarget;
		targeting = true;
		return;
	}
	
	public void turnToTarget(int tempcenterx, int tempcentery) {
		int targetangle;
		double temptargetangle;
		temptargetangle = Math.atan2(tempcentery - centery, tempcenterx - centerx) + Math.PI / 2.0;
		targetangle = (int) Math.round(Math.toDegrees(temptargetangle));
		targetangle = Math.floorMod(targetangle, 360);
		angle += angle < targetangle ? 5 : (angle > targetangle ? -5 : 0);
		angle = Math.floorMod(angle, 360);
		
		if (angle <= targetangle + 5 && angle >= targetangle - 5) {
			angle = targetangle;
			shooting = true;
		}
		
	}
	
	public boolean fireBullet() {
		
		if (currentreload != 0) {
			currentreload--;
		}
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
}

class TankProjectile {
	int owner, angle, speed, size, damage, penetration;
	double centerx, centery;
	Circle body;
	
	TankProjectile(int owner, int angle, int speed, int size, int damage, int penetration, double centerx, double centery, Circle body) {
		this.owner = owner;
		this.angle = angle;
		this.speed = speed;
		this.size = size;
		this.damage = damage;
		this.penetration = penetration;
		this.centerx = centerx;
		this.centery = centery;
		this.body = body;
	}
	
	public void create(int tankowner, int tankcenterx, int tankcentery, int tankangle, int tankbulletspeed, int tankbulletsize, int tankdamage, int tankpenetration) {
		owner = tankowner;
		centerx = tankcenterx;
		centery = tankcentery;
		angle = tankangle;
		speed = tankbulletspeed;
		size = tankbulletsize;
		damage = tankdamage;
		penetration = tankpenetration;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
		body.toBack();
		
		return;
	}
	
	public void updatePosition() {
		centerx += speed * Math.sin(Math.toRadians((double) angle));
		centery -= speed * Math.cos(Math.toRadians((double) angle));
		
		body.setCenterX(centerx);
		body.setCenterY(centery);
		
		return;
	}
	
}

public class TankTakeover extends Application {
	
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }
 
	public static int STAGE_WIDTH = 1000;
	public static int STAGE_HEIGHT = 600;
	public static int DISTANCE_FROM_BORDER_X = 100;
	public static int DISTANCE_FROM_BORDER_Y = 60;
	public static int LENGTH_OF_GRID_SQUARE = 80;
	public static int LENGTH_OF_BARREL = 25;
	
	int i, j, k = 0;
	boolean inthelevel = true;
	boolean bullettoborder, bullettotank, bullettobullet = false;
	PlayerTank tankplayers[] = new PlayerTank[2];
	DroneTank tankdrones[][] = new DroneTank[11][7];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
 
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Tank Takeover");
		
		Group root = new Group();
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		
		tankplayers[0] = new PlayerTank(0, 0, 3, 0, /*30*/0, /*30*/0, /*5*/1, 100, 5, 3, 1, false, false, false, new Circle(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.BLUE), new Line(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		tankplayers[1] = new PlayerTank(0, 10, 3, 0, 30, 30, 5, 100, 5, 3, 1, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.RED), new Line(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		
		tankplayers[0].barrel.setStroke(Color.BLUE);
		tankplayers[1].barrel.setStroke(Color.RED);
		
		for (i = 0; i < 2; i++) {
			tankplayers[i].barrel.setStrokeWidth(6);
			tankplayers[i].healthdisplay.setText(Integer.toString(tankplayers[i].health));
			root.getChildren().addAll(tankplayers[i].base, tankplayers[i].barrel, tankplayers[i].healthdisplay);
		}
		
		for (i = 0; i < 11; i++) {
			
			for (j = 0; j < 7; j++) {
				
				if (!(i == 0 && j == 3) && !(i == 10 && j == 3)) {
					tankdrones[i][j] = new DroneTank(0, -1, i, j, 0, 90, 90, 5, 50, 5, 3, 1, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 20, Color.GRAY), new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 13, Color.GRAY), new Line(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Text(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) + 4, ""));
					
					tankdrones[i][j].barrel.setStroke(Color.GRAY);
					tankdrones[i][j].barrel.setStrokeWidth(6);
					tankdrones[i][j].healthdisplay.setText(Integer.toString(tankdrones[i][j].health));
					root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthdisplay);
				}
				
			}
			
		}
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (inthelevel) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = true;
							break;
						case D:
							tankplayers[0].turnright = true;
							break;
						case W:
							tankplayers[0].shooting = true;
							break;
						case J:
							tankplayers[1].turnleft = true;
							break;
						case L:
							tankplayers[1].turnright = true;
							break;
						case I:
							tankplayers[1].shooting = true;
							break;
					}
					
				}
				
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (inthelevel) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = false;
							break;
						case D:
							tankplayers[0].turnright = false;
							break;
						case W:
							tankplayers[0].shooting = false;
							break;
						case J:
							tankplayers[1].turnleft = false;
							break;
						case L:
							tankplayers[1].turnright = false;
							break;
						case I:
							tankplayers[1].shooting = false;
							break;
					}
					
				}
				
			}
			
		});
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
			
            public void handle(long now) {
				
				for (i = 0; i < 2; i++) {
					tankplayers[i].moveLR();
					tankplayers[i].updateBarrel();
					tankplayers[i].updateHealthDisplay();
					
					if (tankplayers[i].fireBullet()) {
						allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
						allprojectiles.get(allprojectiles.size() - 1).create(i, DISTANCE_FROM_BORDER_X + (tankplayers[i].centerx * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (tankplayers[i].centery * LENGTH_OF_GRID_SQUARE), tankplayers[i].angle, tankplayers[i].bulletspeed, tankplayers[i].bulletsize, tankplayers[i].damage, tankplayers[i].bulletpenetration);
						root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
						
						tankplayers[i].moveToFront();
					}
					
				}
				
				for (i = 0; i < 77; i++) {
					
					if (tankdrones[i / 7][i % 7] != null) {
						tankdrones[i / 7][i % 7].updateHealthDisplay();
						
						if (!tankdrones[i / 7][i % 7].shooting && tankdrones[i / 7][i % 7].owner != -1) {
							
							if (!tankdrones[i / 7][i % 7].targeting) {
								tankdrones[i / 7][i % 7].findNewTarget(tankdrones, tankplayers);
							} else {
								
								if (tankdrones[i / 7][i % 7].activetarget != 3 && tankdrones[i / 7][i % 7].activetarget != 73) {
									tankdrones[i / 7][i % 7].turnToTarget(tankdrones[tankdrones[i / 7][i % 7].activetarget / 7][tankdrones[i / 7][i % 7].activetarget % 7].centerx, tankdrones[tankdrones[i / 7][i % 7].activetarget / 7][tankdrones[i / 7][i % 7].activetarget % 7].centery);
								} else {
									
									if (tankdrones[i / 7][i % 7].activetarget == 3) {
										tankdrones[i / 7][i % 7].turnToTarget(tankplayers[0].centerx, tankplayers[0].centery);
									} else {
										tankdrones[i / 7][i % 7].turnToTarget(tankplayers[1].centerx, tankplayers[1].centery);
									}
									
								}
								
								tankdrones[i / 7][i % 7].updateBarrel();
							}
							
						}
						
						if (tankdrones[i / 7][i % 7].fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(tankdrones[i / 7][i % 7].owner, DISTANCE_FROM_BORDER_X + (tankdrones[i / 7][i % 7].centerx * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (tankdrones[i / 7][i % 7].centery * LENGTH_OF_GRID_SQUARE), tankdrones[i / 7][i % 7].angle, tankdrones[i / 7][i % 7].bulletspeed, tankdrones[i / 7][i % 7].bulletsize, tankdrones[i / 7][i % 7].damage, tankdrones[i / 7][i % 7].bulletpenetration);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							
							tankdrones[i / 7][i % 7].moveToFront();
						}
						
					}
					
				}
				
				for (i = allprojectiles.size() - 1; i >= 0; i--) {
					allprojectiles.get(i).updatePosition();
					
					if (allprojectiles.get(i).centerx < 0 || allprojectiles.get(i).centerx > STAGE_WIDTH || allprojectiles.get(i).centery < 0 || allprojectiles.get(i).centery > STAGE_HEIGHT) {
						allprojectiles.get(i).body.setVisible(false);
						allprojectiles.remove(i);
						bullettoborder = true;
					}
					
					for (j = 0; j < 77 && !(bullettoborder || bullettotank); j++) {
						
						if (tankdrones[j / 7][j % 7] != null) {
						
							if (allprojectiles.get(i).body.getBoundsInParent().intersects(tankdrones[j / 7][j % 7].base.getBoundsInParent()) && allprojectiles.get(i).owner != tankdrones[j / 7][j % 7].owner) {
								tankdrones[j / 7][j % 7].health -= allprojectiles.get(i).damage;
								allprojectiles.get(i).body.setVisible(false);
								
								if (tankdrones[j / 7][j % 7].health <= 0) {
									tankdrones[j / 7][j % 7].toTeam(allprojectiles.get(i).owner);
									
									if (tankdrones[j / 7][j % 7].owner != -1) {
										
										for (k = 0; k < 77; k++) {
											
											if (tankdrones[k / 7][k % 7] != null) {
												
												if (tankdrones[k / 7][k % 7].activetarget == j) {
													tankdrones[k / 7][k % 7].targeting = false;
													tankdrones[k / 7][k % 7].shooting = false;
												}
												
											}
											
										}
										
									}
									
								}
								
								allprojectiles.remove(i);
								bullettotank = true;
							}
							
						}
					
					}
					
					for (j = 0; j < 2 && !(bullettoborder || bullettotank); j++) {
						
						if (allprojectiles.get(i).body.getBoundsInParent().intersects(tankplayers[j].base.getBoundsInParent()) && allprojectiles.get(i).owner != j) {
							tankplayers[j].health -= allprojectiles.get(i).damage;
							allprojectiles.get(i).body.setVisible(false);
							allprojectiles.remove(i);
							bullettotank = true;
						}
						
					}
					
					for (j = 0; j < allprojectiles.size() && !(bullettoborder || bullettotank || bullettobullet); j++) {
						
						if (allprojectiles.get(i).body.getBoundsInParent().intersects(allprojectiles.get(j).body.getBoundsInParent()) && allprojectiles.get(j).owner != allprojectiles.get(i).owner) {
							allprojectiles.get(i).body.setVisible(false);
							allprojectiles.get(j).body.setVisible(false);
							
							if (i < j) {
								allprojectiles.remove(j);
								allprojectiles.remove(i);
							} else {
								allprojectiles.remove(i);
								allprojectiles.remove(j);
							}
							
							bullettobullet = true;
						}
						
					}
					
					bullettoborder = bullettotank = bullettobullet = false;
				}
				
			}
			
		};
		timer.start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
>>>>>>> f08a4a0bf8d6690a0d436319c7cceb646fb2dc61
}