import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
//Importing my stuff

public class TankTakeover extends Application {
	
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }
 
	public static int STAGE_WIDTH = 1000;
	public static int STAGE_HEIGHT = 600;
	public static int DISTANCE_FROM_BORDER_X = 100;
	public static int DISTANCE_FROM_BORDER_Y = 60;
	public static int LENGTH_OF_GRID_SQUARE = 80;
	public static int LENGTH_OF_BARREL = 25;
	public static int NUM_OF_PLAYERS = 2;
	public static int NUM_OF_DRONES = 5;
	public static int NUM_OF_LEVELS = 5;
	public static int TB_ELIMINATED = -2;
	public static int CORNER_ARC = 20;
	
	int i, j, k, levelselection, demoangle = 0;
	int demotankselection = -1;
	boolean inthelevel, inhelp = false;
	
	DroneTank tankdemos[] = new DroneTank[NUM_OF_DRONES];
	PlayerTank tankplayers[] = new PlayerTank[NUM_OF_PLAYERS];
	DroneTank tankdrones[][] = new DroneTank[11][7];
	TankDescriptions alldescriptions[] = new TankDescriptions[NUM_OF_DRONES];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
	
	String levelschematics[][] = new String[11][7];
	
	Image[] icons = new Image[NUM_OF_DRONES];
	Image[] levelpics = new Image[NUM_OF_LEVELS];
	ImageView[] demopictures = new ImageView[NUM_OF_DRONES];
	ImageView[] levelpictures = new ImageView[NUM_OF_LEVELS];
	Rectangle[] demobackgrounds = new Rectangle[NUM_OF_DRONES];
	
	ImageView startbutton = new ImageView(new Image("Start_Button.png"));
	ImageView helpbutton = new ImageView(new Image("Help_Button.png"));
	ImageView backbutton = new ImageView(new Image("Back_Button.png"));
	
	Rectangle selectionbox = new Rectangle(STAGE_WIDTH + 10, 17, 170, 136);
	Rectangle namebase = new Rectangle(0, 25, 0, 34);
	Rectangle descriptionbase = new Rectangle(150, 515, 630, 65);
	Rectangle statsbase = new Rectangle(750, 175, 235, 150);
	
	Line redcrossA = new Line(225, 438, 485, 565);
	Line redcrossB = new Line(225, 565, 485, 438);
	
	Text nolevelselection = new Text(250, 420, "Please select a stage!");
	Text healthtext = new Text(808, 200, "Health:");
	Text damagetext = new Text(792, 250, "Damage:");
	Text firingspeedtext = new Text(760, 300, "Firing speed:");
 
	public void start(Stage primaryStage) throws Exception {
		startbutton.setLayoutX(230);
		startbutton.setLayoutY(443);
		helpbutton.setLayoutX(520);
		helpbutton.setLayoutY(443);
		backbutton.setLayoutX(880);
		backbutton.setLayoutY(539);
		backbutton.setVisible(false);
		
		selectionbox.setStroke(Color.GREEN);
		selectionbox.setFill(Color.TRANSPARENT);
		selectionbox.setStrokeWidth(6);
		selectionbox.setArcWidth(CORNER_ARC);
		selectionbox.setArcHeight(CORNER_ARC);
		
		namebase.setStroke(Color.BLACK);
		namebase.setFill(Color.GRAY);
		namebase.setStrokeWidth(4);
		namebase.setArcWidth(CORNER_ARC);
		namebase.setArcHeight(CORNER_ARC);
		namebase.setVisible(false);
		
		descriptionbase.setStroke(Color.BLACK);
		descriptionbase.setFill(Color.GRAY);
		descriptionbase.setStrokeWidth(4);
		descriptionbase.setArcWidth(CORNER_ARC);
		descriptionbase.setArcHeight(CORNER_ARC);
		descriptionbase.setVisible(false);
		
		statsbase.setStroke(Color.BLACK);
		statsbase.setFill(Color.GRAY);
		statsbase.setStrokeWidth(4);
		statsbase.setArcWidth(CORNER_ARC);
		statsbase.setArcHeight(CORNER_ARC);
		statsbase.setVisible(false);
		
		redcrossA.setStroke(Color.RED);
		redcrossB.setStroke(Color.RED);
		redcrossA.setStrokeWidth(5);
		redcrossB.setStrokeWidth(5);
		redcrossA.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossB.setStrokeLineCap(StrokeLineCap.ROUND);
		redcrossA.setVisible(false);
		redcrossB.setVisible(false);
		nolevelselection.setFont(Font.font("Verdana", 20));
		nolevelselection.setVisible(false);
		
		healthtext.setFont(Font.font("Verdana", 16));
		healthtext.setVisible(false);
		damagetext.setFont(Font.font("Verdana", 16));
		damagetext.setVisible(false);
		firingspeedtext.setFont(Font.font("Verdana", 16));
		firingspeedtext.setVisible(false);
		
		primaryStage.setTitle("Tank Takeover");
		Group root = new Group(startbutton, helpbutton, backbutton, selectionbox, namebase, descriptionbase, statsbase, redcrossA, redcrossB, nolevelselection, healthtext, damagetext, firingspeedtext);
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		
		for (i = 0; i < NUM_OF_DRONES; i++) {
			icons[i] = new Image("Class_" + i + "_Icon.png");
			demopictures[i] = new ImageView(icons[i]);
			demopictures[i].setLayoutX(50 - (icons[i].getWidth() / 2));
			demopictures[i].setLayoutY(50 + (i * 100) + (50 - (icons[i].getHeight() / 2)));
			demopictures[i].setVisible(false);
			
			demobackgrounds[i] = new Rectangle(20, 70 + (i * 100), 60, 60);
			demobackgrounds[i].setStroke(Color.BLACK);
			demobackgrounds[i].setFill(Color.TRANSPARENT);
			demobackgrounds[i].setStrokeWidth(5);
			demobackgrounds[i].toFront();
			demobackgrounds[i].setVisible(false);
			
			levelpics[i] = new Image("Level_" + (i + 1) + "_Picture.png");
			levelpictures[i] = new ImageView(levelpics[i]);
			levelpictures[i].setLayoutX(25 + (i * 200));
			levelpictures[i].setLayoutY(25);
			final int index = i;
			
			levelpictures[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					
					if (levelselection != index + 1) {
						levelselection = index + 1;
						selectionbox.setX(15 + (index * 200));
						redcrossA.setVisible(false);
						redcrossB.setVisible(false);
						nolevelselection.setVisible(false);
					} else {
						levelselection = 0;
						selectionbox.setX(STAGE_WIDTH + 10);
					}
					
				}
				
			});
			
			root.getChildren().addAll(demopictures[i], levelpictures[i], demobackgrounds[i]);
		}
		
		for (i = 0; i < 2; i++) {
			tankplayers[i] = new PlayerTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, new Circle(), new Line(), new Line(), new Line(), new Line(), new Text());
			tankplayers[i].create(i);
			tankplayers[i].setPlayerClass(0);
			tankplayers[i].setClassAttributes();
		}
		
		byte inbuf[] = new byte[20500];
		String name = new String();
		String descriptionline1 = new String();
		String descriptionline2 = new String();
		String health = new String();
		String damage = new String();
		String firingspeed = new String();
		
		try (DataInputStream dataIn = new DataInputStream(new FileInputStream("DroneDescriptions.txt"))) {
			dataIn.read(inbuf);
			String str = new String(inbuf); //converts array representing the characters of the text file to a string
			String[] linedescriptions = str.split("\n");
			
			for (i = 0; i < NUM_OF_DRONES; i++) {
				name = linedescriptions[i * 6].substring(9, linedescriptions[i * 6].length());
				descriptionline1 = linedescriptions[i * 6 + 1];
				descriptionline2 = linedescriptions[i * 6 + 2];
				health = linedescriptions[i * 6 + 3].substring(0, linedescriptions[i * 6 + 3].length()).trim();
				damage = linedescriptions[i * 6 + 4].substring(0, linedescriptions[i * 6 + 4].length()).trim();
				firingspeed = linedescriptions[i * 6 + 5].substring(0, linedescriptions[i * 6 + 5].length()).trim();
				
				alldescriptions[i] = new TankDescriptions(new Text(name), new Text(descriptionline1), new Text(descriptionline2), new Text(health), new Text(damage), new Text(firingspeed), new Rectangle(), new Rectangle(), new Rectangle());
				alldescriptions[i].create();
				alldescriptions[i].setDescriptionFont();
				alldescriptions[i].setStatsRects();
				alldescriptions[i].setDescriptionVisible(false);
				alldescriptions[i].name.setLayoutX(-(alldescriptions[i].name.getBoundsInLocal().getWidth() / 2));
				TankDescriptions tempdesc = alldescriptions[i];
				root.getChildren().addAll(tempdesc.name, tempdesc.descriptionline1, tempdesc.descriptionline2, tempdesc.health, tempdesc.damage, tempdesc.firingspeed, tempdesc.healthrect, tempdesc.damagerect, tempdesc.firingspeedrect);
			}
			
		} catch (IOException exc) {
			System.out.println("Read error.");
			return;
		}
		
		for (PlayerTank tankplayer : tankplayers) {
			root.getChildren().addAll(tankplayer.base, tankplayer.barrel, tankplayer.healthdisplay, tankplayer.sightline, tankplayer.sightlinereflect, tankplayer.sightlinereflect2);
		}
		
		for (i = 0; i < NUM_OF_DRONES; i++) {
			tankdemos[i] = new DroneTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, true, false, new Circle(), new Circle(), new Line(), new Rectangle(), new Rectangle(), new Rectangle(), new ImageView());
			tankdemos[i].create(5, 3);
			tankdemos[i].setDroneClass(i);
			tankdemos[i].setClassAttributes(icons[tankdemos[i].clazz]);
			tankdemos[i].setDroneVisible(false);
			root.getChildren().addAll(tankdemos[i].base, tankdemos[i].barrel, tankdemos[i].icon);
		}
		
		startbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (levelselection != 0) {
					inthelevel = true;
					startbutton.setVisible(false);
					helpbutton.setVisible(false);
					selectionbox.setVisible(false);
					
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.setPlayerVisible(true);
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						root.getChildren().remove(allprojectiles.get(i).body);
						allprojectiles.remove(i);
					}
					
					for (ImageView levelpicture : levelpictures) {
						levelpicture.setVisible(false);
					}
					
					byte inbuf[] = new byte[20500];
					
					try (DataInputStream dataIn = new DataInputStream(new FileInputStream("StageDronePlacement.txt"))) {
						dataIn.read(inbuf);
						String str = new String(inbuf);
						str = str.substring(str.indexOf("Level " + Integer.toString(levelselection)) + 10, str.length()).trim();
						
						for (i = 0; i < 7; i++) {
							
							for (j = 0; j < 11; j++) {
								levelschematics[j][i] = str.substring(0, 1);
								str = str.substring(1, str.length()).trim();
							}
							
						} //while
						
					} catch (IOException exc) {
						System.out.println("Read error.");
						return;
					}
					
					for (i = 0; i < 11; i++) {
						
						for (j = 0; j < 7; j++) {
							
							if (!(i == 0 && j == 3) && !(i == 10 && j == 3)) {
								tankdrones[i][j] = new DroneTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, new Circle(), new Circle(), new Line(), new Rectangle(), new Rectangle(), new Rectangle(), new ImageView());
								tankdrones[i][j].create(i, j);
								
								if (levelschematics[i][j].equals("-")) {
									tankdrones[i][j] = null;
								} else {
									tankdrones[i][j].setDroneClass(Integer.valueOf(levelschematics[i][j]));
									tankdrones[i][j].setClassAttributes(icons[tankdrones[i][j].clazz]);
									tankdrones[i][j].setDroneVisible(true);
									root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthbase, tankdrones[i][j].bluehealthbar, tankdrones[i][j].redhealthbar, tankdrones[i][j].icon);
									
									if (tankdrones[i][j].clazz == 4) {
										DroneTank tempdrone = tankdrones[i][j];
										Circle boundary = new Circle(tempdrone.getTrueX(), tempdrone.getTrueY(), tempdrone.bulletrange);
										boundary.setStroke(Color.BLACK);
										boundary.setFill(Color.TRANSPARENT);
										root.getChildren().add(boundary);
										boundary.toBack();
									}
									
								}
								
							}
							
						}
						
					}
					
				} else {
					redcrossA.setVisible(true);
					redcrossB.setVisible(true);
					nolevelselection.setVisible(true);
				}
				
			}
			
		});
		
		helpbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				inhelp = true;
				startbutton.setVisible(false);
				helpbutton.setVisible(false);
				selectionbox.setVisible(false);
				redcrossA.setVisible(false);
				redcrossB.setVisible(false);
				nolevelselection.setVisible(false);
				backbutton.setVisible(true);
				
				for (PlayerTank tankplayer : tankplayers) {
					tankplayer.setPlayerVisible(false);
				}
				
				for (i = allprojectiles.size() - 1; i >= 0; i--) {
					root.getChildren().remove(allprojectiles.get(i).body);
					allprojectiles.remove(i);
				}
				
				for (i = 0; i < NUM_OF_DRONES; i++) {
					levelpictures[i].setVisible(false);
					demopictures[i].setVisible(true);
					demobackgrounds[i].setVisible(true);
					final int index = i;
					
					demobackgrounds[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						
						public void handle(MouseEvent event) {
							demotankselection = index;
							
							for (j = 0; j < NUM_OF_DRONES; j++) {
								tankdemos[j].setDroneVisible(j == index ? true : false);
								alldescriptions[j].setDescriptionVisible(j == index ? true : false);
								namebase.setVisible(true);
								descriptionbase.setVisible(true);
								statsbase.setVisible(true);
								healthtext.setVisible(true);
								damagetext.setVisible(true);
								firingspeedtext.setVisible(true);
								namebase.setX(500 - (alldescriptions[index].name.getBoundsInLocal().getWidth() / 2) - 10);
								namebase.setWidth(alldescriptions[index].name.getBoundsInLocal().getWidth() + 20);
							}
							
						}
						
					});
					
				}
				
				backbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					
					public void handle(MouseEvent event) {
						inhelp = false;
						backbutton.setVisible(false);
						namebase.setVisible(false);
						descriptionbase.setVisible(false);
						statsbase.setVisible(false);
						healthtext.setVisible(false);
						damagetext.setVisible(false);
						firingspeedtext.setVisible(false);
						startbutton.setVisible(true);
						helpbutton.setVisible(true);
						selectionbox.setVisible(true);
						
						for (PlayerTank tankplayer : tankplayers) {
							tankplayer.setPlayerVisible(true);
						}
						
						for (i = 0; i < NUM_OF_DRONES; i++) {
							demopictures[i].setVisible(false);
							demobackgrounds[i].setVisible(false);
							tankdemos[i].setDroneVisible(false);
							alldescriptions[i].setDescriptionVisible(false);
							levelpictures[i].setVisible(true);
						}
						
						for (i = allprojectiles.size() - 1; i >= 0; i--) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						}
						
						demotankselection = -1;
						allprojectiles.clear();
						event.consume();
					}
					
				});
				
			}
			
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (!inhelp) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = true;
							tankplayers[0].lastpressed = -1;
							break;
						case D:
							tankplayers[0].turnright = true;
							tankplayers[0].lastpressed = 1;
							break;
						case W:
							tankplayers[0].shooting = true;
							break;
						case J:
							tankplayers[1].turnleft = true;
							tankplayers[1].lastpressed = -1;
							break;
						case L:
							tankplayers[1].turnright = true;
							tankplayers[1].lastpressed = 1;
							break;
						case I:
							tankplayers[1].shooting = true;
							break;
					}
					
				}
				
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (!inhelp) {
					
					switch (event.getCode()) {
						case A:
							tankplayers[0].turnleft = false;
							break;
						case D:
							tankplayers[0].turnright = false;
							break;
						case W:
							tankplayers[0].shooting = false;
							break;
						case J:
							tankplayers[1].turnleft = false;
							break;
						case L:
							tankplayers[1].turnright = false;
							break;
						case I:
							tankplayers[1].shooting = false;
							break;
					}
					
					if (!inthelevel) {
						
						switch (event.getCode()) {
							case Q:
								tankplayers[0].clazz = Math.floorMod(tankplayers[0].clazz - 1, 4);
								tankplayers[0].setClassAttributes();
								break;
							case E:
								tankplayers[0].clazz = Math.floorMod(tankplayers[0].clazz + 1, 4);
								tankplayers[0].setClassAttributes();
								break;
							case U:
								tankplayers[1].clazz = Math.floorMod(tankplayers[1].clazz - 1, 4);
								tankplayers[1].setClassAttributes();
								break;
							case O:
								tankplayers[1].clazz = Math.floorMod(tankplayers[1].clazz + 1, 4);
								tankplayers[1].setClassAttributes();
								break;
						}
						
					}
					
				}
				
			}
			
		});
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
			
            public void handle(long now) {
				
				if (inthelevel) {
				
					//Player tank logic
					for (i = 0; i < NUM_OF_PLAYERS; i++) {
						tankplayers[i].moveLR(); //takes keyboard inputs to turn the players
						tankplayers[i].updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayers[i].updateHealthDisplay(); //updates the numerical health display
						tankplayers[i].updateSightLine(); //updates the graphical appearance of the sight line to follow the angle
						
						/* Shooting bullets logic
						 * A method decrements a variable until it reaches 0 and checks for this
						 * Once this happens:
							* A bullet is added to the object ArrayList allprojectiles
							* This bullet is given the properties of the tank that fired it, which are:
								* angle
								* size
								* penetration
								* damage
								* origin point
								* team that it's on
							* The variable is then reset to the max reload timer
						 */
						if (tankplayers[i].fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							PlayerTank tempplayer = tankplayers[i];
							allprojectiles.get(allprojectiles.size() - 1).create(i, tempplayer.getTrueX(), tempplayer.getTrueY(), tempplayer.getBarrelLength(), tempplayer.angle, tempplayer.bulletspeed, tempplayer.bulletsize, tempplayer.damage, tempplayer.bulletpenetration, 99999);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankplayers[i].moveToFront();
							System.out.println(allprojectiles.size());
						}
						
					}
					
					//Drone tank logic
					for (DroneTank[] tankdronerow : tankdrones) {
					
						for (DroneTank tankdrone : tankdronerow) {
							
							if (tankdrone != null) {
								tankdrone.updateHealthDisplay(); //updates the graphical health bar display
								tankdrone.displayHealthBar(); //displays the health bar if the tank was damaged 60 seconds ago
								
								/* Targeting enemy/neutral drone logic
								 * For the tanks that aren't guards or neutral:
									* If the tank is not currently locked onto a target and shooting:
										* If the tank hasn't found a new target, then it will run a method to determine the closest tank it should target
										* Otherwise, the tank will use the coordinates of its targeted tank (represented by the parameter "activetarget") to change its angle until it's facing its target
										* The tank will only target the enemy player once all other tanks have been converted, and special parameters associated with the coordinates of the player tanks are passed if the attacking tank is targeting the enemy player
										* After all this targeting logic, the barrel is graphically updated to follow its angle
									* If the tank is a guard and not locked onto a target and shooting:
										* If the guard hasn't found a new bullet to gun down, it will run a method to determine the closest bullet that is within the 200 pixel range and is from the enemy team
										* The logic for guards differs in that its angle used to attack an enemy bullet is updated every time it attacks
									* Finally, if the tank can release a bullet, a bullet will be added to allprojectiles that inherits the properties of:
										* angle
										* size
										* penetration
										* damage
										* origin point
										* team that it's on
								 */
								if (tankdrone.clazz != 4) {
								
									if (!tankdrone.shooting && tankdrone.owner != -1) {
										
										if (!tankdrone.targeting) {
											tankdrone.findNewTarget(tankdrones, tankplayers);
										} else {
											
											if (tankdrone.activetarget != 3 && tankdrone.activetarget != 73) {
												DroneTank tempdrone = tankdrones[tankdrone.activetarget / 7][tankdrone.activetarget % 7];
												tankdrone.turnToTarget(tempdrone.centerx, tempdrone.centery);
											} else {
												
												if (tankdrone.activetarget == 3) {
													tankdrone.turnToTarget(tankplayers[0].centerx, tankplayers[0].centery);
												} else {
													tankdrone.turnToTarget(tankplayers[1].centerx, tankplayers[1].centery);
												}
												
											}
											
											tankdrone.updateBarrel();
										}
										
									}
									
								} else if (tankdrone.owner != -1) {
									
									if (!tankdrone.targeting) {
										tankdrone.findNewTargetGuard(allprojectiles);
									}
									
									if (tankdrone.targeting) {
										
										if (tankdrone.activetarget < allprojectiles.size()) {
											TankProjectile tempbullet = allprojectiles.get(tankdrone.activetarget);
											tankdrone.turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
											tankdrone.updateBarrel();
										} else {
											tankdrone.targeting = false;
										}
									}
									
								}
								
								if (tankdrone.fireBullet()) {
									allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
									allprojectiles.get(allprojectiles.size() - 1).create(tankdrone.owner, tankdrone.getTrueX(), tankdrone.getTrueY(), tankdrone.getBarrelLength(), tankdrone.angle, tankdrone.bulletspeed, tankdrone.bulletsize, tankdrone.damage, tankdrone.bulletpenetration, tankdrone.bulletrange);
									root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
									tankdrone.moveToFront();
									
									if (tankdrone.clazz == 4) {
										tankdrone.findNewTargetGuard(allprojectiles);
									}
									
								}
								
							}
							
						}
						
					}
					
					//Collision detection logic for bullets
					for (i = 0; i < allprojectiles.size(); i++) {
						allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
						allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						TankProjectile tempbullet = allprojectiles.get(i);
					
						/* Checking against various attributes to determine if something needs to be done about the bullet
						 * First, the bullet runs a method to determine if it's off the stage
						 * If this is true, given the stage width and height:
							* The bullet is removed from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * Then, the bullet runs a method to determine if it's touching a drone tank
						 * If this is true:
							* The drone tank is deducted health, taking into account its current state (which team it's on, if any) and the bullet damage
							* If the bullet has caused the drone to undergo a team shift, its traits are modified to take into account is new team (whether it's been converted or set back to neutral):
								* Within this method, all drones that were targeting this tank have their targeting reset
							* The bullet is removed from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * The, the bullet runs the same method, but using the player tank traits, to determine if it's touched an enemy player
						 * If this is true:
							* The player is deducted health according to the damage of the bullet
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 * Finally, the bullet is checked if it is intersecting another bullet of the other team
						 * If this is true:
							* Some logic is run to remove both bullets from the ArrayList allprojectiles
							* Any guards that may have been targeting the bullet have their targeting reset to find a new bullet to attack
						 */
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							
							if (allprojectiles.get(i).damage != 2) {
								root.getChildren().remove(allprojectiles.get(i).body);
								allprojectiles.get(i).owner = TB_ELIMINATED;
								
								for (DroneTank[] tankdronerow : tankdrones) {
									
									for (DroneTank tankdrone : tankdronerow) {
										
										if (tankdrone != null && tankdrone.clazz == 4) {
											tankdrone.resetTargeting();
										}										
									}
									
								}
								
							} else {
								allprojectiles.get(i).body.setVisible(true);
								
								if (allprojectiles.get(i).centerx < 0 || allprojectiles.get(i).centerx > STAGE_WIDTH) {
									allprojectiles.get(i).angle = 360 - allprojectiles.get(i).angle; 
								} else if (allprojectiles.get(i).centery < 0 || allprojectiles.get(i).centery > STAGE_HEIGHT) {
									allprojectiles.get(i).angle = 180 - allprojectiles.get(i).angle; 
								}
								
								allprojectiles.get(i).updatePosition();
							}
							
						}
						
						if (allprojectiles.get(i).outOfRange() && allprojectiles.get(i).owner != 4) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.get(i).owner = TB_ELIMINATED;
						}
						
						for (j = 0; j < 77 && allprojectiles.get(i).owner != TB_ELIMINATED; j++) {
							
							if (tankdrones[j / 7][j % 7] != null && allprojectiles.get(i).intersectsNonGuard(tankdrones[j / 7][j % 7].base, tankdrones[j / 7][j % 7].owner)) {
								tankdrones[j / 7][j % 7].removeHealth(tempbullet.damage, tempbullet.owner);
								tankdrones[j / 7][j % 7].healthBelowZeroActivites(tempbullet.owner, tankdrones, j);
								root.getChildren().remove(tempbullet.body);
								allprojectiles.get(i).owner = TB_ELIMINATED;
								
								for (DroneTank[] tankdronerow : tankdrones) {
									
									for (DroneTank tankdrone : tankdronerow) {
										
										if (tankdrone != null && tankdrone.clazz == 4) {
											tankdrone.resetTargeting();
										}										
									}
									
								}
								
							}
							
						}
						
						for (j = 0; j < NUM_OF_PLAYERS && allprojectiles.get(i).owner != TB_ELIMINATED; j++) {
							
							if (allprojectiles.get(i).intersectsNonGuard(tankplayers[j].base, j)) {
								tankplayers[j].currenthealth -= allprojectiles.get(i).damage;
								root.getChildren().remove(allprojectiles.get(i).body);
								allprojectiles.get(i).owner = TB_ELIMINATED;	
							}
							
						}
						
						for (j = 0; j < allprojectiles.size() && allprojectiles.get(i).owner != TB_ELIMINATED; j++) {
							
							if (allprojectiles.get(j).owner != allprojectiles.get(i).owner && allprojectiles.get(j).owner != TB_ELIMINATED) {
								
								if (allprojectiles.get(i).body.getBoundsInLocal().intersects(allprojectiles.get(j).body.getBoundsInLocal())) {
									int temppenetration = allprojectiles.get(j).penetration;
									allprojectiles.get(j).penetration -= allprojectiles.get(i).penetration;
									allprojectiles.get(i).penetration -= temppenetration;
									
									if (allprojectiles.get(i).penetration <= 0) {
										allprojectiles.get(i).body.setVisible(false);
										root.getChildren().remove(allprojectiles.get(i).body);
										allprojectiles.get(i).owner = TB_ELIMINATED;
									}
									
									if (allprojectiles.get(j).penetration <= 0) {
										allprojectiles.get(j).body.setVisible(false);
										root.getChildren().remove(allprojectiles.get(j).body);
										allprojectiles.get(j).owner = TB_ELIMINATED;
									}
									
								}
								
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == TB_ELIMINATED) {
							allprojectiles.remove(i);
						}
						
					}
					
				} else if (inhelp) {
					demoangle = (demoangle + 1) % 360;
					
					for (i = 0; i < NUM_OF_DRONES; i++) {
						tankdemos[i].angle = demoangle;
						tankdemos[i].updateBarrel();
						
						if (tankdemos[i].fireBullet() && i == demotankselection) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							DroneTank tempdrone = tankdemos[i];
							allprojectiles.get(allprojectiles.size() - 1).create(tempdrone.owner, tempdrone.getTrueX(), tempdrone.getTrueY(), tempdrone.getBarrelLength(), tempdrone.angle, tempdrone.bulletspeed, tempdrone.bulletsize, tempdrone.damage, tempdrone.bulletpenetration, tempdrone.bulletrange);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankdemos[i].moveToFront();
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
						allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						}
					
					}
					
				} else {
					
					for (i = 0; i < NUM_OF_PLAYERS; i++) {
						tankplayers[i].moveLR(); //takes keyboard inputs to turn the players
						tankplayers[i].updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayers[i].updateHealthDisplay(); //updates the numerical health display
						tankplayers[i].sightline.setVisible(false);
						tankplayers[i].sightlinereflect.setVisible(false);
						tankplayers[i].sightlinereflect2.setVisible(false);
						
						if (tankplayers[i].fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Circle()));
							PlayerTank tempplayer = tankplayers[i];
							allprojectiles.get(allprojectiles.size() - 1).create(i, tempplayer.getTrueX(), tempplayer.getTrueY(), tempplayer.getBarrelLength(), tempplayer.angle, tempplayer.bulletspeed, tempplayer.bulletsize, tempplayer.damage, tempplayer.bulletpenetration, 99999);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankplayers[i].moveToFront();
						}
						
					}
					
					for (i = 0; i < allprojectiles.size(); i++) {
						allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
						allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						TankProjectile tempbullet = allprojectiles.get(i);
					
						if (allprojectiles.get(i).offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							
							if (allprojectiles.get(i).damage != 2) {
								root.getChildren().remove(allprojectiles.get(i).body);
								allprojectiles.get(i).owner = TB_ELIMINATED;
							} else {
								allprojectiles.get(i).body.setVisible(true);
								
								if (allprojectiles.get(i).centerx < 0 || allprojectiles.get(i).centerx > STAGE_WIDTH) {
									allprojectiles.get(i).angle = 360 - allprojectiles.get(i).angle; 
								} else if (allprojectiles.get(i).centery < 0 || allprojectiles.get(i).centery > STAGE_HEIGHT) {
									allprojectiles.get(i).angle = 180 - allprojectiles.get(i).angle; 
								}
								
								allprojectiles.get(i).updatePosition();
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == TB_ELIMINATED) {
							allprojectiles.remove(i);
						}
						
					}
					
				}
				
			}
			
		};
		timer.start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
}