import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class DroneTank extends Tank {
	int activetarget, lastdamaged, targetangle;
	boolean targeting;
	Circle ownerflag, rangeboundary;
	Rectangle healthbase, bluehealthbar, redhealthbar;
	ImageView icon;
	
	DroneTank() {
		super();
		
		this.activetarget = 0;
		this.lastdamaged = 0;
		this.targetangle = 0;
		this.targeting = false;
		this.ownerflag = new Circle();
		this.rangeboundary = new Circle();
		this.healthbase = new Rectangle();
		this.bluehealthbar = new Rectangle();
		this.redhealthbar = new Rectangle();
		this.icon = new ImageView();
	}
	
	public void create(int tempx, int tempy) {
		owner = -1;
		centerx = tempx;
		centery = tempy;
		base.setCenterX(getTrueX());
		base.setCenterY(getTrueY());
		base.setFill(Color.GRAY);
		ownerflag.setCenterX(getTrueX());
		ownerflag.setCenterY(getTrueY());
		ownerflag.setFill(Color.GRAY);
		rangeboundary.setCenterX(getTrueX());
		rangeboundary.setCenterY(getTrueY());
		rangeboundary.setStroke(Color.BLACK);
		rangeboundary.setFill(Color.TRANSPARENT);
		barrel.setStartX(getTrueX());
		barrel.setStartY(getTrueY());
		barrel.setEndX(getTrueX());
		barrel.setStroke(Color.GRAY);
		healthbase.setX(getTrueX() - 10);
		healthbase.setY(getTrueY() - 3);
		healthbase.setWidth(20);
		healthbase.setHeight(6);
		bluehealthbar.setX(getTrueX() - 9);
		bluehealthbar.setY(getTrueY() - 2);
		bluehealthbar.setWidth(18);
		bluehealthbar.setHeight(4);
		bluehealthbar.setFill(Color.BLUE);
		redhealthbar.setX(getTrueX() - 9);
		redhealthbar.setY(getTrueY() - 2);
		redhealthbar.setWidth(18);
		redhealthbar.setHeight(4);
		redhealthbar.setFill(Color.RED);
	}
	
	public void setDroneClass(int droneclass) {
		clazz = droneclass;
	}
	
	public void setClassAttributes(Image tempicon) {
		
		switch (clazz) {
			case 0:
				maxreload = currentreload = 90;
				damage = 5;
				maxhealth = 50;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				bulletrange = 99999;
				barrellength = 25;
				base.setRadius(20);
				barrel.setStrokeWidth(6);
				icon.setLayoutX(getTrueX() - 10);
				icon.setLayoutY(getTrueY() - 10);
				break;
			case 1:
				maxreload = currentreload = 180;
				damage = 15;
				maxhealth = 30;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 2;
				bulletrange = 99999;
				barrellength = 30;
				base.setRadius(15);
				barrel.setStrokeWidth(4);
				icon.setLayoutX(getTrueX() - 8);
				icon.setLayoutY(getTrueY() - 8);
				break;
			case 2:
				maxreload = currentreload = 5;
				damage = 1;
				maxhealth = 100;
				bulletspeed = 4;
				bulletsize = 3;
				bulletpenetration = 1;
				bulletrange = 300;
				barrellength = 31;
				base.setRadius(30);
				barrel.setStrokeWidth(9);
				icon.setLayoutX(getTrueX() - 20);
				icon.setLayoutY(getTrueY() - 20);
				break;
			case 3:
				maxreload = currentreload = 300;
				damage = 100;
				maxhealth = 150;
				bulletspeed = 2;
				bulletsize = 20; 
				bulletpenetration = 100;
				bulletrange = 99999;
				barrellength = 30;
				base.setRadius(35);
				barrel.setStrokeWidth(45);
				icon.setLayoutX(getTrueX() - 25);
				icon.setLayoutY(getTrueY() - 26);
				break;
			case 4:
				maxreload = currentreload = 0;
				damage = 0;
				maxhealth = 120;
				bulletspeed = 18;
				bulletsize = 4;
				bulletpenetration = 10;
				bulletrange = 200;
				barrellength = 22;
				base.setRadius(25);
				barrel.setStrokeWidth(12);
				icon.setLayoutX(getTrueX() - 11);
				icon.setLayoutY(getTrueY() - 13);
				break;
		}
		
		icon.setImage(tempicon);
		ownerflag.setRadius(0.8 * base.getRadius());
		barrel.setEndY(getTrueY() - barrellength);
	}
	
	public void setDroneVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		ownerflag.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthbase.setVisible(tempvisible);
		bluehealthbar.setVisible(tempvisible);
		redhealthbar.setVisible(tempvisible);
		icon.setVisible(tempvisible);
		rangeboundary.setVisible(tempvisible);
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		ownerflag.toFront();
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
	}
	
	public void updateHealthDisplay() {
		
		if ((currenthealth > 0 && owner == -1) || owner == 0) {
			bluehealthbar.setWidth(18 * ((double) currenthealth / maxhealth));
			bluehealthbar.setVisible(true);
			redhealthbar.setVisible(false);
		} else if ((currenthealth < 0 && owner == -1) || owner == 1) {
			redhealthbar.setWidth(18 * ((double) Math.abs(currenthealth) / maxhealth));
			redhealthbar.setX(getTrueX() + 9 - redhealthbar.getWidth());
			redhealthbar.setVisible(true);
			bluehealthbar.setVisible(false);
		} else if (currenthealth == 0) {
			bluehealthbar.setVisible(false);
			redhealthbar.setVisible(false);
		}
		
	}
	
	public void toTeam(int newowner) {
		owner = newowner;
		targeting = shooting = false;
		ownerflag.setFill(newowner == 0 ? Color.BLUE : Color.RED);
		ownerflag.toFront();
		currenthealth = maxhealth;
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
	}
	
	public void toNeutral(int tempowner) {
		owner = -1;
		resetTargeting();
		ownerflag.setFill(Color.GRAY);
		ownerflag.toFront();
		
		if (tempowner == -1)
			currenthealth = 0;
		else
			currenthealth *= owner == 0 ? 1 : -1;
		
		icon.toFront();
		updateHealthDisplay();
	}
	
	public void findNewTarget(DroneTank[][] temptankdrones, PlayerTank[] temptankplayers) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (DroneTank[] temptankdronerow : temptankdrones)
			for (DroneTank temptankdrone : temptankdronerow)
				
				if (temptankdrone != null && owner != temptankdrone.owner) {
					double tempdistance = Math.pow((double) temptankdrone.centerx - centerx, 2) + Math.pow((double) temptankdrone.centery - centery, 2);
					int tempindex = temptankdrone.centerx * 7 + temptankdrone.centery;
					
					if (Math.pow(Constants.LENGTH_OF_GRID_SQUARE, 2) * tempdistance < Math.pow(bulletrange, 2)) {
						
						if (tempdistance < closestsquaredistance) {
							closestsquaredistance = Math.pow((double) temptankdrone.centerx - centerx, 2) + Math.pow((double) temptankdrone.centery - centery, 2);
							closesttarget = tempindex;
							possibletargets.clear();
							possibletargets.add(tempindex);
						} else if (tempdistance == closestsquaredistance) {
							possibletargets.add(tempindex);
						}
						
					}
					
				}
		
		if (closestsquaredistance == 99999 && clazz != 2) {
			int temptarget = owner == 0 ? 1 : 0;
			activetarget = temptankplayers[temptarget].getIndex();
			targeting = true;
			findTargetAngle(temptankplayers[temptarget]);
		} else if (clazz != 2 || (clazz == 2 && closestsquaredistance != 99999)) {
			activetarget = possibletargets.get(random.nextInt(possibletargets.size()));
			targeting = true;
			findTargetAngle(temptankdrones[activetarget / 7][activetarget % 7]);
		}
		
	}
	
	public void findTargetAngle(Tank temptank) {
		double temptargetangle;
		temptargetangle = Math.atan2(temptank.centery - centery, temptank.centerx - centerx) + Math.PI / 2.0;
		targetangle = Math.floorMod((int) Math.round(Math.toDegrees(temptargetangle)), 360);
	}
	
	public int findMouseTargetAngle(double mouseposx, double mouseposy) {
		double temptargetangle;
		temptargetangle = Math.atan2(mouseposy - getTrueY(), mouseposx - getTrueX()) + Math.PI / 2.0;
		int targetangle = Math.floorMod((int) Math.round(Math.toDegrees(temptargetangle)), 360);
		
		return targetangle;
	}
	
	public void turnToTarget() {
		
		if (Math.abs(Math.floorMod(targetangle - angle, 360)) <= bulletspeed) {
			angle = targetangle;
			shooting = true;
		} else {
			shooting = false;
		}
		
		if (Math.abs(targetangle - angle) > 180) {
			angle -= angle < targetangle ? bulletspeed : (angle > targetangle ? -bulletspeed : 0);
		} else {
			angle += angle < targetangle ? bulletspeed : (angle > targetangle ? -bulletspeed : 0);
		}
		
		angle = Math.floorMod(angle, 360);
		
		if (Math.abs(Math.floorMod(targetangle - angle, 360)) <= bulletspeed) {
			angle = targetangle;
			shooting = true;
		} else {
			shooting = false;
		}
		
	}
	
	public void findNewTargetGuard(ArrayList<TankProjectile> allprojectiles) {
		int closesttarget = 0;
		double closestsquaredistance = Double.MAX_VALUE;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (int i1 = 0; i1 < allprojectiles.size(); i1++)
			
			if (owner != allprojectiles.get(i1).owner && allprojectiles.get(i1).size != 4) {
				double tempdistance = Math.pow(allprojectiles.get(i1).centerx - getTrueX(), 2) + Math.pow(allprojectiles.get(i1).centery - getTrueY(), 2);
				
				if (tempdistance < closestsquaredistance && tempdistance < Math.pow(bulletrange, 2)) {
					closestsquaredistance = tempdistance;
					closesttarget = i1;
					possibletargets.clear();
					possibletargets.add(i1);
				} else if (tempdistance == closestsquaredistance && tempdistance < Math.pow(bulletrange, 2)) {
					possibletargets.add(i1);
				}
				
			}
		
		
		if (possibletargets.size() != 0) {
			closesttarget = possibletargets.get(random.nextInt(possibletargets.size()));
			activetarget = closesttarget;
			targeting = true;
		} else {
			resetTargeting();
		}
		
	}
	
	public void turnToTargetGuard(double tempcenterx, double tempcentery, double tempspeed, int tempangle) {
		int targetangle;
		double temptargetangle;
		double timetobullet = Math.sqrt(Math.pow(tempcentery - getTrueY(), 2) + Math.pow(tempcenterx - getTrueX(), 2)) / bulletspeed;
		tempcenterx += tempspeed * timetobullet * Math.sin(Math.toRadians((double) tempangle)) * 0.9;
		tempcentery -= tempspeed * timetobullet * Math.cos(Math.toRadians((double) tempangle)) * 0.9;
		temptargetangle = Math.atan2(tempcentery - getTrueY(), tempcenterx - getTrueX()) + Math.PI / 2.0;
		targetangle = (int) Math.round(Math.toDegrees(temptargetangle));
		targetangle = Math.floorMod(targetangle, 360);
		angle = Math.floorMod(targetangle, 360);
		shooting = true;
	}
	
	public void displayHealthBar() {
		lastdamaged++;
		double tempvisible = 1.5 - (double) lastdamaged / 60;
		tempvisible = tempvisible < 0 ? 0 : tempvisible;
		tempvisible = tempvisible > 1 ? 1 : tempvisible;
		healthbase.setStroke(Color.rgb(0, 0, 0, tempvisible));
		healthbase.setFill(Color.rgb(0, 0, 0, tempvisible));
		bluehealthbar.setStroke(Color.rgb(0, 0, 255, tempvisible));
		bluehealthbar.setFill(Color.rgb(0, 0, 255, tempvisible));
		redhealthbar.setStroke(Color.rgb(255, 0, 0, tempvisible));
		redhealthbar.setFill(Color.rgb(255, 0, 0, tempvisible));
	}
	
	public void removeHealth(TankProjectile bullet) {
		lastdamaged = 0;
		
		if (owner == -1)
			currenthealth += (int) ((bullet.boosting ? 1.4 : 1) * bullet.damage * (bullet.owner == 0 ? 1 : -1));
		else
			currenthealth -= (int) ((bullet.boosting ? 1.4 : 1) * bullet.damage);
		
	}
	
	public void resetTargeting() {
		targeting = false;
		shooting = false;
	}
	
	public boolean bulletCollision(TankProjectile tempbullet) {
		
		if (Math.abs(currenthealth) >= maxhealth && owner == -1) {
			toTeam(tempbullet.owner);
			return true;
		} else if (currenthealth <= 0 && owner != -1)
			toNeutral(tempbullet.owner);
		else if (owner == -1 && clazz != 4 && !targeting) {
			activetarget = tempbullet.origindrone;
			targeting = true;
		}
		
		return false;
	}
	
}