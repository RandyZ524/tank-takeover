import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.*;

public class DroneTank {
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, maxhealth, currenthealth, bulletspeed, bulletsize, bulletpenetration, bulletrange, activetarget, lastdamaged;
	boolean underattack, shooting, targeting;
	Circle base, ownerflag;
	Line barrel;
	Rectangle healthbase, bluehealthbar, redhealthbar;
	ImageView icon;
	
	DroneTank(int clazz, int owner, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int maxhealth, int currenthealth, int bulletspeed, int bulletsize, int bulletpenetration, int bulletrange, int activetarget, int lastdamaged, boolean underattack, boolean shooting, boolean targeting, Circle base, Circle ownerflag, Line barrel, Rectangle healthbase, Rectangle bluehealthbar, Rectangle redhealthbar, ImageView icon) {
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
		bluehealthbar.setWidth(9);
		bluehealthbar.setHeight(4);
		bluehealthbar.setFill(Color.BLUE);
		redhealthbar.setX(getTrueX());
		redhealthbar.setY(getTrueY() - 2);
		redhealthbar.setWidth(9);
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
				base.setRadius(25);
				barrel.setStrokeWidth(12);
				icon.setLayoutX(getTrueX() - 11);
				icon.setLayoutY(getTrueY() - 13);
				break;
		}
		
		icon.setImage(tempicon);
		ownerflag.setRadius(0.8 * base.getRadius());
		barrel.setEndY(getTrueY() - getBarrelLength());
	}
	
	public void setDroneVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		ownerflag.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthbase.setVisible(tempvisible);
		bluehealthbar.setVisible(tempvisible);
		redhealthbar.setVisible(tempvisible);
		icon.setVisible(tempvisible);
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
	
	public int getTrueX() {
		return 100 + (centerx * 80);
	}
	
	public int getTrueY() {
		return 60 + (centery * 80);
	}
	
	public void updateBarrel() {
		barrel.setEndX(getTrueX() + getBarrelLength() * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(getTrueY() - getBarrelLength() * Math.cos(Math.toRadians(angle)));
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
	}
	
	public void toNeutral() {
		ownerflag.setFill(Color.GRAY);
		ownerflag.toFront();
		currenthealth *= owner == 0 ? 1 : -1;
		owner = -1;
		targeting = shooting = false;
		icon.toFront();
		healthbase.toFront();
		bluehealthbar.toFront();
		redhealthbar.toFront();
	}
	
	public void findNewTarget(DroneTank[][] temptankdrones, PlayerTank[] temptankplayers) {
		int closesttarget = 0;
		double closestsquaredistance = 99999;
		double tempdistance = 0;
		ArrayList<Integer> possibletargets = new ArrayList<Integer>();
		Random random = new Random();
		
		for (DroneTank[] temptankdronerow : temptankdrones) {
			
			for (DroneTank temptankdrone : temptankdronerow) {
			
				if (temptankdrone != null && owner != temptankdrone.owner) {
					tempdistance = Math.pow((double) temptankdrone.centerx - centerx, 2) + Math.pow((double) temptankdrone.centery - centery, 2);
				
					if (tempdistance < closestsquaredistance && 6400 * tempdistance < Math.pow(bulletrange, 2)) {
						closestsquaredistance = Math.pow((double) temptankdrone.centerx - centerx, 2) + Math.pow((double) temptankdrone.centery - centery, 2);
						closesttarget = temptankdrone.centerx * 7 + temptankdrone.centery;
						possibletargets.clear();
						possibletargets.add(temptankdrone.centerx * 7 + temptankdrone.centery);
					} else if (tempdistance == closestsquaredistance && 6400 * tempdistance < Math.pow(bulletrange, 2)) {
						possibletargets.add(temptankdrone.centerx * 7 + temptankdrone.centery);
					}
					
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
				} else if (tempdistance == closestsquaredistance) possibletargets.add(i1);
				
			}
			
		}
		
		if (possibletargets.size() != 0) {
			closesttarget = possibletargets.get(random.nextInt(possibletargets.size()));
			activetarget = closesttarget;
			targeting = true;
		}
		
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
	}
	
	public boolean fireBullet() {
		if (currentreload != 0) currentreload--;
		
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
		
	}
	
	public void removeHealth(int tempdamage, int tempowner) {
		lastdamaged = 0;
		if (owner == -1) currenthealth -= (tempdamage * (tempowner == 0 ? -1 : 1));
		else currenthealth -= tempdamage;
	}
	
	public void resetTargeting() {
		targeting = false;
		shooting = false;
	}
	
	public void healthBelowZeroActivites(int tempowner, DroneTank[][] temptankdrones, int j1) {
		
		if (Math.abs(currenthealth) >= maxhealth && owner == -1) {
			toTeam(tempowner);
			
			for (DroneTank[] temptankdronerow : temptankdrones) {
				
				for (DroneTank temptankdrone : temptankdronerow) {
					if (temptankdrone != null && temptankdrone.activetarget == j1) temptankdrone.resetTargeting();
				}
				
			}
			
		} else if (currenthealth <= 0 && owner != -1) toNeutral();
		
	}
	
	public int getBarrelLength() {
		
		switch (clazz) {
			case 0: return 25;
			case 1: return 30;
			case 2: return 31;
			case 3: return 30;
			case 4: return 22;
		}
		
		return 25;
	}
	
}