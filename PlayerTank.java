import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerTank extends Tank {
	int swiveltime, lastpressed, maxricochet, homingbasehealth;
	boolean turnleft, turnright, ricochet, megamind, homing;
	Line sightline, sightlinereflect, sightlinereflect2;
	Text healthdisplay;
	Rectangle homingbase;
	ImageView homingicon;
	
	PlayerTank() {
		super();
		
		this.swiveltime = 0;
		this.lastpressed = 0;
		this.maxricochet = 0;
		this.homingbasehealth = 0;
		this.turnleft = false;
		this.turnright = false;
		this.ricochet = false;
		this.megamind = false;
		this.homing = false;
		this.sightline = new Line();
		this.sightlinereflect = new Line();
		this.sightlinereflect2 = new Line();
		this.healthdisplay = new Text();
		this.homingbase = new Rectangle();
		this.homingicon = new ImageView(new Image("homing_icon.png"));
	}
	
	/* create method
		* This accepts an "ID" corresponding to the team of the player, either 0 or 1
			* The team ID is used to determine both the coordinates and the actual position of the player
			* In addition, the base, barrel, and sight line color of the player is determined by its team
		* The rest of the properties are standard for all players
	*/
	public void create(int tempid) {
		Color tempcolor = tempid == 0 ? Color.BLUE : Color.RED;
		clazz = 0;
		owner = tempid;
		centerx = owner == 0 ? 0 : 10;
		centery = 3;
		bulletrange = 99999;
		homingbasehealth = 500;
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
		homingbase.setX(owner == 0 ? 20 : 940);
		homingbase.setY(280);
		homingbase.setWidth(40);
		homingbase.setHeight(40);
		homingbase.setStroke(Color.BLACK);
		homingbase.setFill(Constants.LIGHT_GRAY);
		homingbase.setStrokeWidth(4);
		homingbase.setArcWidth(Constants.CORNER_ARC);
		homingbase.setArcHeight(Constants.CORNER_ARC);
		homingbase.setVisible(false);
		homingicon.setLayoutX(homingbase.getX() + 2);
		homingicon.setLayoutY(282);
		homingicon.setVisible(false);
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
				maxreload = currentreload = 25;
				damage = 10;
				maxhealth = currenthealth = 100;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				barrellength = 25;
				base.setRadius(20);
				barrel.setStrokeWidth(6);
				ricochet = false;
				megamind = false;
				break;
			case 1:
				maxreload = currentreload = 3;
				damage = 1;
				maxhealth = currenthealth = 120;
				bulletspeed = 3;
				bulletsize = 3;
				bulletpenetration = 1;
				barrellength = 30;
				base.setRadius(25);
				barrel.setStrokeWidth(4);
				ricochet = false;
				megamind = false;
				break;
			case 2:
				maxreload = currentreload = 90;
				damage = 29;
				maxhealth = currenthealth = 75;
				bulletspeed = 15;
				bulletsize = 2;
				bulletpenetration = 3;
				barrellength = 25;
				base.setRadius(15);
				barrel.setStrokeWidth(3);
				ricochet = false;
				megamind = false;
				break;
			case 3:
				maxreload = currentreload = 10;
				damage = 2;
				maxhealth = currenthealth = 100;
				bulletspeed = 3;
				bulletsize = 3;
				bulletpenetration = 1;
				barrellength = 34;
				base.setRadius(30);
				barrel.setStrokeWidth(3);
				ricochet = true;
				megamind = false;
				break;
			case 4:
				maxreload = currentreload = 40;
				damage = 10;
				maxhealth = currenthealth = 100;
				bulletspeed = 5;
				bulletsize = 3;
				bulletpenetration = 1;
				barrellength = 37;
				base.setRadius(35);
				barrel.setStrokeWidth(5);
				ricochet = false;
				megamind = true;
				break;
		}
		
		homing = false;
		barrel.setEndY(getTrueY() - barrellength);
		healthdisplay.setText(Integer.toString(currenthealth));
		
		if (ricochet) {
			sightlinereflect.setStartX(getTrueX());
			sightlinereflect.setStartY(0);
			sightlinereflect.setEndX(getTrueX());
			sightlinereflect.setEndY(1200);
			sightlinereflect.setStroke(sightline.getStroke());
			sightlinereflect2.setStartX(getTrueX());
			sightlinereflect2.setStartY(0);
			sightlinereflect2.setEndX(getTrueX());
			sightlinereflect2.setEndY(1200);
			sightlinereflect2.setStroke(sightline.getStroke());
			
			maxricochet = 3;
		} else {
			sightlinereflect.setVisible(false);
			sightlinereflect2.setVisible(false);
			
			maxricochet = 0;
		}
		
		
	}
	
	/* setPlayerVisible method
		* This method takes a boolean variable and sets the player to either visible or visible
		* Essentially runs the setVisible() method on an entire player object
	*/
	public void setPlayerVisible(boolean homingmode, boolean tempvisible) {
		base.setVisible(tempvisible);
		barrel.setVisible(tempvisible);
		healthdisplay.setVisible(tempvisible);
		sightline.setVisible(tempvisible);
		
		if (ricochet) {
			sightlinereflect.setVisible(tempvisible);
			sightlinereflect2.setVisible(tempvisible);
		} else {
			sightlinereflect.setVisible(false);
			sightlinereflect2.setVisible(false);
		}
		
		if (homingmode) {
			homingbase.setVisible(tempvisible);
			homingicon.setVisible(tempvisible);
		} else {
			homingbase.setVisible(false);
			homingicon.setVisible(false);
		}
		
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
		
		if ((key.equals("A") && owner == 0) || (key.equals("J") && owner == 1)) {
			turnleft = true;
			lastpressed = -1;
		} else if ((key.equals("D") && owner == 0) || (key.equals("L") && owner == 1)) {
			turnright = true;
			lastpressed = 1;
		} else if ((key.equals("W") && owner == 0) || (key.equals("I") && owner == 1)) {
			shooting = true;
		}
		
	}
	
	/* stopLRandShooting method
		* This method acts similarly to the LRandShooting method, except inputs map to setting actions to false
		* Running this in conjunction with LRandShooting allows multiple keyboard inputs to have effects at the same time
			* While a key has been pressed but hasn't been released, it is assumed that the key is being held, so the action continues to be performed
		* In addition, the keys S + K map to the "megamind" trait, which enables certain classes of players to reset the targeting of all controlled drones
			* If a reset is applicable (both correct class and correct key release), the method returns true
	*/
	public boolean stopLRandShooting(String key) {
		
		if ((key.equals("A") && owner == 0) || (key.equals("J") && owner == 1)) {
			turnleft = false;
		} else if ((key.equals("D") && owner == 0) || (key.equals("L") && owner == 1)) {
			turnright = false;
		} else if ((key.equals("W") && owner == 0) || (key.equals("I") && owner == 1)) { 
			shooting = false;
		} else if (megamind && ((key.equals("S") && owner == 0) || (key.equals("K") && owner == 1))) {
			return true;
		}
		
		return false;
	}
	
	/* switchClass method
		* This method takes keyboard inputs and changes the class of the corresponding player if the appropriate key is pressed
		* For player 1 and 2,
			* Q + U map to decrementing the class ID
			* E + O map to incrementing the class ID
		* The attributes of the player is updated if a class switch is made
	*/
	public void switchClass(String key, int numofplayerclasses) {
		int temp = 0;
		
		if (owner == 0) {
			
			if (key.equals("Q")) {
				temp = 1;
			} else if (key.equals("E")) {
				temp = -1;
			}
			
		} else {
			
			if (key.equals("U")) {
				temp = 1;
			} else if (key.equals("O")) {
				temp = -1;
			}
			
		}
		
		clazz = Math.floorMod(clazz - temp, numofplayerclasses);
		
		if (temp != 0)
			setClassAttributes();
		
	}
	
	/* moveLR method
		* This method uses the player attributes turnleft and turnright to turn the individual player according to keyboard inputs
		* While the player is pressing a turn indication, swiveltime is incremented
			* This value is effectively bounded at 90
			* The number of degrees to turn the player is given by
				* floor(swiveltime / 18)
		* Only when the key is held for 18/90 of a second or more the command to turn is permitted
			* However, if a turn key is held for less than this, the method causes the player to turn just 1 degree in either direction
			* This allows for fine control of the player angle
	*/
	public void moveLR() {
		
		if (!(turnright || turnleft) && swiveltime < 18 && swiveltime != 0) {
			angle += lastpressed == 1 ? 1 : -1;
			swiveltime = 0;
			return;
		}
		
		if (turnright || turnleft) {
			swiveltime++;
		} else if (swiveltime != 0) {
			swiveltime = 0;
		}
		
		if (swiveltime >= 18) {
			int turnangle = swiveltime < 90 ? (swiveltime / 18) + 1 : 6;
			angle += turnright ? turnangle : (turnleft ? -turnangle : 0);
			angle = Math.floorMod(angle, 360);
		}
		
	}
	
	/* updateHealthDisplay method
		* This method simply sets the Text node "healthdisplay" to the value of the attribute "currenthealth"
	*/
	public void updateHealthDisplay() {
		healthdisplay.setText(Integer.toString(currenthealth));
		healthdisplay.setX(getTrueX() - (healthdisplay.getLayoutBounds().getWidth() * 0.5));
	}
	
	public void updateSightLine() {
		sightline.setEndX(getTrueX() + 1200 * Math.sin(Math.toRadians(angle)));
		sightline.setEndY(getTrueY() - 1200 * Math.cos(Math.toRadians(angle)));
		
		if (ricochet) {
			int tempangle = updateReflection(sightlinereflect, sightline, angle);
			updateReflection(sightlinereflect2, sightlinereflect, tempangle);
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
		barrel.setVisible(true);
		healthdisplay.setVisible(true);
		sightline.setVisible(false);
		sightlinereflect.setVisible(false);
		sightlinereflect2.setVisible(false);
	}
	
	private int updateReflection(Line currentline, Line referenceline, int tempangle) {
		double tempx = referenceline.getStartX();
		double tempy = referenceline.getStartY();
		boolean inbounds = true;
		
		while (inbounds) {
			tempx += Math.sin(Math.toRadians(tempangle));
			tempy -= Math.cos(Math.toRadians(tempangle));
			
			if (tempx < 0 || tempx > Constants.STAGE_WIDTH) {
				inbounds = false;
			} else if (tempy < 0 || tempy > Constants.STAGE_HEIGHT) {
				inbounds = false;
			}
			
		}
		
		tempangle = (tempx < 0 || tempx > Constants.STAGE_WIDTH) ? 360 - tempangle : 180 - tempangle;
		currentline.setStartX(tempx);
		currentline.setStartY(tempy);
		currentline.setEndX(tempx + 1200 * Math.sin(Math.toRadians(tempangle)));
		currentline.setEndY(tempy - 1200 * Math.cos(Math.toRadians(tempangle)));
		
		return tempangle;
	}
	
}