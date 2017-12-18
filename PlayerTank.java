import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class PlayerTank {
	int clazz, team, centerx, centery, angle, maxreload, currentreload, damage, currenthealth, bulletspeed, bulletsize, bulletpenetration, swiveltime, lastpressed, maxricochet;
	boolean turnleft, turnright, shooting, ricochet, megamind;
	Circle base;
	Line barrel, sightline, sightlinereflect, sightlinereflect2;
	Text healthdisplay;
	
	PlayerTank(int clazz, int team, int centerx, int centery, int angle, int maxreload, int currentreload, int damage, int currenthealth, int bulletspeed, int bulletsize, int bulletpenetration, int swiveltime, int lastpressed, int maxricochet, boolean turnleft, boolean turnright, boolean shooting, boolean ricochet, boolean megamind, Circle base, Line barrel, Line sightline, Line sightlinereflect, Line sightlinereflect2, Text healthdisplay) {
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
		this.maxricochet = maxricochet;
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
	
	/* create method
		* This accepts an "ID" corresponding to the team of the player, either 0 or 1
			* The team ID is used to determine both the coordinates and the actual position of the player
			* In addition, the base, barrel, and sightline color of the player is determined by its team
		* The rest of the properties are standard for all players
	*/
	public void create(int tempid) {
		Color tempcolor = tempid == 0 ? Color.BLUE : Color.RED;
		clazz = 0;
		team = tempid;
		centerx = team == 0 ? 0 : 10;
		centery = 3;
		base.setCenterX(getTrueX());
		base.setCenterY(getTrueY());
		base.setFill(tempcolor);
		barrel.setStartX(getTrueX());
		barrel.setStartY(getTrueY());
		barrel.setEndX(getTrueX());
		barrel.setStroke(tempcolor);
		sightline.setStartX(getTrueX());
		sightline.setStartY(getTrueY());
		sightline.setEndX(getTrueX());
		sightline.setEndY(getTrueY() - 1200);
		sightline.setStroke(tempcolor);
		healthdisplay.setX(getTrueX() - 10);
		healthdisplay.setY(getTrueY() + 4);
	}
	
	/* setPlayerClass method
		* Takes an integer and the number of total player classes
		* If this is a valid class ID, sets the player class parameter to this
	*/
	public void setPlayerClass(int playerclass, int numofplayerclasses) {
		
		if (playerclass >= 0 && playerclass < numofplayerclasses)
			clazz = playerclass;
		
	}
	
	/* setClassAttributes method
		* This is called every time the class of the player is switched
		* It assigns stats and properties to the specific classes
	*/
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
			
			maxricochet = 3;
		} else {
			sightlinereflect.setStrokeWidth(0);
			sightlinereflect2.setStrokeWidth(0);
			
			maxricochet = 0;
		}
		
		
	}
	
	/* setPlayerVisible method
		* This method takes a boolean variable and sets the player to either visible or visible
		* Essentially runs the setVisible() method on an entire player object
	*/
	public void setPlayerVisible(boolean tempvisible) {
		base.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthdisplay.setVisible(tempvisible);
		sightline.setVisible(tempvisible);
		sightlinereflect.setVisible(tempvisible);
		sightlinereflect2.setVisible(tempvisible);
	}
	
	/* moveToFront method
		* This method moves the player parts to the front of the scene when called
		* Essentially runs the toFront() method on an entire player object
	*/
	public void moveToFront() {
		base.toFront();
		barrel.toFront();
		healthdisplay.toFront();
	}
	
	/* LRandShooting method
		* This method takes in a String corresponding to keyboard inputs when called
		* Each pair of keys maps to an action taken by one of the two players. For player 1 and 2, respectively,
			* A + J map to turning left
			* D + L map to turning right
			* W + I map to firing bullets
		* Turning left/right also set a variable to -1/1, respectively. This is used by the turning player method to turn a player by 1 degree if tapped
	*/
	public void LRandShooting(String key) {
		
		if ((key.equals("A") && team == 0) || (key.equals("J") && team == 1)) {
			turnleft = true;
			lastpressed = -1;
		} else if ((key.equals("D") && team == 0) || (key.equals("L") && team == 1)) {
			turnright = true;
			lastpressed = 1;
		} else if ((key.equals("W") && team == 0) || (key.equals("I") && team == 1))
			shooting = true;
		
	}
	
	/* stopLRandShooting method
		* This method acts similarly to the LRandShooting method, except inputs map to setting actions to false
		* Running this in conjunction with LRandShooting allows multiple keyboard inputs to have effects at the same time
			* While a key has been pressed but hasn't been released, it is assumed that the key is being held, so the action continues to be performed
		* In addition, the keys S + K map to the "megamind" trait, which enables certain classes of players to reset the targeting of all controlled drones
			* If a reset is applicable (both correct class and correct key release), the method returns true
	*/
	public boolean stopLRandShooting(String key) {
		
		if ((key.equals("A") && team == 0) || (key.equals("J") && team == 1))
			turnleft = false;
		else if ((key.equals("D") && team == 0) || (key.equals("L") && team == 1))
			turnright = false;
		else if ((key.equals("W") && team == 0) || (key.equals("I") && team == 1))
			shooting = false;
		else if (megamind && ((key.equals("S") && team == 0) || (key.equals("K") && team == 1)))
			return true;
		
		return false;
	}
	
	/* switchClass method
		* This method takes keyboard inputs and changes the class of the corresponding player if the approppriate key is pressed
		* For player 1 and 2,
			* Q + U map to decrementing the class ID
			* E + O map to incrementing the class ID
		* The attributes of the player is updated if a class switch is made
	*/
	public void switchClass(String key, int numofplayerclasses) {
		int temp = 0;
		
		if ((key.equals("Q") && team == 0) || (key.equals("U") && team == 1))
			temp = 1;
		else if ((key.equals("E") && team == 0) || (key.equals("O") && team == 1))
			temp = -1;
		
		clazz = Math.floorMod(clazz - temp, numofplayerclasses);
		
		if (temp != 0)
			setClassAttributes();
		
	}
	
	/* moveLR method
		* This method uses the player attributes turnleft and turnright to turn the individual player according to keyboard inputs
		* While the player is pressing a turn indication, swiveltime is incremented
			* This value is effectively bounded at 60
			* The number of degrees to turn the player is given by
				* floor(swiveltime / 12)
		* Only when the key is held for 12/60 of a second or more the command to turn is permitted
			* However, if a turn key is held for less than this, the method causes the player to turn just 1 degree in either direction
			* This allows for fine control of the player angle
	*/
	public void moveLR() {
		
		if (!(turnright || turnleft) && swiveltime < 12 && swiveltime != 0)
			angle += lastpressed == 1 ? 1 : -1;
		if (turnright || turnleft)
			swiveltime++;
		else if (swiveltime != 0)
			swiveltime = 0;
		
		int turnangle = swiveltime < 60 ? swiveltime / 12 : 5;
		angle += turnright ? turnangle : (turnleft ? -turnangle : 0);
		angle = Math.floorMod(angle, 360);
	}
	
	/* getTrueX method
		* This method uses the coordinate representation of the player's x position (ranging from 0 to 10)
		* Coordinate 0 maps to a pixel coordinate of 100, and each coordinate is 80 pixels apart
			* Thus, the coordinate x is multiplied by 80 and added to 100
	*/
	public int getTrueX() { return centerx * 80 + 100; }
	
	/* getTrueY method
		* This method uses the coordinate representation of the player's y position (ranging from 0 to 6)
		* Coordinate 0 maps to a pixel coordinate of 60, and each coordinate is 80 pixels apart
			* Thus, the coordinate x is multiplied by 80 and added to 60
	*/
	public int getTrueY() { return centery * 80 + 60; }
	
	/* updateBarrel method
		* This method graphically updates the barrel (represented as a line with high StrokeWidth) by setting its end points
			* getTrueX and getTrueY are used to get the center of the tank, then the barrel length is received
			* This is added to the barrel length multiplied by the value from -1 to 1 returned by the trig functions performed on the tank angle
	*/
	public void updateBarrel() {
		barrel.setEndX(getTrueX() + getBarrelLength() * Math.sin(Math.toRadians(angle)));
		barrel.setEndY(getTrueY() - getBarrelLength() * Math.cos(Math.toRadians(angle)));
	}
	
	/* updateHealthDisplay method
		* This method simply sets the Text node "healthdisplay" to the value of the attribute "currenthealth"
	*/
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
				
				if (tempx < 0 || tempx > 1000)
					inbounds = false;
				else if (tempy < 0 || tempy > 600)
					inbounds = false;
				
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
				
				if (tempx < 0 || tempx > 1000)
					inbounds = false;
				else if (tempy < 0 || tempy > 600)
					inbounds = false;
			}
			
			tempangle = (tempx < 0 || tempx > 1000) ? 360 - tempangle : 180 - tempangle;
			sightlinereflect2.setStartX(tempx);
			sightlinereflect2.setStartY(tempy);
			sightlinereflect2.setEndX(tempx + 1200 * Math.sin(Math.toRadians(tempangle)));
			sightlinereflect2.setEndY(tempy - 1200 * Math.cos(Math.toRadians(tempangle)));
		}
		
	}
	
	/* fireBullet method
		* This method is called at every frame for both players
		* The variable "currentreload" is decremented at each frame
		* If the player is indicating that they want to shoot and currentreload is 0, the method returns true
			* In addition, currentreload is set to the value of the attribute "maxreload"
			* If either condition isn't met, the method returns false
	*/
	public boolean fireBullet() {
		
		if (currentreload != 0)
			currentreload--;
		if (shooting && currentreload == 0) {
			currentreload = maxreload;
			return true;
		}
		
		return false;
	}
	
	/* getBarrelLength method
		* This method uses the class of the player to return the length of its barrel
		* Called by the setClassAttributes and the updateBarrel method
		* Defaults to the length of class 0, though this pathway is never taken
	*/
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
	
	/* reset method
		* This method uses the player team to properly reset the player properties after a game has ended
		* Called for both players after selecting the back button once a game has ended
		* Since class is set to 0 during the create method, the setClassAttributes method is run afterwards
		* Other traits are reset such that the state of the game is equivalent to startup
	*/
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