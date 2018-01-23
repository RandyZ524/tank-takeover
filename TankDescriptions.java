import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TankDescriptions {
	Text name, descriptionline1, descriptionline2, health, damage, firingspeed;
	Rectangle healthrect, damagerect, firingspeedrect;
	
	TankDescriptions(Text name, Text descriptionline1, Text descriptionline2, Text health, Text damage, Text firingspeed, Rectangle healthrect, Rectangle damagerect, Rectangle firingspeedrect) {
		this.name = name;
		this.descriptionline1 = descriptionline1;
		this.descriptionline2 = descriptionline2;
		this.health = health;
		this.damage = damage;
		this.firingspeed = firingspeed;
		this.healthrect = healthrect;
		this.damagerect = damagerect;
		this.firingspeedrect = firingspeedrect;
	}
	
	public void create() {
		name.setX(500);
		name.setY(50);
		descriptionline1.setX(160);
		descriptionline1.setY(540);
		descriptionline2.setX(160);
		descriptionline2.setY(570);
		health.setX(875);
		health.setY(200);
		damage.setX(875);
		damage.setY(250);
		firingspeed.setX(875);
		firingspeed.setY(300);
		healthrect.setX(875);
		healthrect.setY(210);
		healthrect.setHeight(4);
		damagerect.setX(875);
		damagerect.setY(260);
		damagerect.setHeight(4);
		firingspeedrect.setX(875);
		firingspeedrect.setY(310);
		firingspeedrect.setHeight(4);
	}
	
	public void setDescriptionFont() {
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
		descriptionline1.setFont(Font.font("Verdana", 20));
		descriptionline2.setFont(Font.font("Verdana", 20));
		health.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		damage.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		firingspeed.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	}
	
	public void setStatsRects() {
		Color tempcolor = Color.WHITE;
		int templength = 0;
		
		switch (health.getText()) {
			case "Low":
				tempcolor = Color.BLUE;
				templength = 10;
				break;
			case "Medium":
				tempcolor = Color.GREEN;
				templength = 25;
				break;
			case "High":
				tempcolor = Color.YELLOW;
				templength = 50;
				break;
			case "Extreme":
				tempcolor = Color.RED;
				templength = 100;
				break;
		}
		
		healthrect.setFill(tempcolor);
		healthrect.setWidth(templength);
		healthrect.setArcWidth(8);
		healthrect.setArcHeight(8);
		
		switch (damage.getText()) {
			case "None":
				tempcolor = Color.BLACK;
				templength = 5;
				break;
			case "Minuscule":
				tempcolor = Color.BLUE;
				templength = 10;
				break;
			case "Low":
				tempcolor = Color.GREEN;
				templength = 25;
				break;
			case "High":
				tempcolor = Color.YELLOW;
				templength = 50;
				break;
			case "Extreme":
				tempcolor = Color.RED;
				templength = 100;
				break;
		}
		
		damagerect.setFill(tempcolor);
		damagerect.setWidth(templength);
		damagerect.setArcWidth(8);
		damagerect.setArcHeight(8);
		
		switch (firingspeed.getText()) {
			case "Sluggish":
				tempcolor = Color.BLACK;
				templength = 5;
				break;
			case "Slow":
				tempcolor = Color.BLUE;
				templength = 10;
				break;
			case "Average":
				tempcolor = Color.GREEN;
				templength = 25;
				break;
			case "Fast":
				tempcolor = Color.YELLOW;
				templength = 50;
				break;
			case "Extreme":
				tempcolor = Color.RED;
				templength = 100;
				break;
		}
		
		firingspeedrect.setFill(tempcolor);
		firingspeedrect.setWidth(templength);
		firingspeedrect.setArcWidth(8);
		firingspeedrect.setArcHeight(8);
	}
	
	public void setDescriptionVisible(boolean tempvisible) {
		name.setVisible(tempvisible);
		descriptionline1.setVisible(tempvisible);
		descriptionline2.setVisible(tempvisible);
		health.setVisible(tempvisible);
		damage.setVisible(tempvisible);
		firingspeed.setVisible(tempvisible);
		healthrect.setVisible(tempvisible);
		damagerect.setVisible(tempvisible);
		firingspeedrect.setVisible(tempvisible);
	}
	
}