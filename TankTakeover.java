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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
//Importing my stuff

class PlayerTank {
	int clazz, centerx, centery, angle, maxreload, currentreload, damage, currenthealth, bulletspeed, bulletsize, bulletpenetration, swiveltime, lastpressed;
	boolean turnleft, turnright, shooting;
	Circle base;
	Line barrel, sightline;
	Text healthdisplay;
	
	PlayerTank(int clazz, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int currenthealth, int bulletspeed, int bulletsize, int bulletpenetration, int swiveltime, int lastpressed, boolean turnleft, boolean turnright, boolean shooting, Circle base, Line barrel, Line sightline, Text healthdisplay) {
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
		this.swiveltime = swiveltime;
		this.turnleft = turnleft;
		this.turnright = turnright;
		this.shooting = shooting;
		this.base = base;
		this.barrel = barrel;
		this.sightline = sightline;
		this.healthdisplay = healthdisplay;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		healthdisplay.toFront();
		return;
	}
	
	public void moveLR() {
		
		if (!(turnright || turnleft) && swiveltime < 12 && swiveltime != 0) {
			angle += lastpressed == 1 ? 1 : -1;
		} 
		
		if (turnright || turnleft) {
			swiveltime++;
		} else if (swiveltime != 0) {
			swiveltime = 0;
		}
		
		int turnangle = swiveltime < 60 ? swiveltime / 12 : 5;
		angle += turnright ? turnangle : (turnleft ? -turnangle : 0);
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
	
	public void updateSightLine() {
		sightline.setEndX(100 + (80 * centerx) + 1200 * Math.sin(Math.toRadians(angle)));
		sightline.setEndY(60 + (80 * centery) - 1200 * Math.cos(Math.toRadians(angle)));
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
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, maxhealth, currenthealth, bulletspeed, bulletsize, bulletpenetration, activetarget, lastdamaged;
	boolean underattack, shooting, targeting;
	Circle base, ownerflag;
	Line barrel;
	Rectangle healthbase, bluehealthbar, redhealthbar;
	ImageView icon;
	
	DroneTank(int clazz, int owner, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int maxhealth, int health, int bulletspeed, int bulletsize, int bulletpenetration, int activetarget, int lastdamaged, boolean underattack, boolean shooting, boolean targeting, Circle base, Circle ownerflag, Line barrel, Rectangle healthbase, Rectangle bluehealthbar, Rectangle redhealthbar, ImageView icon) {
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
		this.lastdamaged = lastdamaged;
		this.underattack = underattack;
		this.shooting = shooting;
		this.targeting = targeting;
		this.base = base;
		this.ownerflag = ownerflag;
		this.barrel = barrel;
		this.healthbase = healthbase;
		this.bluehealthbar = bluehealthbar;
		this.redhealthbar = redhealthbar;
		this.icon = icon;
	}
	
	public void setDroneClass(int droneclass) {
		clazz = droneclass;
		return;
	}
	
	public void setClassAttributes(Image[] tempicons) {
		icon.setImage(tempicons[clazz]);
		
		switch (clazz) {
			case 0:
				maxreload = currentreload = 90;
				damage = 5;
				maxhealth = 50;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(20);
				barrel.setStrokeWidth(6);
				barrel.setEndY(60 + (80 * centery) - 25);
				icon.setLayoutX(100 + (80 * centerx) - 10);
				icon.setLayoutY(60 + (80 * centery) - 10);
				icon.setVisible(true);
				break;
			case 1:
				maxreload = currentreload = 180;
				damage = 15;
				maxhealth = 30;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 2;
				base.setRadius(15);
				barrel.setStrokeWidth(4);
				barrel.setEndY(60 + (80 * centery) - 30);
				icon.setLayoutX(100 + (80 * centerx) - 8);
				icon.setLayoutY(60 + (80 * centery) - 8);
				icon.setVisible(true);
				break;
			case 2:
				maxreload = currentreload = 5;
				damage = 1;
				maxhealth = 100;
				bulletspeed = 4;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(30);
				barrel.setStrokeWidth(9);
				barrel.setEndY(60 + (80 * centery) - 31);
				icon.setLayoutX(100 + (80 * centerx) - 20);
				icon.setLayoutY(60 + (80 * centery) - 20);
				icon.setVisible(true);
				break;
			case 3:
				maxreload = currentreload = 300;
				damage = 100;
				maxhealth = 150;
				bulletspeed = 1;
				bulletsize = 20; 
				bulletpenetration = 30;
				base.setRadius(35);
				barrel.setStrokeWidth(45);
				barrel.setEndY(60 + (80 * centery) - 30);
				icon.setLayoutX(100 + (80 * centerx) - 25);
				icon.setLayoutY(60 + (80 * centery) - 26);
				icon.setVisible(true);
				break;
			case 4:
				maxreload = currentreload = 3;
				damage = 0;
				maxhealth = 200;
				bulletspeed = 15;
				bulletsize = 4;
				bulletpenetration = 10;
				base.setRadius(25);
				barrel.setStrokeWidth(12);
				barrel.setEndY(60 + (80 * centery) - 22);
				icon.setLayoutX(100 + (80 * centerx) - 11);
				icon.setLayoutY(60 + (80 * centery) - 13);
				icon.setVisible(true);
				break;
		}
		
		ownerflag.setRadius(0.8 * base.getRadius());
		return;
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		ownerflag.toFront();
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
		return;
	}
	
	public int getBarrelLength() {
		int barrellength = 25;
		
		switch (clazz) {
			case 0: barrellength = 25; break;
			case 1: barrellength = 30; break;
			case 2: barrellength = 31; break;
			case 3: barrellength = 30; break;
			case 4: barrellength = 22; break;
		}
		
		return barrellength;
	}
	
	public int getTrueX(int tempdistancefromborderx, int templengthofgridsquare) {
		return tempdistancefromborderx + (centerx * templengthofgridsquare);
	}
	
	public int getTrueY(int tempdistancefrombordery, int templengthofgridsquare) {
		return tempdistancefrombordery + (centery * templengthofgridsquare);
	}
	
	public void updateBarrel(int barrellength) {
		barrel.setEndX(100 + (80 * centerx) + barrellength * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(60 + (80 * centery) - barrellength * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		
		if ((currenthealth > 0 && owner == -1) || owner == 0) {
			bluehealthbar.setWidth(Math.round(9 * ((double) currenthealth / maxhealth)));
			bluehealthbar.setX(100 + (80 * centerx) - bluehealthbar.getWidth());
			bluehealthbar.toFront();
			redhealthbar.setWidth(0);
		} else if ((currenthealth < 0 && owner == -1) || owner == 1) {
			redhealthbar.setWidth(Math.round(9 * ((double) Math.abs(currenthealth) / maxhealth)));
			redhealthbar.toFront();
			bluehealthbar.setWidth(0);
		}
		
		return;
	}
	
	public void toTeam(int newowner) {
		owner = newowner;
		ownerflag.setFill(newowner == 0 ? Color.BLUE : Color.RED);
		ownerflag.toFront();
		currenthealth = maxhealth;
		targeting = shooting = false;
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
		return;
	}
	
	public void toNeutral(int oldowner) {
		owner = -1;
		ownerflag.setFill(Color.GRAY);
		ownerflag.toFront();
		currenthealth *= oldowner == 0 ? 1 : -1;
		targeting = shooting = false;
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
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
	
	public void findNewTargetGuard(ArrayList<TankProjectile> tempallprojectiles) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		double tempdistance = 0;
		int i1;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (i1 = 0; i1 < tempallprojectiles.size(); i1++) {
			
			if (owner != tempallprojectiles.get(i1).owner && tempallprojectiles.get(i1).size != 4) {
				tempdistance = Math.sqrt(Math.pow((double) tempallprojectiles.get(i1).centerx - (100 + (80 * centerx)), 2) + Math.pow((double) tempallprojectiles.get(i1).centery - (60 + (80 * centery)), 2));
				
				if (tempdistance < closestsquaredistance && tempdistance < 200) {
					closestsquaredistance = tempdistance;
					closesttarget = i1;
					possibletargets.clear();
					possibletargets.add(i1);
				} else if (tempdistance == closestsquaredistance && owner != tempallprojectiles.get(i1).owner) {
					possibletargets.add(i1);
				}
				
			}
			
		}
		
		if (possibletargets.size() != 0) {
			closesttarget = possibletargets.get(random.nextInt(possibletargets.size()));
			activetarget = closesttarget;
			targeting = true;
		}
		
		return;
	}
	
	public void turnToTargetGuard(double tempcenterx, double tempcentery, int tempspeed, int tempangle) {
		int targetangle;
		double temptargetangle;
		double timetobullet = Math.sqrt(Math.pow(tempcentery - (60 + (centery * 80)), 2) + Math.pow(tempcenterx - (100 + (centerx * 80)), 2)) / bulletspeed;
		tempcenterx += tempspeed * timetobullet * Math.sin(Math.toRadians((double) tempangle)) * 0.9;
		tempcentery -= tempspeed * timetobullet * Math.cos(Math.toRadians((double) tempangle)) * 0.9;
		temptargetangle = Math.atan2(tempcentery - (60 + (centery * 80)), tempcenterx - (100 + (centerx * 80))) + Math.PI / 2.0;
		targetangle = (int) Math.round(Math.toDegrees(temptargetangle));
		targetangle = Math.floorMod(targetangle, 360);
		angle = targetangle;
		shooting = true;
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
	
	public void displayHealthBar() {
		lastdamaged++;
		
		if (lastdamaged > 60) {
			healthbase.setVisible(false);
			bluehealthbar.setVisible(false);
			redhealthbar.setVisible(false);
		} else {
			healthbase.setVisible(true);
			bluehealthbar.setVisible(true);
			redhealthbar.setVisible(true);
		}
		
		return;
	}
	
	public void removeHealth(int tempdamage, int tempowner) {
		lastdamaged = 0;
		
		if (owner == -1) {
			currenthealth -= (tempdamage * (tempowner == 0 ? -1 : 1));
		} else {
			currenthealth -= tempdamage;
		}
		
		return;
	}
	
	public void resetTargeting() {
		targeting = false;
		shooting = false;
		return;
	}
	
	public void healthBelowZeroActivites(int tempowner, DroneTank[][] temptankdrones, int j1) {
		
		if (Math.abs(currenthealth) >= maxhealth && owner == -1) {
			toTeam(tempowner);
			
			for (int i1 = 0; i1 < 77; i1++) {
				
				if (temptankdrones[i1 / 7][i1 % 7] != null && temptankdrones[i1 / 7][i1 % 7].activetarget == j1) {
					temptankdrones[i1 / 7][i1 % 7].resetTargeting();
				}
				
			}
			
		} else if (currenthealth <= 0 && owner != -1) {
			toNeutral(owner);
		}
		
		return;
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
	
	public boolean offStage(int tempstagewidth, int tempstageheight) {
		
		if (centerx < 0 || centerx > tempstagewidth || centery < 0 || centery > tempstageheight) {
			body.setVisible(false);
			return true;
		}
		
		return false;
	}
	
	public boolean intersectsNonGuard(Circle tempbase, int tempowner) {
		
		if (body.getBoundsInParent().intersects(tempbase.getBoundsInParent()) && owner != tempowner && size != 4) {
			body.setVisible(false);
			return true;
		}
		
		return false;
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
	String levelschematics[][] = new String[11][7];
	Image[] icons = new Image[5];
 
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Tank Takeover");
		
		Group root = new Group();
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		
		for (i = 0; i <= 4; i++) {
			icons[i] = new Image("Class_" + i + "_Icon.png");
		}
		
		tankplayers[0] = new PlayerTank(0, 0, 3, 0, 30, 30, 500, 100, 5, 3, 1, 0, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.BLUE), new Line(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Line(DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - 1200), new Text(DISTANCE_FROM_BORDER_X - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		tankplayers[1] = new PlayerTank(0, 10, 3, 0, 30, 30, 500, 100, 5, 3, 1, 0, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), 20, Color.RED), new Line(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Line(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) - 1200), new Text(DISTANCE_FROM_BORDER_X + (10 * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (3 * LENGTH_OF_GRID_SQUARE) + 4, ""));
		
		tankplayers[0].barrel.setStroke(Color.BLUE);
		tankplayers[0].sightline.setStroke(Color.BLUE);
		tankplayers[1].barrel.setStroke(Color.RED);
		tankplayers[1].sightline.setStroke(Color.RED);
		
		int success;
		byte inbuf[] = new byte[20500];
		String str = new String();
		
		try (DataInputStream dataIn = new DataInputStream(new FileInputStream("StageDronePlacement.txt"))) {
			success = dataIn.read(inbuf);

			if (success != -1) {
				
				for (i = 0; i < success; i++) {
					str += (char) inbuf[i];
				}
			
			}
			//Adding to a megastring while the file has not ended
			
			str = str.substring(str.indexOf("C") + 1, str.indexOf("D") - 1).trim();
			
			for (i = 0; i < 7; i++) {
				
				for (j = 0; j < 11; j++) {
					levelschematics[j][i] = str.substring(0, 1);
					str = str.substring(1, str.length()).trim();
				}
				
			} //while
			
		} catch (IOException exc) {
			System.out.println("Read error.");
			return;
		}
		
		for (i = 0; i < 2; i++) {
			tankplayers[i].barrel.setStrokeWidth(6);
			tankplayers[i].healthdisplay.setText(Integer.toString(tankplayers[i].currenthealth));
			root.getChildren().addAll(tankplayers[i].base, tankplayers[i].barrel, tankplayers[i].healthdisplay, tankplayers[i].sightline);
		}
		
		for (i = 0; i < 11; i++) {
			
			for (j = 0; j < 7; j++) {
				
				if (!(i == 0 && j == 3) && !(i == 10 && j == 3)) {
					tankdrones[i][j] = new DroneTank(0, -1, i, j, 0, 90, 90, 5, 50, 0, 5, 3, 1, 0, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 20, Color.GRAY), new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 13, Color.GRAY), new Line(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 3, 20, 6), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 9, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 2, 9, 4), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 2, 9, 4), new ImageView());
					
					tankdrones[i][j].barrel.setStroke(Color.GRAY);
					tankdrones[i][j].healthbase.setFill(Color.BLACK);
					tankdrones[i][j].bluehealthbar.setFill(Color.BLUE);
					tankdrones[i][j].redhealthbar.setFill(Color.RED);
					
					if (levelschematics[i][j].equals("-")) {
						tankdrones[i][j] = null;
					} else {
						tankdrones[i][j].setDroneClass(Integer.valueOf(levelschematics[i][j]));
						tankdrones[i][j].setClassAttributes(icons);
						
						if (tankdrones[i][j].clazz == 4) {
							Circle boundary = new Circle(100 + 80 * tankdrones[i][j].centerx, 60 + 80 * tankdrones[i][j].centery, 200);
							boundary.setStroke(Color.BLACK);
							boundary.setFill(Color.TRANSPARENT);
							root.getChildren().add(boundary);
						}
						
						root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthbase, tankdrones[i][j].bluehealthbar, tankdrones[i][j].redhealthbar, tankdrones[i][j].icon);
					}
					
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
							tankplayers[0].lastpressed = -1;
							break;
						case D:
							tankplayers[0].turnright = true;
							tankplayers[0].lastpressed = 1;
							break;
						case W:
							tankplayers[0].shooting = true;
							break;
						case J:
							tankplayers[1].turnleft = true;
							tankplayers[1].lastpressed = -1;
							break;
						case L:
							tankplayers[1].turnright = true;
							tankplayers[1].lastpressed = 1;
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
				
				if (inthelevel) {
				
					for (i = 0; i < 2; i++) {
						tankplayers[i].moveLR();
						tankplayers[i].updateBarrel();
						tankplayers[i].updateHealthDisplay();
						tankplayers[i].updateSightLine();
						
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
							tankdrones[i / 7][i % 7].displayHealthBar();
							
							if (tankdrones[i / 7][i % 7].clazz != 4) {
							
								if (!tankdrones[i / 7][i % 7].shooting && tankdrones[i / 7][i % 7].owner != -1) {
									
									if (!tankdrones[i / 7][i % 7].targeting) {
										tankdrones[i / 7][i % 7].findNewTarget(tankdrones, tankplayers);
									} else {
										
										if (tankdrones[i / 7][i % 7].activetarget != 3 && tankdrones[i / 7][i % 7].activetarget != 73) {
											DroneTank tempdrone = tankdrones[tankdrones[i / 7][i % 7].activetarget / 7][tankdrones[i / 7][i % 7].activetarget % 7];
											tankdrones[i / 7][i % 7].turnToTarget(tempdrone.centerx, tempdrone.centery);
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
								
							} else if (tankdrones[i / 7][i % 7].owner != -1) {
								
								if (!tankdrones[i / 7][i % 7].targeting) {
									tankdrones[i / 7][i % 7].findNewTargetGuard(allprojectiles);
								}
								
								if (tankdrones[i / 7][i % 7].targeting) {
									TankProjectile tempbullet = allprojectiles.get(tankdrones[i / 7][i % 7].activetarget);
									tankdrones[i / 7][i % 7].turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
									tankdrones[i / 7][i % 7].updateBarrel(tankdrones[i / 7][i % 7].getBarrelLength());
								}
								
							}
							
							if (tankdrones[i / 7][i % 7].fireBullet()) {
								allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
								DroneTank tempdrone = tankdrones[i / 7][i % 7];
								allprojectiles.get(allprojectiles.size() - 1).create(tempdrone.owner, tempdrone.getTrueX(DISTANCE_FROM_BORDER_X, LENGTH_OF_GRID_SQUARE), tempdrone.getTrueY(DISTANCE_FROM_BORDER_Y, LENGTH_OF_GRID_SQUARE), tempdrone.angle, tempdrone.bulletspeed, tempdrone.bulletsize, tempdrone.damage, tempdrone.bulletpenetration);
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
								
								tankdrones[i / 7][i % 7].moveToFront();
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						allprojectiles.get(i).updatePosition();
						allprojectiles.get(i).body.toBack();
						
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							allprojectiles.remove(i);
							
							for (j = 0; j < 77; j++) {
								
								if (tankdrones[j / 7][j % 7] != null && tankdrones[j / 7][j % 7].clazz == 4) {
									tankdrones[j / 7][j % 7].resetTargeting();
								}
								
							}
							
							bullettoborder = true;
						}
						
						for (j = 0; j < 77 && !(bullettoborder || bullettotank); j++) {
							
							if (tankdrones[j / 7][j % 7] != null) {
							
								if (allprojectiles.get(i).intersectsNonGuard(tankdrones[j / 7][j % 7].base, tankdrones[j / 7][j % 7].owner)) {
									tankdrones[j / 7][j % 7].removeHealth(allprojectiles.get(i).damage, allprojectiles.get(i).owner);
									tankdrones[j / 7][j % 7].healthBelowZeroActivites(allprojectiles.get(i).owner, tankdrones, j);
									allprojectiles.remove(i);
									
									for (j = 0; j < 77; j++) {
										
										if (tankdrones[j / 7][j % 7] != null && tankdrones[j / 7][j % 7].clazz == 4) {
											tankdrones[j / 7][j % 7].resetTargeting();
										}
										
									}
									
									bullettotank = true;
								}
								
							}
						
						}
						
						for (j = 0; j < 2 && !(bullettoborder || bullettotank); j++) {
							
							if (allprojectiles.get(i).intersectsNonGuard(tankplayers[j].base, j)) {
								tankplayers[j].currenthealth -= allprojectiles.get(i).damage;
								allprojectiles.remove(i);
								
								for (j = 0; j < 77; j++) {
									
									if (tankdrones[j / 7][j % 7] != null && tankdrones[j / 7][j % 7].clazz == 4) {
										tankdrones[j / 7][j % 7].findNewTargetGuard(allprojectiles);
									}
									
								}
								
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
								
								for (j = 0; j < 77; j++) {
									
									if (tankdrones[j / 7][j % 7] != null && tankdrones[j / 7][j % 7].clazz == 4) {
										tankdrones[j / 7][j % 7].findNewTargetGuard(allprojectiles);
									}
									
								}
								
								bullettobullet = true;
							}
							
						}
						
						bullettoborder = bullettotank = bullettobullet = false;
					}
					
				}
				
			}
			
		};
		timer.start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
}