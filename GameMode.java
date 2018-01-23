import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameMode {
	boolean enabled;
	String[] description;
	CheckBox togglemode;
	
	static Rectangle descriptionbase = new Rectangle(360, 165, Constants.LIGHT_GRAY);
	static Text[] descriptiondisplay = new Text[6];
	
	static {
		descriptionbase.setStroke(Color.BLACK);
		descriptionbase.setStrokeWidth(4);
		descriptionbase.setArcWidth(Constants.CORNER_ARC);
		descriptionbase.setArcHeight(Constants.CORNER_ARC);
		descriptionbase.setVisible(false);
	}
	
	public GameMode() {
		this.enabled = false;
		this.description = new String[6];
		this.togglemode = new CheckBox();
	}
	
	public void create(String tempstring, int index) {
		togglemode.setText(tempstring.substring(0, 1) + tempstring.substring(1).toLowerCase());
		togglemode.setLayoutX(25 + (index * 200));
		togglemode.setLayoutY(160);
	}
	
}