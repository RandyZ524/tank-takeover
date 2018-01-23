import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Tank {
	int clazz, owner, centerx, centery, angle, maxreload, currentreload, damage, maxhealth, currenthealth, bulletspeed, bulletsize, bulletpenetration, bulletrange, barrellength, recoilframedata;
	boolean shooting;
	Circle base;
	Line barrel;
	
	public Tank() {
		this.clazz = 0;
		this.owner = 0;
		this.centerx = 0;
		this.centery = 0;
		this.angle = 0;
		this.maxreload = 0;
		this.currentreload = 0;
		this.damage = 0;
		this.maxhealth = 0;
		this.currenthealth = 0;
		this.bulletspeed = 0;
		this.bulletsize = 0;
		this.bulletpenetration = 0;
		this.bulletrange = 0;
		this.barrellength = 0;
		this.recoilframedata = 0;
		this.shooting = false;
		this.base = new Circle();
		this.barrel = new Line();
	}
	
	/* getTrueX method
		* This method uses the coordinate representation of the tank's x position (ranging from 0 to 10)
		* Coordinate 0 maps to a pixel coordinate of 100, and each coordinate is 80 pixels apart
			* Thus, the coordinate x is multiplied by 80 and added to 100
	*/
	public int getTrueX() { return centerx * Constants.LENGTH_OF_GRID_SQUARE + Constants.DISTANCE_FROM_BORDER_X; }
	
	/* getTrueY method
		* This method uses the coordinate representation of the tank's y position (ranging from 0 to 6)
		* Coordinate 0 maps to a pixel coordinate of 60, and each coordinate is 80 pixels apart
			* Thus, the coordinate x is multiplied by 80 and added to 60
	*/
	public int getTrueY() { return centery * Constants.LENGTH_OF_GRID_SQUARE + Constants.DISTANCE_FROM_BORDER_Y; }
	
	public int getIndex() { return centerx * 7 + centery; }
	
	/* fireBullet method
		* This method is called at every frame for all tanks
		* The variable "currentreload" is decremented at each frame
		* If the tank is indicating that they want to shoot and currentreload is 0, the method returns true
			* In addition, currentreload is set to the value of the attribute "maxreload"
			* If either condition isn't met, the method returns false
	*/
	public boolean fireBullet() {
		
		if (currentreload != 0)
			currentreload--;
		
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			recoilframedata = (int) Math.round((double) maxreload * 0.6);
			return true;
		}
		
		return false;
	}
	
	/* updateBarrel method
		* This method graphically updates the barrel (represented as a line with high StrokeWidth) by setting its end points
			* getTrueX and getTrueY are used to get the center of the tank, then the barrel length is received
			* This is added to the barrel length multiplied by the value from -1 to 1 returned by the trig functions performed on the tank angle
	*/
	public void updateBarrel() {
		double recoilfraction = 1;
		
		if (recoilframedata > 0) {
			recoilframedata--;
			double maxrecoilframes = (double) maxreload * 0.6;
			double fractionofrecoil = 1 - (recoilframedata / maxrecoilframes * 0.6);
			int truelength = barrellength + (int) barrel.getStrokeWidth();
			recoilfraction = (base.getRadius() + (truelength - base.getRadius()) * fractionofrecoil) / truelength;
		}
		
		barrel.setEndX(getTrueX() + barrellength * recoilfraction * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(getTrueY() - barrellength * recoilfraction * Math.cos(Math.toRadians(angle)));
	}
	
}