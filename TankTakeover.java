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
import javafx.scene.shape.*;
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
	
	public void reverseDronesVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthdisplay.setVisible(tempvisible);
		sightline.setVisible(tempvisible);
		return;
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
	
	public int getTrueX() {
		return 100 + (centerx * 80);
	}
	
	public int getTrueY() {
		return 60 + (centery * 80);
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
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, maxhealth, currenthealth, bulletspeed, bulletsize, bulletpenetration, bulletrange, activetarget, lastdamaged;
	boolean underattack, shooting, targeting;
	Circle base, ownerflag;
	Line barrel;
	Rectangle healthbase, bluehealthbar, redhealthbar;
	ImageView icon;
	
	DroneTank(int clazz, int owner, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int maxhealth, int health, int bulletspeed, int bulletsize, int bulletpenetration, int bulletrange, int activetarget, int lastdamaged, boolean underattack, boolean shooting, boolean targeting, Circle base, Circle ownerflag, Line barrel, Rectangle healthbase, Rectangle bluehealthbar, Rectangle redhealthbar, ImageView icon) {
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
		this.bulletrange = bulletrange;
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
				bulletrange = 99999;
				base.setRadius(20);
				barrel.setStrokeWidth(6);
				barrel.setEndY(getTrueY() - 25);
				icon.setLayoutX(getTrueX() - 10);
				icon.setLayoutY(getTrueY() - 10);
				icon.setVisible(true);
				break;
			case 1:
				maxreload = currentreload = 180;
				damage = 15;
				maxhealth = 30;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 2;
				bulletrange = 99999;
				base.setRadius(15);
				barrel.setStrokeWidth(4);
				barrel.setEndY(getTrueY() - 30);
				icon.setLayoutX(getTrueX() - 8);
				icon.setLayoutY(getTrueY() - 8);
				icon.setVisible(true);
				break;
			case 2:
				maxreload = currentreload = 5;
				damage = 1;
				maxhealth = 100;
				bulletspeed = 4;
				bulletsize = 3;
				bulletpenetration = 1;
				bulletrange = 300;
				base.setRadius(30);
				barrel.setStrokeWidth(9);
				barrel.setEndY(getTrueY() - 31);
				icon.setLayoutX(getTrueX() - 20);
				icon.setLayoutY(getTrueY() - 20);
				icon.setVisible(true);
				break;
			case 3:
				maxreload = currentreload = 300;
				damage = 100;
				maxhealth = 150;
				bulletspeed = 2;
				bulletsize = 15; 
				bulletpenetration = 30;
				bulletrange = 99999;
				base.setRadius(35);
				barrel.setStrokeWidth(45);
				barrel.setEndY(getTrueY() - 30);
				icon.setLayoutX(getTrueX() - 25);
				icon.setLayoutY(getTrueY() - 26);
				icon.setVisible(true);
				break;
			case 4:
				maxreload = currentreload = 1;
				damage = 0;
				maxhealth = 120;
				bulletspeed = 20;
				bulletsize = 4;
				bulletpenetration = 10;
				bulletrange = 200;
				base.setRadius(25);
				barrel.setStrokeWidth(12);
				barrel.setEndY(getTrueY() - 22);
				icon.setLayoutX(getTrueX() - 11);
				icon.setLayoutY(getTrueY() - 13);
				icon.setVisible(true);
				break;
		}
		
		ownerflag.setRadius(0.8 * base.getRadius());
		return;
	}
	
	public void reverseDronesVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		ownerflag.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthbase.setVisible(tempvisible);
		bluehealthbar.setVisible(tempvisible);
		redhealthbar.setVisible(tempvisible);
		icon.setVisible(tempvisible);
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
	
	public int getTrueX() {
		return 100 + (centerx * 80);
	}
	
	public int getTrueY() {
		return 60 + (centery * 80);
	}
	
	public void updateBarrel(int barrellength) {
		barrel.setEndX(getTrueX() + barrellength * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(getTrueY() - barrellength * Math.cos(Math.toRadians(angle)));
		return;
	}
	
	public void updateHealthDisplay() {
		
		if ((currenthealth > 0 && owner == -1) || owner == 0) {
			bluehealthbar.setWidth(9 * ((double) currenthealth / maxhealth));
			bluehealthbar.setX(getTrueX() - bluehealthbar.getWidth());
			bluehealthbar.toFront();
			redhealthbar.setWidth(0);
		} else if ((currenthealth < 0 && owner == -1) || owner == 1) {
			redhealthbar.setWidth(9 * ((double) Math.abs(currenthealth) / maxhealth));
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
		return;
	}
	
	public void findNewTarget(DroneTank[][] temptankdrones, PlayerTank[] temptankplayers) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		double tempdistance = 0;
		int i1;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (i1 = 0; i1 < 77; i1++) {
			
			if (temptankdrones[i1 / 7][i1 % 7] != null && owner != temptankdrones[i1 / 7][i1 % 7].owner) {
				tempdistance = Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2);
			
				if (tempdistance < closestsquaredistance && 6400 * tempdistance < Math.pow(bulletrange, 2)) {
					closestsquaredistance = Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centerx - centerx, 2) + Math.pow((double) temptankdrones[i1 / 7][i1 % 7].centery - centery, 2);
					closesttarget = i1;
					possibletargets.clear();
					possibletargets.add(i1);
				} else if (tempdistance == closestsquaredistance && 6400 * tempdistance < Math.pow(bulletrange, 2)) {
					possibletargets.add(i1);
				}
				
			}
			
		}
		
		if (closestsquaredistance == 99999 && clazz != 2) {
			closesttarget = owner == 0 ? 73 : 3;
			activetarget = closesttarget;
			targeting = true;
		} else if (clazz != 2 || (clazz == 2 && closestsquaredistance != 99999)) {
			closesttarget = possibletargets.get(random.nextInt(possibletargets.size()));
			activetarget = closesttarget;
			targeting = true;
		}
		
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
		
		return;
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
				tempdistance = Math.sqrt(Math.pow((double) tempallprojectiles.get(i1).centerx - getTrueX(), 2) + Math.pow((double) tempallprojectiles.get(i1).centery - getTrueY(), 2));
				
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
		double timetobullet = Math.sqrt(Math.pow(tempcentery - getTrueY(), 2) + Math.pow(tempcenterx - getTrueX(), 2)) / bulletspeed;
		tempcenterx += tempspeed * timetobullet * Math.sin(Math.toRadians((double) tempangle)) * 0.9;
		tempcentery -= tempspeed * timetobullet * Math.cos(Math.toRadians((double) tempangle)) * 0.9;
		temptargetangle = Math.atan2(tempcentery - getTrueY(), tempcenterx - getTrueX()) + Math.PI / 2.0;
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
	int owner, angle, speed, size, damage, penetration, range;
	double centerx, centery, originx, originy;
	Circle body;
	
	TankProjectile(int owner, int angle, int speed, int size, int damage, int penetration, int range, double centerx, double centery, double originx, double originy, Circle body) {
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
	
	public void create(int tankowner, int tankcenterx, int tankcentery, int tankangle, int tankbulletspeed, int tankbulletsize, int tankdamage, int tankpenetration, int tankrange) {
		owner = tankowner;
		centerx = originx = tankcenterx;
		centery = originy = tankcentery;
		angle = tankangle;
		speed = tankbulletspeed;
		size = tankbulletsize;
		damage = tankdamage;
		penetration = tankpenetration;
		range = tankrange;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
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
	
	public boolean outOfRange() {
		return Math.pow(centerx - originx, 2) + Math.pow(centery - originy, 2) > Math.pow(range, 2);
	}
	
	public boolean intersectsNonGuard(Circle tempbase, int tempowner) {
		
		if (body.getBoundsInLocal().intersects(tempbase.getBoundsInLocal()) && owner != tempowner && size != 4) {
			body.setVisible(false);
			return true;
		}
		
		return false;
	}
	
}

class TankDescriptions {
	Text name, descriptionline1, descriptionline2, health, damage, firingspeed;
	
	TankDescriptions(Text name, Text descriptionline1, Text descriptionline2, Text health, Text damage, Text firingspeed) {
		this.name = name;
		this.descriptionline1 = descriptionline1;
		this.descriptionline2 = descriptionline2;
		this.health = health;
		this.damage = damage;
		this.firingspeed = firingspeed;
	}
	
	public void setDescriptionFont() {
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
		descriptionline1.setFont(Font.font("Verdana", 20));
		descriptionline2.setFont(Font.font("Verdana", 20));
		health.setFont(Font.font("Verdana", 16));
		damage.setFont(Font.font("Verdana", 16));
		firingspeed.setFont(Font.font("Verdana", 16));
		return;
	}
	
	public void reverseDescriptionsVisible(boolean tempvisible) {
		name.setVisible(tempvisible);
		descriptionline1.setVisible(tempvisible);
		descriptionline2.setVisible(tempvisible);
		health.setVisible(tempvisible);
		damage.setVisible(tempvisible);
		firingspeed.setVisible(tempvisible);
		return;
	}
	
}

public class TankTakeover extends Application {
	
	public static double getInt() {
		Scanner user = new Scanner(System.in);
		
		while (!user.hasNextInt()) {
			System.out.println("Please enter a real number");
			user.next();
		}
		
		return user.nextInt();
	}
	
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }
 
	public static int STAGE_WIDTH = 1000;
	public static int STAGE_HEIGHT = 600;
	public static int DISTANCE_FROM_BORDER_X = 100;
	public static int DISTANCE_FROM_BORDER_Y = 60;
	public static int LENGTH_OF_GRID_SQUARE = 80;
	public static int LENGTH_OF_BARREL = 25;
	
	int i, j, k, levelselection, demoangle = 0;
	int demotankselection = -1;
	boolean inthelevel, inhelp = false;
	boolean bullettoborder, bullettotank, bullettobullet = false;
	
	DroneTank tankdemos[] = new DroneTank[5];
	PlayerTank tankplayers[] = new PlayerTank[2];
	DroneTank tankdrones[][] = new DroneTank[11][7];
	TankDescriptions alldescriptions[] = new TankDescriptions[5];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
	
	String levelschematics[][] = new String[11][7];
	
	Image[] icons = new Image[5];
	Image[] levelpics = new Image[5];
	ImageView[] demopictures = new ImageView[5];
	ImageView[] levelpictures = new ImageView[5];
	
	ImageView startbutton = new ImageView();
	ImageView helpbutton = new ImageView();
	ImageView backbutton = new ImageView();
	Rectangle selectionbox = new Rectangle(STAGE_WIDTH + 10, 17, 170, 136);
	Line redcrossA = new Line(225, 438, 485, 565);
	Line redcrossB = new Line(225, 565, 485, 438);
	Text nolevelselection = new Text(250, 420, "Please select a stage!");
 
	public void start(Stage primaryStage) throws Exception {
		Image stbutton = new Image("Start_Button.png");
		Image hpbutton = new Image("Help_Button.png");
		Image bkbutton = new Image("Back_Button.png");
		
		startbutton.setImage(stbutton);
		startbutton.setLayoutX(230);
		startbutton.setLayoutY(443);
		
		helpbutton.setImage(hpbutton);
		helpbutton.setLayoutX(520);
		helpbutton.setLayoutY(443);
		
		backbutton.setImage(bkbutton);
		backbutton.setLayoutX(880);
		backbutton.setLayoutY(539);
		backbutton.setVisible(false);
		
		selectionbox.setStroke(Color.GREEN);
		selectionbox.setFill(Color.TRANSPARENT);
		selectionbox.setStrokeWidth(6);
		selectionbox.setArcWidth(20);
		selectionbox.setArcHeight(20);
		
		redcrossA.setStroke(Color.RED);
		redcrossB.setStroke(Color.RED);
		redcrossA.setStrokeWidth(5);
		redcrossB.setStrokeWidth(5);
		redcrossA.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossB.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossA.setVisible(false);
		redcrossB.setVisible(false);
		
		nolevelselection.setFont(Font.font("Verdana", 20));
		nolevelselection.setVisible(false);
		
		primaryStage.setTitle("Tank Takeover");
		
		Group root = new Group(startbutton, helpbutton, backbutton, selectionbox, redcrossA, redcrossB, nolevelselection);
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		
		for (i = 0; i < 5; i++) {
			icons[i] = new Image("Class_" + i + "_Icon.png");
			demopictures[i] = new ImageView(icons[i]);
			demopictures[i].setLayoutX(50 - (icons[i].getWidth() / 2));
			demopictures[i].setLayoutY(50 + (i * 100) + (50 - (icons[i].getHeight() / 2)));
			demopictures[i].setVisible(false);
			
			levelpics[i] = new Image("Level_" + (i + 1) + "_Picture.png");
			levelpictures[i] = new ImageView(levelpics[i]);
			levelpictures[i].setLayoutX(25 + (i * 200));
			levelpictures[i].setLayoutY(25);
			final int index = i;
			
			levelpictures[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					
					if (levelselection != index + 1) {
						levelselection = index + 1;
						selectionbox.setX(15 + (index * 200));
						redcrossA.setVisible(false);
						redcrossB.setVisible(false);
						nolevelselection.setVisible(false);
					} else {
						levelselection = 0;
						selectionbox.setX(STAGE_WIDTH + 10);
					}
					
				}
				
			});
			
			root.getChildren().addAll(demopictures[i], levelpictures[i]);
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
		
		String name = new String();
		String descriptionline1 = new String();
		String descriptionline2 = new String();
		String health = new String();
		String damage = new String();
		String firingspeed = new String();
		str = "";
		
		try (DataInputStream dataIn = new DataInputStream(new FileInputStream("DroneDescriptions.txt"))) {
			success = dataIn.read(inbuf);

			if (success != -1) {
				
				for (i = 0; i < success; i++) {
					str += (char) inbuf[i];
				}
			
			}
			//Adding to a megastring while the file has not ended
			
			String[] linedescriptions = str.split("\n");
			
			for (i = 0; i < 5; i++) {
				name = linedescriptions[i * 6].substring(9, linedescriptions[i * 6].length());
				descriptionline1 = linedescriptions[i * 6 + 1];
				descriptionline2 = linedescriptions[i * 6 + 2];
				health = linedescriptions[i * 6 + 3].substring(8, linedescriptions[i * 6 + 3].length());
				damage = linedescriptions[i * 6 + 4].substring(8, linedescriptions[i * 6 + 4].length());
				firingspeed = linedescriptions[i * 6 + 5].substring(14, linedescriptions[i * 6 + 5].length());
				
				alldescriptions[i] = new TankDescriptions(new Text(500, 50, name), new Text(160, 540, descriptionline1), new Text(160, 570, descriptionline2), new Text(900, 200, health), new Text(900, 250, damage), new Text(900, 300, firingspeed));
				alldescriptions[i].setDescriptionFont();
				alldescriptions[i].reverseDescriptionsVisible(false);
				alldescriptions[i].name.setLayoutX(-(alldescriptions[i].name.getBoundsInLocal().getWidth() / 2));
				root.getChildren().addAll(alldescriptions[i].name, alldescriptions[i].descriptionline1, alldescriptions[i].descriptionline2, alldescriptions[i].health, alldescriptions[i].damage, alldescriptions[i].firingspeed);
			}
			
		} catch (IOException exc) {
			System.out.println("Read error.");
			return;
		}
		
		for (i = 0; i < 2; i++) {
			tankplayers[i].barrel.setStrokeWidth(6);
			tankplayers[i].healthdisplay.setText(Integer.toString(tankplayers[i].currenthealth));
			tankplayers[i].reverseDronesVisible(false);
			root.getChildren().addAll(tankplayers[i].base, tankplayers[i].barrel, tankplayers[i].healthdisplay, tankplayers[i].sightline);
		}
		
		for (i = 0; i < 5; i++) {
			tankdemos[i] = new DroneTank(0, -1, 5, 3, 0, 90, 90, 5, 50, 0, 5, 3, 1, 99999, 0, 0, false, true, false, new Circle(500, 300, 20, Color.GRAY), new Circle(500, 300, 13, Color.GRAY), new Line(500, 300, 500, 275), new Rectangle(), new Rectangle(), new Rectangle(), new ImageView());
			tankdemos[i].barrel.setStroke(Color.GRAY);
			tankdemos[i].barrel.setFill(Color.GRAY);
			tankdemos[i].setDroneClass(i);
			tankdemos[i].setClassAttributes(icons);
			tankdemos[i].reverseDronesVisible(false);
			root.getChildren().addAll(tankdemos[i].base, tankdemos[i].barrel, tankdemos[i].icon);
		}
		
		startbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (levelselection != 0) {
				
					inthelevel = true;
					startbutton.setVisible(false);
					helpbutton.setVisible(false);
					selectionbox.setVisible(false);
					
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.reverseDronesVisible(true);
					}
					
					for (ImageView levelpicture : levelpictures) {
						levelpicture.setVisible(false);
					}
					
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
						
						str = str.substring(str.indexOf("Level " + Integer.toString(levelselection)) + 10, str.length()).trim();
						
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
					
					for (i = 0; i < 11; i++) {
						
						for (j = 0; j < 7; j++) {
							
							if (!(i == 0 && j == 3) && !(i == 10 && j == 3)) {
								tankdrones[i][j] = new DroneTank(0, -1, i, j, 0, 90, 90, 5, 50, 0, 5, 3, 1, 99999, 0, 0, false, false, false, new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 20, Color.GRAY), new Circle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), 13, Color.GRAY), new Line(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - LENGTH_OF_BARREL), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 10, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 3, 20, 6), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE) - 9, DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 2, 9, 4), new Rectangle(DISTANCE_FROM_BORDER_X + (i * LENGTH_OF_GRID_SQUARE), DISTANCE_FROM_BORDER_Y + (j * LENGTH_OF_GRID_SQUARE) - 2, 9, 4), new ImageView());
								
								tankdrones[i][j].barrel.setStroke(Color.GRAY);
								tankdrones[i][j].healthbase.setFill(Color.BLACK);
								tankdrones[i][j].bluehealthbar.setFill(Color.BLUE);
								tankdrones[i][j].redhealthbar.setFill(Color.RED);
								
								if (levelschematics[i][j].equals("-")) {
									tankdrones[i][j] = null;
								} else {
									tankdrones[i][j].setDroneClass(Integer.valueOf(levelschematics[i][j]));
									tankdrones[i][j].setClassAttributes(icons);
									tankdrones[i][j].reverseDronesVisible(true);
									root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthbase, tankdrones[i][j].bluehealthbar, tankdrones[i][j].redhealthbar, tankdrones[i][j].icon);
									
									if (tankdrones[i][j].clazz == 4) {
										Circle boundary = new Circle(tankdrones[i][j].getTrueX(), tankdrones[i][j].getTrueY(), 200);
										boundary.setStroke(Color.BLACK);
										boundary.setFill(Color.TRANSPARENT);
										boundary.toBack();
										root.getChildren().add(boundary);
									}
									
								}
								
							}
							
						}
						
					}
					
				} else {
					redcrossA.setVisible(true);
					redcrossB.setVisible(true);
					nolevelselection.setVisible(true);
				}
				
			}
			
		});
		
		helpbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				inhelp = true;
				startbutton.setVisible(false);
				helpbutton.setVisible(false);
				selectionbox.setVisible(false);
				redcrossA.setVisible(false);
				redcrossB.setVisible(false);
				nolevelselection.setVisible(false);
				backbutton.setVisible(true);
				
				for (i = 0; i < 5; i++) {
					levelpictures[i].setVisible(false);
					demopictures[i].setVisible(true);
					final int index = i;
					
					demopictures[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						
						public void handle(MouseEvent event) {
							demotankselection = index;
							
							for (j = 0; j < 5; j++) {
								tankdemos[j].reverseDronesVisible(j == index ? true : false);
								alldescriptions[j].reverseDescriptionsVisible(j == index ? true : false);
							}
							
						}
						
					});
					
				}
				
				backbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					
					public void handle(MouseEvent event) {
						inhelp = false;
						backbutton.setVisible(false);
						startbutton.setVisible(true);
						helpbutton.setVisible(true);
						selectionbox.setVisible(true);
						
						for (i = 0; i < 5; i++) {
							demopictures[i].setVisible(false);
							levelpictures[i].setVisible(true);
							tankdemos[i].reverseDronesVisible(false);
							alldescriptions[i].reverseDescriptionsVisible(false);
						}
						
						for (i = allprojectiles.size() - 1; i >= 0; i--) {
							root.getChildren().remove(allprojectiles.get(i).body);
						}
						
						demotankselection = -1;
						allprojectiles.clear();
						event.consume();
					}
					
				});
				
			}
			
		});
		
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
				
					//Player tank logic
					for (i = 0; i < 2; i++) {
						tankplayers[i].moveLR(); //takes keyboard inputs to turn the players
						tankplayers[i].updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayers[i].updateHealthDisplay(); //updates the numerical health display
						tankplayers[i].updateSightLine(); //updates the graphical appearance of the sight line to follow the angle
						
						/* Shooting bullets logic
						 * A method decrements a variable until it reaches 0 and checks for this
						 * Once this happens:
							* A bullet is added to the object ArrayList allprojectiles
							* This bullet is given the properties of the tank that fired it, which are:
								* angle
								* size
								* penetration
								* damage
								* origin point
								* team that it's on
							* The variable is then reset to the max reload timer
						 */
						if (tankplayers[i].fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(i, tankplayers[i].getTrueX(), tankplayers[i].getTrueY(), tankplayers[i].angle, tankplayers[i].bulletspeed, tankplayers[i].bulletsize, tankplayers[i].damage, tankplayers[i].bulletpenetration, 99999);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankplayers[i].moveToFront();
						}
						
					}
					
					//Drone tank logic
					for (i = 0; i < 77; i++) {
						
						if (tankdrones[i / 7][i % 7] != null) {
							tankdrones[i / 7][i % 7].updateHealthDisplay(); //updates the graphical health bar display
							tankdrones[i / 7][i % 7].displayHealthBar(); //displays the health bar if the tank was damaged 60 seconds ago
							
							/* Targeting enemy/neutral drone logic
							 * For the tanks that aren't guards or neutral:
								* If the tank is not currently locked onto a target and shooting:
									* If the tank hasn't found a new target, then it will run a method to determine the closest tank it should target
									* Otherwise, the tank will use the coordinates of its targeted tank (represented by the parameter "activetarget") to change its angle until it's facing its target
									* The tank will only target the enemy player once all other tanks have been converted, and special parameters associated with the coordinates of the player tanks are passed if the attacking tank is targeting the enemy player
									* After all this targeting logic, the barrel is graphically updated to follow its angle
								* If the tank is a guard and not locked onto a target and shooting:
									* If the guard hasn't found a new bullet to gun down, it will run a method to determine the closest bullet that is within the 200 pixel range and is from the enemy team
									* The logic for guards differs in that its angle used to attack an enemy bullet is updated every time it attacks
								* Finally, if the tank can release a bullet, a bullet will be added to allprojectiles that inherits the properties of:
									* angle
									* size
									* penetration
									* damage
									* origin point
									* team that it's on
							 */
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
									
									if (tankdrones[i / 7][i % 7].activetarget < allprojectiles.size()) {
										TankProjectile tempbullet = allprojectiles.get(tankdrones[i / 7][i % 7].activetarget);
										tankdrones[i / 7][i % 7].turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
										tankdrones[i / 7][i % 7].updateBarrel(tankdrones[i / 7][i % 7].getBarrelLength());
									} else {
										tankdrones[i / 7][i % 7].targeting = false;
									}
								}
								
							}
							
							if (tankdrones[i / 7][i % 7].fireBullet()) {
								allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
								DroneTank tempdrone = tankdrones[i / 7][i % 7];
								allprojectiles.get(allprojectiles.size() - 1).create(tempdrone.owner, tempdrone.getTrueX(), tempdrone.getTrueY(), tempdrone.angle, tempdrone.bulletspeed, tempdrone.bulletsize, tempdrone.damage, tempdrone.bulletpenetration, tempdrone.bulletrange);
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
								tankdrones[i / 7][i % 7].moveToFront();
								
								if (tankdrones[i / 7][i % 7].clazz == 4) {
									tankdrones[i / 7][i % 7].findNewTargetGuard(allprojectiles);
								}
								
							}
							
						}
						
					}
					
					//Collision detection logic for bullets
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
						allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
					
						/* Checking against various attributes to determine if something needs to be done about the bullet
						 * First, the bullet runs a method to determine if it's off the stage
						 * If this is true, given the stage width and height:
							* The bullet is removed from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * Then, the bullet runs a method to determine if it's touching a drone tank
						 * If this is true:
							* The drone tank is deducted health, taking into account its current state (which team it's on, if any) and the bullet damage
							* If the bullet has caused the drone to undergo a team shift, its traits are modified to take into account is new team (whether it's been converted or set back to neutral):
								* Within this method, all drones that were targeting this tank have their targeting reset
							* The bullet is removed from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * The, the bullet runs the same method, but using the player tank traits, to determine if it's touched an enemy player
						 * If this is true:
							* The player is deducted health according to the damage of the bullet
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * Finally, the bullet is checked if it is intersecting another bullet of the other team
						 * If this is true:
							* Some logic is run to remove both bullets from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * In every case, the three variables bullettoborder, bullettotank, and bullettobullet are used to break out of each for loop early, so they are reset to false for the next bullet's hitbox detection
						 */
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.get(i).owner = -2;
							
							for (j = 0; j < 77; j++) {
								
								if (tankdrones[j / 7][j % 7] != null && tankdrones[j / 7][j % 7].clazz == 4) {
									tankdrones[j / 7][j % 7].resetTargeting();
								}
								
							}
							
						}
						
						if (allprojectiles.get(i).outOfRange() && allprojectiles.get(i).owner != 4) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.get(i).owner = -2;
						}
						
						for (j = 0; j < 77 && allprojectiles.get(i).owner != -2; j++) {
							
							if (tankdrones[j / 7][j % 7] != null && allprojectiles.get(i).intersectsNonGuard(tankdrones[j / 7][j % 7].base, tankdrones[j / 7][j % 7].owner)) {
								tankdrones[j / 7][j % 7].removeHealth(allprojectiles.get(i).damage, allprojectiles.get(i).owner);
								tankdrones[j / 7][j % 7].healthBelowZeroActivites(allprojectiles.get(i).owner, tankdrones, j);
								root.getChildren().remove(allprojectiles.get(i).body);
								allprojectiles.get(i).owner = -2;
								
								for (k = 0; k < 77; k++) {
									
									if (tankdrones[k / 7][k % 7] != null && tankdrones[k / 7][k % 7].clazz == 4) {
										tankdrones[k / 7][k % 7].resetTargeting();
									}
									
								}
								
							}
							
						}
						
						for (j = 0; j < 2 && allprojectiles.get(i).owner != -2; j++) {
							
							if (allprojectiles.get(i).intersectsNonGuard(tankplayers[j].base, j)) {
								tankplayers[j].currenthealth -= allprojectiles.get(i).damage;
								root.getChildren().remove(allprojectiles.get(i).body);
								allprojectiles.get(i).owner = -2;	
							}
							
						}
						
						for (j = allprojectiles.size() - 1; j >= 0 && allprojectiles.get(i).owner != -2; j--) {
							
							if (allprojectiles.get(i).body.getBoundsInLocal().intersects(allprojectiles.get(j).body.getBoundsInLocal()) && allprojectiles.get(j).owner != allprojectiles.get(i).owner && allprojectiles.get(j).owner != -2) {
								int temppenetration = allprojectiles.get(j).penetration;
								allprojectiles.get(j).penetration -= allprojectiles.get(i).penetration;
								allprojectiles.get(i).penetration -= temppenetration;
								
								if (allprojectiles.get(i).penetration <= 0) {
									allprojectiles.get(i).body.setVisible(false);
									root.getChildren().remove(allprojectiles.get(i).body);
									allprojectiles.get(i).owner = -2;
								}
								
								if (allprojectiles.get(j).penetration <= 0) {
									allprojectiles.get(j).body.setVisible(false);
									root.getChildren().remove(allprojectiles.get(j).body);
									allprojectiles.get(j).owner = -2;
								}
								
							}
							
						}
						
						for (j = allprojectiles.size() - 1; j >= 0; j--) {
							
							if (allprojectiles.get(j).owner == -2) {
								allprojectiles.remove(j);
							}
							
						}
						
					}
					
				} else if (inhelp) {
					demoangle = (demoangle + 1) % 360;
					
					for (i = 0; i < 5; i++) {
						tankdemos[i].angle = demoangle;
						tankdemos[i].updateBarrel(tankdemos[i].getBarrelLength());
						
						if (tankdemos[i].fireBullet() && i == demotankselection) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							DroneTank tempdrone = tankdemos[i];
							allprojectiles.get(allprojectiles.size() - 1).create(tempdrone.owner, tempdrone.getTrueX(), tempdrone.getTrueY(), tempdrone.angle, tempdrone.bulletspeed, tempdrone.bulletsize, tempdrone.damage, tempdrone.bulletpenetration, tempdrone.bulletrange);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankdemos[i].moveToFront();
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
						allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
							bullettoborder = true;
						}
					
					}
					
				}
				
			}
			
		};
		timer.start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
}