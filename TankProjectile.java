import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class TankProjectile {
	int owner, angle, size, damage, penetration, range, origindrone, currentricochet;
	double speed, centerx, centery, originx, originy;
	boolean ricochet, homing, boosting;
	Circle body;
	
	TankProjectile() {
		this.owner = 0;
		this.angle = 0;
		this.speed = 0;
		this.size = 0;
		this.damage = 0;
		this.penetration = 0;
		this.origindrone = 0;
		this.currentricochet = 0;
		this.centerx = 0;
		this.centery = 0;
		this.originx = 0;
		this.originy = 0;
		this.ricochet = false;
		this.homing = false;
		this.boosting = false;
		this.body = new Circle();
	}
	
	public void create(Tank tank, boolean tankricochet, int maxricochet, boolean tankhoming) {
		owner = tank.owner;
		originx = tank.getTrueX();
		originy = tank.getTrueY();
		origindrone = tank.getIndex();
		centerx = originx + tank.barrellength * Math.sin(Math.toRadians(tank.angle));
		centery = originy - tank.barrellength * Math.cos(Math.toRadians(tank.angle));
		angle = tank.angle;
		speed = (double) tank.bulletspeed;
		size = tank.bulletsize;
		damage = tank.damage;
		penetration = tank.bulletpenetration;
		range = tank.bulletrange;
		ricochet = tankricochet;
		currentricochet = ricochet ? maxricochet : 0;
		homing = tankhoming;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
	}
	
	public boolean generate(int mouseposx, int mouseposy, int bulletangle, int sizeofarray) {
		originx = mouseposx;
		originy = mouseposy;
		centerx = originx;
		centery = originy;
		angle = bulletangle;
		speed = 2;
		size = 5;
		penetration = 1;
		range = 99999;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
		
		return sizeofarray <= Constants.MAX_NUM_OF_BULLETS;
	}
	
	public void updatePosition() {
		centerx += speed * Math.sin(Math.toRadians((double) angle));
		centery -= speed * Math.cos(Math.toRadians((double) angle));
		body.setCenterX(centerx);
		body.setCenterY(centery);
	}
	
	public boolean offStage() {
		return centerx < 0 || centerx > Constants.STAGE_WIDTH || centery < 0 || centery > Constants.STAGE_HEIGHT;
	}
	
	public void ricochetMovement() {
		currentricochet--;
		
		if (currentricochet == 0)
			owner = -2;
		else {
			int tempangle = (centerx < 0 || centerx > Constants.STAGE_WIDTH) ? 360 : 180;
			angle = tempangle - angle;
			//updatePosition();
		}
		
	}
	
	public void homingMovement(PlayerTank[] temptankplayers, DroneTank[][] tankdrones, boolean inthelevel, boolean decay) {
		Random random = new Random();
		int closesttarget = 0;
		double closestsquaredistance = Double.MAX_VALUE;
		
		if (!inthelevel)
			for (DroneTank[] tankdronerow : tankdrones)
				for (DroneTank tankdrone : tankdronerow)
					
					if (tankdrone != null && (owner != tankdrone.owner || (decay && tankdrone.currenthealth + damage <= tankdrone.maxhealth))) {
						double tempdistance = Math.pow(tankdrone.getTrueX() - centerx, 2) + Math.pow(tankdrone.getTrueY() - centery, 2);
						
						if (tempdistance < closestsquaredistance) {
							closestsquaredistance = Math.pow(tankdrone.getTrueX() - centerx, 2) + Math.pow(tankdrone.getTrueY() - centery, 2);
							closesttarget = tankdrone.getIndex();
						}
						
					}
		
		double tempxpos, tempypos;
		
		if (closesttarget != 0) {
			tempxpos = tankdrones[closesttarget / 7][closesttarget % 7].getTrueX();
			tempypos = tankdrones[closesttarget / 7][closesttarget % 7].getTrueY();
		} else {
			int temp = owner == 0 ? 1 : 0;
			tempxpos = temptankplayers[temp].getTrueX();
			tempypos = temptankplayers[temp].getTrueY();
		}
		
		int targetangle;
		double temptargetangle;
		temptargetangle = Math.atan2(tempypos - centery, tempxpos - centerx) + Math.PI / 2.0;
		targetangle = (int) Math.round(Math.toDegrees(temptargetangle));
		targetangle = Math.floorMod(targetangle, 360);
		
		int turnspeed = (int) Math.round(speed * 0.5);
		
		if (Math.abs(Math.floorMod(targetangle - angle, 360)) <= turnspeed)
			angle = targetangle;
		
		if (Math.abs(targetangle - angle) > 180) {
			angle -= angle < targetangle ? turnspeed : (angle > targetangle ? -turnspeed : 0);
		} else {
			angle += angle < targetangle ? turnspeed : (angle > targetangle ? -turnspeed : 0);
		}
		
		angle = Math.floorMod(angle, 360);
		
		if (Math.abs(Math.floorMod(targetangle - angle, 360)) <= turnspeed) {
			angle = targetangle;
			speed *= 1.05;;
			boosting = true;
		} else {
			boosting = false;
		}
		
	}
	
	public boolean outOfRange() {
		
		if (Math.pow(centerx - originx, 2) + Math.pow(centery - originy, 2) > Math.pow(range, 2)) {
			owner = Constants.TB_ELIMINATED;
			return true;
		}
		
		return false;
	}
	
	public boolean droneCollisionLogic(DroneTank tankdrones[][], boolean decay) {
		
		for (DroneTank[] tankdronerow : tankdrones)
			for (DroneTank tankdrone : tankdronerow)
				if (tankdrone != null && intersectsValidTank(tankdrone))
					
					if (owner != tankdrone.owner) {
						tankdrone.removeHealth(this);
						
						if (tankdrone.bulletCollision(this))
							for (DroneTank[] tankdronerow2 : tankdrones)
								for (DroneTank tankdrone2 : tankdronerow2)
									if (tankdrone2 != null && tankdrone2.activetarget == tankdrone.getIndex())
										tankdrone2.resetTargeting();
						
						return true;
					} else if (decay && (origindrone == 3 || origindrone == 73) && tankdrone.currenthealth + damage <= tankdrone.maxhealth) {
						tankdrone.currenthealth += damage;
						tankdrone.currenthealth = tankdrone.currenthealth > tankdrone.maxhealth ? tankdrone.maxhealth : tankdrone.currenthealth;
						tankdrone.lastdamaged = 0;
						owner = -3;
						origindrone = tankdrone.getIndex();
						return true;
					}
		
		return false;
	}
	
	public boolean playerCollisionLogic(PlayerTank temptankplayers[]) {
		
		for (PlayerTank temptankplayer : temptankplayers)
			
			if (owner != temptankplayer.owner && intersectsValidTank(temptankplayer)) {
				temptankplayer.currenthealth -= (int) ((boosting ? 1.4 : 1) * damage);
				owner = Constants.TB_ELIMINATED;
				return true;
			}
		
		return false;
	}
	
	public void bulletCollisionLogic(ArrayList<TankProjectile> tempallprojectiles) {
		
		for (TankProjectile tempbullet : tempallprojectiles)
			
			if (intersectsValidBullet(tempbullet)) {
				int temppenetration = tempbullet.penetration;
				tempbullet.penetration -= penetration;
				penetration -= temppenetration;
				
				if (penetration <= 0) {
					owner = Constants.TB_ELIMINATED;
					return;
				}
				
				if (tempbullet.penetration <= 0)
					tempbullet.owner = Constants.TB_ELIMINATED;
				
			}
		
	}
	
	public boolean intersectsValidTank(Tank temptank) {
		
		if (size == 4)
			return false;
		if (body.getBoundsInParent().intersects(temptank.base.getBoundsInParent()))
			return true;
		
		return false;
	}
	
	private boolean intersectsValidBullet(TankProjectile tempbullet) {
		
		if (owner == Constants.TB_ELIMINATED || tempbullet.owner == Constants.TB_ELIMINATED)
			return false;
		if (owner  == tempbullet.owner)
			return false;
		if (body.getBoundsInParent().intersects(tempbullet.body.getBoundsInParent()))
			return true;
		
		return false;
	}
	
}