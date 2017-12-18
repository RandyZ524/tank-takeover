import javafx.scene.shape.*;

public class TankProjectile {
	int owner, angle, speed, size, damage, penetration, range, origindrone, currentricochet;
	double centerx, centery, originx, originy;
	boolean ricochet;
	Circle body;
	
	TankProjectile(int owner, int angle, int speed, int size, int damage, int penetration, int range, int origindrone, int currentricochet, double centerx, double centery, double originx, double originy, boolean ricochet, Circle body) {
		this.owner = owner;
		this.angle = angle;
		this.speed = speed;
		this.size = size;
		this.damage = damage;
		this.penetration = penetration;
		this.origindrone = origindrone;
		this.currentricochet = currentricochet;
		this.centerx = centerx;
		this.centery = centery;
		this.originx = originx;
		this.originy = originy;
		this.ricochet = ricochet;
		this.body = body;
	}
	
	public boolean create(int tankowner, int tankcenterx, int tankcentery, int tankindex, int tankbarrellength, int tankangle, int tankbulletspeed, int tankbulletsize, int tankdamage, int tankpenetration, int tankrange, int maxricochet, boolean tankricochet, int sizeofarray) {
		owner = tankowner;
		originx = tankcenterx;
		originy = tankcentery;
		origindrone = tankindex;
		centerx = originx + tankbarrellength * Math.sin(Math.toRadians(tankangle));
		centery = originy - tankbarrellength * Math.cos(Math.toRadians(tankangle));
		angle = tankangle;
		speed = tankbulletspeed;
		size = tankbulletsize;
		damage = tankdamage;
		penetration = tankpenetration;
		range = tankrange;
		ricochet = tankricochet;
		currentricochet = ricochet ? maxricochet : 0;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
		
		return sizeofarray <= 1000;
	}
	
	public void updatePosition() {
		centerx += speed * Math.sin(Math.toRadians((double) angle));
		centery -= speed * Math.cos(Math.toRadians((double) angle));
		body.setCenterX(centerx);
		body.setCenterY(centery);
	}
	
	public boolean offStage(int stagewidth, int stageheight) {
		return centerx < 0 || centerx > stagewidth || centery < 0 || centery > stageheight;
	}
	
	public void ricochetMovement(int stagewidth, int stageheight) {
		currentricochet--;
		
		if (currentricochet == 0)
			owner = -2;
		else {
			int tempangle = (centerx < 0 || centerx > stagewidth) ? 360 : 180;
			angle = tempangle - angle;
			updatePosition();
		}
		
	}
	
	public boolean outOfRange() {
		return Math.pow(centerx - originx, 2) + Math.pow(centery - originy, 2) > Math.pow(range, 2);
	}
	
	public boolean intersectsNonGuard(Circle tempbase, int tempowner) {
		return body.getBoundsInParent().intersects(tempbase.getBoundsInParent()) && owner != tempowner && size != 4;
	}
	
}