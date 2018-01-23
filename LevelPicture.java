import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class LevelPicture {
	String[] description;
	ImageView thumbnail;
	
	public static int levelselection;
	public static Rectangle selectionbox;
	public static Rectangle bigimagebase;
	public static Line redcrossA, redcrossB;
	public static Text noselection;
	public static Text[] displaydesc;
	public static ImageView bigimage;
	
	static {
		levelselection = 0;
		
		selectionbox = new Rectangle(Constants.STAGE_WIDTH + 10, 17, 170, 136);
		selectionbox.setStroke(Color.GREEN);
		selectionbox.setFill(Color.TRANSPARENT);
		selectionbox.setStrokeWidth(6);
		selectionbox.setArcWidth(Constants.CORNER_ARC);
		selectionbox.setArcHeight(Constants.CORNER_ARC);
		
		bigimagebase = new Rectangle(504, 375, Constants.LIGHT_GRAY);
		bigimagebase.setStroke(Color.BLACK);
		bigimagebase.setFill(Color.WHITE);
		bigimagebase.setStrokeWidth(4);
		bigimagebase.setVisible(false);
		
		redcrossA = new Line(225, 438, 485, 565);
		redcrossB = new Line(225, 565, 485, 438);
		redcrossA.setStroke(Color.RED);
		redcrossB.setStroke(Color.RED);
		redcrossA.setStrokeWidth(5);
		redcrossB.setStrokeWidth(5);
		redcrossA.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossB.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossA.setVisible(false);
		redcrossB.setVisible(false);
		
		noselection = new Text(250, 420, "Please select a stage!");
		noselection.setFont(Font.font("Verdana", 20));
		noselection.setVisible(false);
		
		displaydesc = new Text[2];
		
		for (int i = 0; i < 2; i++) {
			displaydesc[i] = new Text();
			displaydesc[i].setFont(Font.font("Verdana", FontPosture.ITALIC, 16));
			displaydesc[i].setVisible(false);
		}
		
		bigimage = new ImageView();
	}
	
	public LevelPicture() {
		this.description = new String[2];
		this.thumbnail = new ImageView();
	}
	
	public void create(int index) {
		thumbnail.setImage(new Image("Level_Thumbnails/" + (index + 1) + ".png"));
		thumbnail.setLayoutX(25 + (index * 200));
		thumbnail.setLayoutY(25);
		
		switch (index) {
			case 0:
				description[0] = "A basic, well-balanced map. Features back-line";
				description[1] = "Snipers, two Sprayers, and a single Annihilator.";
				break;
			case 1:
				description[0] = "Snipers fill the field and ruthlessly pick off";
				description[1] = "enemies, no matter the distance or protection.";
				break;
			case 2:
				description[0] = "A ring of fast-firing Sprayers makes the center";
				description[1] = "a critical and valuable area to control.";
				break;
			case 3:
				description[0] = "With powerful Annihilators everywhere, one errant";
				description[1] = "shot can result in deadly drone retaliation.";
				break;
			case 4:
				description[0] = "A sparse map with weak drones. Only the Snipers";
				description[1] = "can succeed in breaking through the enemy Guard.";
				break;
		}
		
	}
	
	public static void displayPreview(int index, LevelPicture[] levelpictures) {
		bigimagebase.setVisible(true);
		bigimagebase.toFront();
		bigimage.setImage(new Image("Big_Level_Pictures/" + (index + 1) + ".png"));
		bigimage.setVisible(true);
		bigimage.toFront();
		
		for (int i = 0; i < 2; i++) {
			displaydesc[i].setText(levelpictures[index].description[i]);
			displaydesc[i].setVisible(true);
			displaydesc[i].toFront();
		}
	}
	
	public static void dynamicPreview(MouseEvent event, ImageView hoverthumbnail) {
		double mouseposx = event.getX() + hoverthumbnail.getLayoutX();
		double mouseposy = event.getY() + hoverthumbnail.getLayoutY();
		
		int centerofbase = (int) Math.round(bigimagebase.getWidth() * 0.5);
		
		if (mouseposx + centerofbase > Constants.STAGE_WIDTH)
			mouseposx = Constants.STAGE_WIDTH - centerofbase;
		if (mouseposx - centerofbase < bigimagebase.getStrokeWidth())
			mouseposx = centerofbase + bigimagebase.getStrokeWidth();
		
		bigimagebase.setX(mouseposx - (bigimagebase.getWidth() * 0.5) - 2);
		bigimagebase.setY(mouseposy + 18);
		bigimage.setX(bigimagebase.getX() + 2);
		bigimage.setY(bigimagebase.getY() + 2);
		
		for (int i = 0; i < 2; i++) {
			displaydesc[i].setX(bigimagebase.getX() + centerofbase - (displaydesc[i].getLayoutBounds().getWidth() * 0.5));
			displaydesc[i].setY(bigimagebase.getY() + 330 + (i * 25));
		}
		
	}
	
	public static void hidePreview() {
		bigimagebase.setVisible(false);
		bigimage.setVisible(false);
		
		for (Text desc : displaydesc)
			desc.setVisible(false);
		
	}
	
	public static void changeLevelSelection(int index) {
		
		if (levelselection != index + 1) {
			levelselection = index + 1;
			selectionbox.setX(index * 200 + 15);
			setNoSelectionVisible(false);
		}
		
	}
	
	public static void setNoSelectionVisible(boolean visible) {
		redcrossA.setVisible(visible);
		redcrossB.setVisible(visible);
		noselection.setVisible(visible);
	}
	
}