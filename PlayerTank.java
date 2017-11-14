import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class PlayerTank {
	int clazz, team, centerx, centery, angle, maxreload, currentreload, damage, currenthealth, bulletspeed, bulletsize, bulletpenetration, swiveltime, lastpressed;
	boolean turnleft, turnright, shooting, ricochet, megamind;
	Circle base;
	Line barrel, sightline, sightlinereflect, sightlinereflect2;
	Text healthdisplay;
	
	PlayerTank(int clazz, int team, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int currenthealth, int bulletspeed, int bulletsize, int bulletpenetration, int swiveltime, int lastpressed, boolean turnleft, boolean turnright, boolean shooting, boolean ricochet, boolean megamind, Circle base, Line barrel, Line sightline, Line sightlinereflect, Line sightlinereflect2, Text healthdisplay) {
		this.clazz = clazz;
		this.team = team;
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
		this.ricochet = ricochet;
		this.megamind = megamind;
		this.base = base;
		this.barrel = barrel;
		this.sightline = sightline;
		this.sightlinereflect = sightlinereflect;
		this.sightlinereflect2 = sightlinereflect2;
		this.healthdisplay = healthdisplay;
	}
	
	public void create(int tempid) {
		clazz = 0;
		team = tempid;
		centerx = team == 0 ? 0 : 10;
		centery = 3;
		base.setCenterX(getTrueX());
		base.setCenterY(getTrueY());
		base.setFill(team == 0 ? Color.BLUE : Color.RED);
		barrel.setStartX(getTrueX());
		barrel.setStartY(getTrueY());
		barrel.setEndX(getTrueX());
		barrel.setStroke(team == 0 ? Color.BLUE : Color.RED);
		sightline.setStartX(getTrueX());
		sightline.setStartY(getTrueY());
		sightline.setEndX(getTrueX());
		sightline.setEndY(getTrueY() - 1200);
		sightline.setStroke(team == 0 ? Color.BLUE : Color.RED);
		healthdisplay.setX(getTrueX() - 10);
		healthdisplay.setY(getTrueY() + 4);
	}
	
	public void setPlayerClass(int playerclass) {
		clazz = playerclass;
	}
	
	public void setClassAttributes() {
		
		switch (clazz) {
			case 0:
				maxreload = currentreload = 30;
				damage = 10;
				currenthealth = 100;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(20);
				barrel.setStrokeWidth(6);
				ricochet = false;
				megamind = false;
				break;
			case 1:
				maxreload = currentreload = 3;
				damage = 1;
				currenthealth = 120;
				bulletspeed = 4;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(25);
				barrel.setStrokeWidth(4);
				ricochet = false;
				megamind = false;
				break;
			case 2:
				maxreload = currentreload = 90;
				damage = 29;
				currenthealth = 75;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 3;
				base.setRadius(15);
				barrel.setStrokeWidth(3);
				ricochet = false;
				megamind = false;
				break;
			case 3:
				maxreload = currentreload = 10;
				damage = 2;
				currenthealth = 100;
				bulletspeed = 3;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(30);
				barrel.setStrokeWidth(3);
				ricochet = true;
				megamind = false;
				break;
			case 4:
				maxreload = currentreload = 40;
				damage = 10;
				currenthealth = 100;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				base.setRadius(35);
				barrel.setStrokeWidth(5);
				ricochet = false;
				megamind = true;
				break;
		}
		
		barrel.setEndY(getTrueY() - getBarrelLength());
		healthdisplay.setText(Integer.toString(currenthealth));
		
		if (ricochet) {
			sightlinereflect.setStartX(getTrueX());
			sightlinereflect.setStartY(0);
			sightlinereflect.setEndX(getTrueX());
			sightlinereflect.setEndY(1200);
			sightlinereflect.setStroke(sightline.getStroke());
			sightlinereflect.setStrokeWidth(1);
			sightlinereflect2.setStartX(getTrueX());
			sightlinereflect2.setStartY(0);
			sightlinereflect2.setEndX(getTrueX());
			sightlinereflect2.setEndY(1200);
			sightlinereflect2.setStroke(sightline.getStroke());
			sightlinereflect2.setStrokeWidth(1);
		} else {
			sightlinereflect.setStrokeWidth(0);
			sightlinereflect2.setStrokeWidth(0);
		}
		
	}
	
	public void setPlayerVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthdisplay.setVisible(tempvisible);
		sightline.setVisible(tempvisible);
		sightlinereflect.setVisible(tempvisible);
		sightlinereflect2.setVisible(tempvisible);
	}
	
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		healthdisplay.toFront();
	}
	
	public void moveLR() {
		if (!(turnright || turnleft) && swiveltime < 12 && swiveltime != 0) angle += lastpressed == 1 ? 1 : -1;
		if (turnright || turnleft) swiveltime++;
		else if (swiveltime != 0) swiveltime = 0;
		
		int turnangle = swiveltime < 60 ? swiveltime / 12 : 5;
		angle += turnright ? turnangle : (turnleft ? -turnangle : 0);
		angle = Math.floorMod(angle, 360);
	}
	
	public int getTrueX() {
		return centerx * 80 + 100;
	}
	
	public int getTrueY() {
		return centery * 80 + 60;
	}
	
	public void updateBarrel() {
		barrel.setEndX(getTrueX() + getBarrelLength() * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(getTrueY() - getBarrelLength() * Math.cos(Math.toRadians(angle)));
	}
	
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(currenthealth));
	}
	
	public void updateSightLine() {
		sightline.setEndX(getTrueX() + 1200 * Math.sin(Math.toRadians(angle)));
		sightline.setEndY(getTrueY() - 1200 * Math.cos(Math.toRadians(angle)));
		
		if (clazz == 3) {
			double tempx = sightline.getStartX();
			double tempy = sightline.getStartY();
			boolean inbounds = true;
			int tempangle = 0;
			
			while (inbounds) {
				tempx += Math.sin(Math.toRadians(angle));
				tempy -= Math.cos(Math.toRadians(angle));
				if (tempx < 0 || tempx > 1000) inbounds = false;
				else if (tempy < 0 || tempy > 600) inbounds = false;
			}
			
			tempangle = (tempx < 0 || tempx > 1000) ? 360 - angle : 180 - angle;
			sightlinereflect.setStartX(tempx);
			sightlinereflect.setStartY(tempy);
			sightlinereflect.setEndX(tempx + 1200 * Math.sin(Math.toRadians(tempangle)));
			sightlinereflect.setEndY(tempy - 1200 * Math.cos(Math.toRadians(tempangle)));
			
			inbounds = true;
			tempx = sightlinereflect.getStartX();
			tempy = sightlinereflect.getStartY();
			
			while (inbounds) {
				tempx += Math.sin(Math.toRadians(tempangle));
				tempy -= Math.cos(Math.toRadians(tempangle));
				if (tempx < 0 || tempx > 1000) inbounds = false;
				else if (tempy < 0 || tempy > 600) inbounds = false;
			}
			
			tempangle = (tempx < 0 || tempx > 1000) ? 360 - tempangle : 180 - tempangle;
			sightlinereflect2.setStartX(tempx);
			sightlinereflect2.setStartY(tempy);
			sightlinereflect2.setEndX(tempx + 1200 * Math.sin(Math.toRadians(tempangle)));
			sightlinereflect2.setEndY(tempy - 1200 * Math.cos(Math.toRadians(tempangle)));
		}
		
	}
	
	public boolean fireBullet() {
		if (currentreload != 0) currentreload--;
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
	public int getBarrelLength() {
		
		switch (clazz) {
			case 0: return 25;
			case 1: return 30;
			case 2: return 22;
			case 3: return 34;
			case 4: return 37;
			default: return 25;
		}
		
	}
	
	public void reset(int tempid) {
		create(tempid);
		setClassAttributes();
		angle = 0;
		healthdisplay.setVisible(true);
		sightline.setVisible(false);
		sightlinereflect.setVisible(false);
		sightlinereflect2.setVisible(false);
	}
	
}