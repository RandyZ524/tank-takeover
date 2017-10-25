import javafx.scene.shape.*;

public class TankProjectile {
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
	
	public void create(int tankowner, int tankcenterx, int tankcentery, int tankbarrellength, int tankangle, int tankbulletspeed, int tankbulletsize, int tankdamage, int tankpenetration, int tankrange) {
		owner = tankowner;
		centerx = originx = tankcenterx + tankbarrellength * Math.sin(Math.toRadians(tankangle));;
		centery = originy = tankcentery - tankbarrellength * Math.cos(Math.toRadians(tankangle));;
		angle = tankangle;
		speed = tankbulletspeed;
		size = tankbulletsize;
		damage = tankdamage;
		penetration = tankpenetration;
		range = tankrange;
		body.setCenterX(centerx);
		body.setCenterY(centery);
		body.setRadius(size);
	}
	
	public void updatePosition() {
		centerx += speed * Math.sin(Math.toRadians((double) angle));
		centery -= speed * Math.cos(Math.toRadians((double) angle));
		body.setCenterX(centerx);
		body.setCenterY(centery);
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