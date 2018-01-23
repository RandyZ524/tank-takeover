import javafx.animation.FadeTransition;
import javafx.scene.image.*;
import javafx.util.Duration;

public class HealingPlus {
	int frames;
	ImageView healingicon;
	
	HealingPlus() {
		this.frames = 0;
		this.healingicon = new ImageView(new Image("healing_icon.png"));
	}
	
	public void setOrigin(DroneTank tankdrone) {
		healingicon.setLayoutX(tankdrone.getTrueX() - 9);
		healingicon.setLayoutY(tankdrone.getTrueY() - 9);
		FadeTransition ft = new FadeTransition();
		ft.setNode(healingicon);
		ft.setDuration(new Duration(1000));
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.play();
	}
	
	public boolean updateAndRemove() {
		frames++;
		healingicon.setLayoutY(healingicon.getLayoutY() - 1);
		healingicon.toFront();
		return frames >= 60;
	}
	
}