import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Slider;
import javafx.scene.Cursor;
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
	public static int NUM_OF_PLAYER_CLASSES = 5;
	public static int NUM_OF_DRONES = 5;
	public static int NUM_OF_LEVELS = 5;
	public static int TB_ELIMINATED = -2;
	public static int CORNER_ARC = 20;
	
	int i, j, k, levelselection, respawntimer;
	int spiralindex = 0;
	int demotankselection = -1;
	double mouseposx, mouseposy;
	boolean inthelevel, inhelp, mouseonscreen, mousedragged;
	boolean isovernode = false;
	boolean guardboundaryvisible = true;
	
	int autoangle = 0;
	
	DroneTank tankdemos[] = new DroneTank[NUM_OF_DRONES];
	PlayerTank tankplayers[] = new PlayerTank[NUM_OF_PLAYERS];
	DroneTank tankdrones[][] = new DroneTank[11][7];
	TankDescriptions alldescriptions[] = new TankDescriptions[NUM_OF_DRONES];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
	
	String levelschematics[][] = new String[11][7];
	
	Image[] icons = new Image[NUM_OF_DRONES];
	ImageView[] demopictures = new ImageView[NUM_OF_DRONES];
	ImageView[] levelpictures = new ImageView[NUM_OF_LEVELS];
	Rectangle[] demobackgrounds = new Rectangle[NUM_OF_DRONES];
	HelpType[] enumtypeofprojectile = new HelpType[5];
	Text[] typeofprojectile = new Text[5];
	
	ImageView startbutton = new ImageView(new Image("Buttons/Start.png"));
	ImageView helpbutton = new ImageView(new Image("Buttons/Help.png"));
	ImageView backbutton = new ImageView(new Image("Buttons/Back.png"));
	ImageView deathicon = new ImageView(new Image("Death_Icon.png"));
	
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
	Text victorytext = new Text(170, 250, "Player ");
	
	Color lightgray = Color.rgb(195, 195, 195);
	AnimationTimer timer = null;
	Random random = new Random();
	HelpType typeselected = HelpType.CIRCLE;
	
	public void start(Stage primaryStage) throws Exception {
		startbutton.setLayoutX(230);
		startbutton.setLayoutY(443);
		helpbutton.setLayoutX(520);
		helpbutton.setLayoutY(443);
		backbutton.setLayoutX(880);
		backbutton.setLayoutY(539);
		backbutton.setVisible(false);
		deathicon.setVisible(false);
		
		selectionbox.setStroke(Color.GREEN);
		selectionbox.setFill(Color.TRANSPARENT);
		selectionbox.setStrokeWidth(6);
		selectionbox.setArcWidth(CORNER_ARC);
		selectionbox.setArcHeight(CORNER_ARC);
		
		namebase.setStroke(Color.BLACK);
		namebase.setFill(lightgray);
		namebase.setStrokeWidth(4);
		namebase.setArcWidth(CORNER_ARC);
		namebase.setArcHeight(CORNER_ARC);
		namebase.setVisible(false);
		
		descriptionbase.setStroke(Color.BLACK);
		descriptionbase.setFill(lightgray);
		descriptionbase.setStrokeWidth(4);
		descriptionbase.setArcWidth(CORNER_ARC);
		descriptionbase.setArcHeight(CORNER_ARC);
		descriptionbase.setVisible(false);
		
		statsbase.setStroke(Color.BLACK);
		statsbase.setFill(lightgray);
		statsbase.setStrokeWidth(4);
		statsbase.setArcWidth(CORNER_ARC);
		statsbase.setArcHeight(CORNER_ARC);
		statsbase.setVisible(false);
		
		redcrossA.setStroke(Color.RED);
		redcrossB.setStroke(Color.RED);
		redcrossA.setStrokeWidth(5);
		redcrossB.setStrokeWidth(5);
		redcrossA.setStrokeLineCap(StrokeLineCap.ROUND);
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
		
		victorytext.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		victorytext.setStroke(Color.BLACK);
		victorytext.setStrokeWidth(5);
		victorytext.setVisible(false);
		
		Slider autosplitslider = new Slider(1, 6, 1);
		autosplitslider.setLayoutX(750);
		autosplitslider.setLayoutY(130);
		autosplitslider.setShowTickMarks(true);
		autosplitslider.setShowTickLabels(true);
		autosplitslider.setSnapToTicks(true);
		autosplitslider.setMajorTickUnit(1);
		autosplitslider.setMinorTickCount(10);
		autosplitslider.setBlockIncrement(0.1);
		autosplitslider.setVisible(false);
		
		primaryStage.setTitle("Tank Takeover");
		Group root = new Group(startbutton, helpbutton, backbutton, deathicon, selectionbox, namebase, descriptionbase, statsbase, redcrossA, redcrossB, nolevelselection, healthtext, damagetext, firingspeedtext, victorytext, autosplitslider);
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		scene.setCursor(Cursor.CROSSHAIR);
		
		for (i = 0; i < 5; i++) {
			String tempstring = HelpType.names()[i];
			typeofprojectile[i] = new Text(900, i * 30 + 30, tempstring.substring(0, 1) + tempstring.substring(1).toLowerCase());
			typeofprojectile[i].setFont(Font.font("Verdana", 20));
			typeofprojectile[i].setVisible(false);
			root.getChildren().add(typeofprojectile[i]);
			final int index = i;
			
			typeofprojectile[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					typeselected = HelpType.valueOf(typeofprojectile[index].getText().toString().toUpperCase());
					autosplitslider.setVisible(typeselected == HelpType.AUTO);
					
					for (int j = 0; j < 5; j++)
						typeofprojectile[j].setFill(j != index ? Color.BLACK : Color.GREEN);
					
				}
				
			});
			
			typeofprojectile[i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					isovernode = true;
				}
				
			});
			
			typeofprojectile[i].addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					isovernode = false;
				}
				
			});
			
		}
		
		for (i = 0; i < NUM_OF_DRONES; i++) {
			icons[i] = new Image("Class_Icons/" + i + ".png");
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
			
			levelpictures[i] = new ImageView(new Image("Level_Pictures/" + (i + 1) + ".png"));
			levelpictures[i].setLayoutX(25 + (i * 200));
			levelpictures[i].setLayoutY(25);
			final int index = i;
			
			levelpictures[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					
					if (levelselection != index + 1) {
						levelselection = index + 1;
						selectionbox.setX(index * 200 + 15);
						redcrossA.setVisible(false);
						redcrossB.setVisible(false);
						nolevelselection.setVisible(false);
					} else {
						levelselection = 0;
						selectionbox.setX(STAGE_WIDTH + 10);
					}
					
				}
				
			});
			
			demobackgrounds[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					demotankselection = index == demotankselection ? -1 : index;
					boolean tempvisible = demotankselection != -1;
					
					namebase.setVisible(tempvisible);
					descriptionbase.setVisible(tempvisible);
					statsbase.setVisible(tempvisible);
					healthtext.setVisible(tempvisible);
					damagetext.setVisible(tempvisible);
					firingspeedtext.setVisible(tempvisible);
					namebase.setX(500 - (alldescriptions[index].name.getLayoutBounds().getWidth() / 2) - 10);
					namebase.setWidth(alldescriptions[index].name.getLayoutBounds().getWidth() + 20);
					
					for (j = 0; j < NUM_OF_DRONES; j++) {
						
						if (j == index && demotankselection != -1) {
							demobackgrounds[j].setStroke(Color.GREEN);
							tankdemos[j].setDroneVisible(true);
							
							if (tankdemos[j].clazz == 4 && guardboundaryvisible) {
								tankdemos[j].rangeboundary.setRadius(tankdemos[j].bulletrange);
								tankdemos[j].rangeboundary.setVisible(true);
							}
							
						} else {
							demobackgrounds[j].setStroke(Color.BLACK);
							tankdemos[j].setDroneVisible(false);
							tankdemos[j].rangeboundary.setVisible(false);
						}
						
						alldescriptions[j].setDescriptionVisible((j == index && demotankselection != -1) ? true : false);
					}
					
				}
				
			});
			
			demobackgrounds[i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					isovernode = true;
				}
				
			});
			
			demobackgrounds[i].addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					isovernode = false;
				}
				
			});
			
			root.getChildren().addAll(demopictures[i], levelpictures[i], demobackgrounds[i]);
		}
		
		for (i = 0; i < NUM_OF_PLAYERS; i++) {
			tankplayers[i] = new PlayerTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, false, new Circle(), new Line(), new Line(), new Line(), new Line(), new Text());
			tankplayers[i].create(i);
			tankplayers[i].setPlayerClass(0, NUM_OF_PLAYERS);
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
			}
			
			for (TankDescriptions singledesc : alldescriptions) {
				singledesc.create();
				singledesc.setDescriptionFont();
				singledesc.setStatsRects();
				singledesc.setDescriptionVisible(false);
				singledesc.name.setLayoutX(-(singledesc.name.getLayoutBounds().getWidth() / 2));
				root.getChildren().addAll(singledesc.name, singledesc.descriptionline1, singledesc.descriptionline2, singledesc.health, singledesc.damage, singledesc.firingspeed, singledesc.healthrect, singledesc.damagerect, singledesc.firingspeedrect);
			}
			
		} catch (IOException exc) {
			System.out.println("Read error.");
			return;
		}
		
		for (PlayerTank tankplayer : tankplayers)
			root.getChildren().addAll(tankplayer.base, tankplayer.barrel, tankplayer.healthdisplay, tankplayer.sightline, tankplayer.sightlinereflect, tankplayer.sightlinereflect2);
		
		for (i = 0; i < 5; i++) {
			tankdemos[i] = new DroneTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, true, false, new Circle(), new Circle(), new Circle(), new Line(), new Rectangle(), new Rectangle(), new Rectangle(), new ImageView());
			tankdemos[i].create(5, 3);
			tankdemos[i].setDroneClass(i);
			tankdemos[i].setClassAttributes(icons[tankdemos[i].clazz]);
			tankdemos[i].setDroneVisible(false);
			root.getChildren().addAll(tankdemos[i].base, tankdemos[i].barrel, tankdemos[i].icon, tankdemos[i].rangeboundary);
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
						tankplayer.setClassAttributes();
						tankplayer.setPlayerVisible(true);
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						root.getChildren().remove(allprojectiles.get(i).body);
						allprojectiles.remove(i);
					}
					
					for (ImageView levelpicture : levelpictures)
						levelpicture.setVisible(false);
					
					byte inbuf[] = new byte[20500];
					
					try (DataInputStream dataIn = new DataInputStream(new FileInputStream("StageDronePlacement.txt"))) {
						dataIn.read(inbuf);
						String str = new String(inbuf);
						str = str.substring(str.indexOf("Level " + levelselection) + 10, str.length()).trim();
						
						for (i = 0; i < 7; i++) {
							
							for (j = 0; j < 11; j++) {
								levelschematics[j][i] = str.substring(0, 1);
								str = str.substring(1, str.length()).trim();
							}
							
						}
						
					} catch (IOException exc) {
						System.out.println("Read error.");
						return;
					}
					
					for (i = 0; i < 11; i++) {
						
						for (j = 0; j < 7; j++) {
							
							if (levelschematics[i][j].equals("-"))
								tankdrones[i][j] = null;
							else {
								tankdrones[i][j] = new DroneTank(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, new Circle(), new Circle(), new Circle(), new Line(), new Rectangle(), new Rectangle(), new Rectangle(), new ImageView());
								tankdrones[i][j].create(i, j);
								tankdrones[i][j].setDroneClass(Integer.valueOf(levelschematics[i][j]));
								tankdrones[i][j].setClassAttributes(icons[tankdrones[i][j].clazz]);
								tankdrones[i][j].setDroneVisible(true);
								root.getChildren().addAll(tankdrones[i][j].base, tankdrones[i][j].ownerflag, tankdrones[i][j].barrel, tankdrones[i][j].healthbase, tankdrones[i][j].bluehealthbar, tankdrones[i][j].redhealthbar, tankdrones[i][j].icon);
								
								if (tankdrones[i][j].clazz == 4 && guardboundaryvisible) {
									tankdrones[i][j].rangeboundary.setRadius(tankdrones[i][j].bulletrange);
									tankdrones[i][j].rangeboundary.setVisible(true);
									root.getChildren().add(tankdrones[i][j].rangeboundary);
								}
								
							}
							
						}
						
					}
					
					scene.setCursor(Cursor.NONE);
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
				isovernode = false;
				startbutton.setVisible(false);
				helpbutton.setVisible(false);
				selectionbox.setVisible(false);
				redcrossA.setVisible(false);
				redcrossB.setVisible(false);
				nolevelselection.setVisible(false);
				backbutton.setVisible(true);
				
				if (typeselected == HelpType.AUTO)
					autosplitslider.setVisible(true);
				
				for (Text typeofbullet : typeofprojectile)
					typeofbullet.setVisible(true);
				for (PlayerTank tankplayer : tankplayers)
					tankplayer.setPlayerVisible(false);
				
				for (i = allprojectiles.size() - 1; i >= 0; i--) {
					root.getChildren().remove(allprojectiles.get(i).body);
					allprojectiles.remove(i);
				}
				
				for (i = 0; i < NUM_OF_DRONES; i++) {
					levelpictures[i].setVisible(false);
					demopictures[i].setVisible(true);
					demobackgrounds[i].setVisible(true);
				}
				
			}
			
		});
		
		backbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (inhelp) {
					inhelp = false;
					backbutton.setVisible(false);
					namebase.setVisible(false);
					descriptionbase.setVisible(false);
					statsbase.setVisible(false);
					healthtext.setVisible(false);
					damagetext.setVisible(false);
					firingspeedtext.setVisible(false);
					autosplitslider.setVisible(false);
					startbutton.setVisible(true);
					helpbutton.setVisible(true);
					selectionbox.setVisible(true);
					
					for (Text typeofbullet : typeofprojectile)
						typeofbullet.setVisible(false);
					for (PlayerTank tankplayer : tankplayers)
						tankplayer.setPlayerVisible(true);
					
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
				} else if (inthelevel) {
					inthelevel = false;
					backbutton.setVisible(false);
					deathicon.setVisible(false);
					victorytext.setVisible(false);
					startbutton.setVisible(true);
					helpbutton.setVisible(true);
					selectionbox.setVisible(true);
					
					for (i = 0; i < NUM_OF_PLAYERS; i++)
						tankplayers[i].reset(i);
					for (TankProjectile singlebullet : allprojectiles)
						root.getChildren().remove(singlebullet.body);
					for (ImageView levelpicture : levelpictures)
						levelpicture.setVisible(true);
					
					for (DroneTank[] tankdronerow : tankdrones) {
					for (DroneTank tankdrone : tankdronerow) {
						
						if (tankdrone != null) {
							root.getChildren().removeAll(tankdrone.base, tankdrone.ownerflag, tankdrone.rangeboundary, tankdrone.barrel, tankdrone.healthbase, tankdrone.bluehealthbar, tankdrone.redhealthbar, tankdrone.icon);
							tankdrone = null;
						}
						
					}
					}
					
					victorytext.setText("Player ");
					levelselection = 0;
					selectionbox.setX(STAGE_WIDTH + 10);
					allprojectiles.clear();
					timer.start();
				}
				
			}
			
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				if (!inhelp)
					for (PlayerTank tankplayer : tankplayers)
						tankplayer.LRandShooting(event.getCode().toString());
				//key presses are accepted if either in the game or on the start screen (which contains the demo tanks)
				//the key being pressed is passed to a method for each player that determines whether to start turning them left, right, or make them shoot
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			
			public void handle(KeyEvent event) {
				
				if (!inhelp) {
					boolean[] megamindplayers = new boolean[2];
					
					for (i = 0; i < NUM_OF_PLAYERS; i++) {
						megamindplayers[i] = tankplayers[i].stopLRandShooting(event.getCode().toString());
						
						if (megamindplayers[i]) {
							
							for (DroneTank[] tankdronerow : tankdrones) {
							for (DroneTank tankdrone : tankdronerow) {
								if (tankdrone != null && tankdrone.owner == i && tankdrone.clazz != 4)
									tankdrone.resetTargeting();
							}
							}
							
						}
						
					}
					
					if (!inthelevel)
						for (PlayerTank tankplayer : tankplayers)
							tankplayer.switchClass(event.getCode().toString(), NUM_OF_PLAYER_CLASSES);
					
				}
				
			}
			
		});
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (inhelp) {
					mouseonscreen = true;
					mouseposx = event.getX();
					mouseposy = event.getY();
				}
				
			}
			
		});
		
		scene.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				if (inhelp)
					mouseonscreen = false;
			}
			
		});
		
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (inhelp) {
					mousedragged = true;
					mouseposx = event.getX();
					mouseposy = event.getY();
				}
				
			}
			
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (inhelp && typeselected != HelpType.CIRCLE) {
					mousedragged = true;
					mouseposx = event.getX();
					mouseposy = event.getY();
				}
				
			}
			
		});
		
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (inhelp) {
					mousedragged = false;
				}
				
			}
			
		});
		
		timer = new AnimationTimer() {
			@Override
			
			public void handle(long now) {
				
				if (inthelevel) {
					
					//Player tank logic
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.moveLR(); //takes keyboard inputs to turn the players
						tankplayer.updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayer.updateHealthDisplay(); //updates the numerical health display
						tankplayer.updateSightLine(); //updates the graphical appearance of the sight line to follow the angle
						
						if (tankplayer.currenthealth <= 0) {
							tankplayer.healthdisplay.setVisible(false);
							deathicon.setVisible(true);
							deathicon.relocate(tankplayer.getTrueX() - 11, tankplayer.getTrueY() - 11);
							deathicon.toFront();
							victorytext.setVisible(true);
							victorytext.setText(victorytext.getText() + (tankplayer.team == 0 ? "2" : "1") + " Wins!");
							victorytext.setFill(tankplayer.team == 0 ? Color.RED : Color.BLUE);
							victorytext.setX((STAGE_WIDTH - victorytext.getLayoutBounds().getWidth()) / 2);
							victorytext.toFront();
							backbutton.setVisible(true);
							backbutton.toFront();
							scene.setCursor(Cursor.CROSSHAIR);
							timer.stop();
						}
						
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
						if (tankplayer.fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(tankplayer.team, tankplayer.getTrueX(), tankplayer.getTrueY(), tankplayer.team == 0 ? 3 : 73, tankplayer.getBarrelLength(), tankplayer.angle, tankplayer.bulletspeed, tankplayer.bulletsize, tankplayer.damage, tankplayer.bulletpenetration, 99999, tankplayer.maxricochet, tankplayer.ricochet, 0);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankplayer.moveToFront();
						}
						
					}
					
					//Drone tank logic
					for (DroneTank[] tankdronerow : tankdrones) {
					for (DroneTank tankdrone : tankdronerow) {
						
						if (tankdrone != null) {
							tankdrone.updateHealthDisplay(); //updates the graphical health bar display
							tankdrone.displayHealthBar(); //displays the health bar if the tank was damaged 60 frames ago
							
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
								* If the tank is neutral and currently holds an active target:
									* The tank will set its attacking parameters to default if its active target is currently neutral
									* 
								* Finally, if the tank can release a bullet, a bullet will be added to allprojectiles that inherits the properties of:
									* angle
									* size
									* penetration
									* damage
									* origin point
									* team that it's on
							 */
							if (tankdrone.clazz != 4 && tankdrone.owner != -1) {
								//not guard and not neutral
								
								if (!tankdrone.shooting) {
									//not locked onto a target and shooting
									
									if (!tankdrone.targeting) tankdrone.findNewTarget(tankdrones);
									//finds new target if it doesn't have one
									else {
										
										if (tankdrone.activetarget != 3 && tankdrone.activetarget != 73) {
											//not targeting either player
											DroneTank tempdrone = tankdrones[tankdrone.activetarget / 7][tankdrone.activetarget % 7];
											tankdrone.turnToTarget(tempdrone.centerx, tempdrone.centery); //turns to target if not locked onto it
										} else {
											PlayerTank tempplayer = tankplayers[tankdrone.activetarget == 3 ? 0 : 1];
											tankdrone.turnToTarget(tempplayer.centerx, tempplayer.centery); //takes stats from player it's currently targeting
										}
										
										tankdrone.updateBarrel(); //updates graphical display of barrel
									}
									
								}
								
							} else if (tankdrone.owner != -1) {
								//non neutrals (only guards on teams)
								
								if (!tankdrone.targeting)
									tankdrone.findNewTargetGuard(allprojectiles); //finds new bullet target if it doesn't have one
								
								if (tankdrone.targeting) {
									
									if (tankdrone.activetarget < allprojectiles.size()) {
										TankProjectile tempbullet = allprojectiles.get(tankdrone.activetarget);
										tankdrone.turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
										tankdrone.updateBarrel();
									} else
										tankdrone.resetTargeting();
									
								}
								
							} else if (tankdrone.targeting) {
								DroneTank tempdrone = tankdrones[tankdrone.activetarget / 7][tankdrone.activetarget % 7];
								int temp = tankdrone.activetarget;
								
								if (tempdrone != null && tempdrone.owner == -1)
									tankdrone.resetTargeting();
								else if (temp != 3 && temp != 73)
									tankdrone.turnToTarget(tempdrone.centerx, tempdrone.centery);
								else
									tankdrone.turnToTarget(tankplayers[temp == 3 ? 0 : 1].centerx, tankplayers[temp == 3 ? 0 : 1].centery);
								
								tankdrone.updateBarrel();
							}
							
							if (tankdrone.fireBullet()) {
								allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, new Circle()));
								allprojectiles.get(allprojectiles.size() - 1).create(tankdrone.owner, tankdrone.getTrueX(), tankdrone.getTrueY(), tankdrone.getIndex(), tankdrone.getBarrelLength(), tankdrone.angle, tankdrone.bulletspeed, tankdrone.bulletsize, tankdrone.damage, tankdrone.bulletpenetration, tankdrone.bulletrange, 0, false, 0);
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
								tankdrone.moveToFront();
								
								if (tankdrone.clazz == 4)
									tankdrone.findNewTargetGuard(allprojectiles);
								
							}
							
						}
						
					}
					}
					
					//Collision detection logic for bullets
					for (TankProjectile singlebullet : allprojectiles) {
						singlebullet.updatePosition(); //uses bullet speed and angle to determine the next location each frame
						singlebullet.body.toBack(); //ensures that all bullets are behind everything else
						
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
						if (singlebullet.offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							
							if (!singlebullet.ricochet) {
								singlebullet.owner = TB_ELIMINATED;
								continue;
							} else
								singlebullet.ricochetMovement(STAGE_WIDTH, STAGE_HEIGHT);
							
						}
						
						if (singlebullet.outOfRange()) {
							singlebullet.owner = TB_ELIMINATED;
							continue;
						}
						
						for (DroneTank[] tankdronerow : tankdrones) {
						for (DroneTank tankdrone : tankdronerow) {
							
							if (tankdrone != null && singlebullet.owner != TB_ELIMINATED && singlebullet.intersectsNonGuard(tankdrone.base, tankdrone.owner)) {
								tankdrone.removeHealth(singlebullet.damage, singlebullet.owner);
								
								if (tankdrone.bulletCollision(singlebullet.owner, singlebullet.origindrone)) {
									
									for (DroneTank[] tankdronerow2 : tankdrones)
									for (DroneTank tankdrone2 : tankdronerow2)
										if (tankdrone2 != null && tankdrone2.activetarget == tankdrone.getIndex())
											tankdrone2.resetTargeting();
										
								}
								
								singlebullet.owner = TB_ELIMINATED;
								continue;
							}
							
						}
						}
						
						for (PlayerTank tankplayer : tankplayers) {
							
							if (singlebullet.intersectsNonGuard(tankplayer.base, tankplayer.team)) {
								tankplayer.currenthealth -= singlebullet.damage;
								singlebullet.owner = TB_ELIMINATED;
							}
							
						}
						
						for (TankProjectile singlebullet2 : allprojectiles) {
							
							if (singlebullet2.owner != singlebullet.owner && singlebullet2.owner != TB_ELIMINATED) {
								
								if (singlebullet.body.getBoundsInLocal().intersects(singlebullet2.body.getBoundsInLocal())) {
									int temppenetration = singlebullet2.penetration;
									singlebullet2.penetration -= singlebullet.penetration;
									singlebullet.penetration -= temppenetration;
									
									if (singlebullet.penetration <= 0)
										singlebullet.owner = TB_ELIMINATED;
									if (singlebullet2.penetration <= 0)
										singlebullet2.owner = TB_ELIMINATED;
									
								}
								
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == TB_ELIMINATED) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						}
						
					}
					
				} else if (inhelp) {
					
					if (mousedragged && !isovernode) {
						int temp = 0;
						int loopcount = 1;
						
						switch (typeselected) {
							case CIRCLE:
								loopcount = 360;
								mousedragged = false;
								break;
							case SPIRAL:
								temp = spiralindex++;	
								break;
							case RANDOM:
								temp = random.nextInt(360);
								break;
							case AUTO:
								autoangle = (autoangle + (int) (360 / autosplitslider.getValue()) + 1) % 360;
								mouseposx = 500 + 200 * Math.sin(Math.toRadians((double) autoangle));
								mouseposy = 300 - 200 * Math.cos(Math.toRadians((double) autoangle));
							case HOMING:
								double tempangledouble = Math.atan2(300 - mouseposy, 500 - mouseposx) + Math.PI / 2.0;
								temp = (int) Math.round(Math.toDegrees(tempangledouble));
								temp = Math.floorMod(temp, 360);
								break;
						}
						
						for (int angle = 0; angle < loopcount; angle++) {
							
							if (loopcount == 360)
								temp = angle;
							
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, new Circle()));
							
							if (allprojectiles.get(allprojectiles.size() - 1).create(0, (int) mouseposx, (int) mouseposy, 0, 0, temp, 2, 5, 0, 1, 99999, 3, false, allprojectiles.size()))
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							else
								allprojectiles.remove(allprojectiles.size() - 1);
							
						}
						
					}
					
					for (DroneTank tankdemo : tankdemos) {
						
						if (mouseonscreen && tankdemo.clazz != 4)
							tankdemo.turnToTarget((mouseposx - 100) / 80, (mouseposy - 60) / 80);
						else
							tankdemo.angle = (tankdemo.angle + 1) % 360;
						
						tankdemo.shooting = true;
						tankdemo.updateBarrel();
						
						if (tankdemo.clazz == 4) {
							tankdemo.findNewTargetGuard(allprojectiles); //finds new bullet target if it doesn't have one
							
							if (tankdemo.targeting) {
								
								if (tankdemo.activetarget < allprojectiles.size()) {
									TankProjectile tempbullet = allprojectiles.get(tankdemo.activetarget);
									tankdemo.turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
									tankdemo.updateBarrel();
								} else
									tankdemo.resetTargeting();
								
							}
							
						}
						
						if (tankdemo.fireBullet() && tankdemo.clazz == demotankselection) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(tankdemo.owner, tankdemo.getTrueX(), tankdemo.getTrueY(), tankdemo.getIndex(), tankdemo.getBarrelLength(), tankdemo.angle, tankdemo.bulletspeed, tankdemo.bulletsize, tankdemo.damage, tankdemo.bulletpenetration, tankdemo.bulletrange, 0, false, 0);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankdemo.moveToFront();
						}
						
					}
					
					for (TankProjectile singlebullet : allprojectiles) {
						
						if (singlebullet.outOfRange())
							singlebullet.owner = TB_ELIMINATED;
						
						if (singlebullet.offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							
							if (!singlebullet.ricochet)
								singlebullet.owner = TB_ELIMINATED;
							else
								singlebullet.ricochetMovement(STAGE_WIDTH, STAGE_HEIGHT);
							
						}
						
						for (TankProjectile singlebullet2 : allprojectiles) {
							
							if (singlebullet.owner != -1 && singlebullet2.owner != singlebullet.owner && singlebullet2.owner != TB_ELIMINATED && singlebullet.owner != TB_ELIMINATED && Math.pow(singlebullet.centerx - singlebullet2.centerx, 2) + Math.pow(singlebullet.centerx - singlebullet2.centerx, 2) < 100) {
								
								if (singlebullet.body.getBoundsInLocal().intersects(singlebullet2.body.getBoundsInLocal())) {
									int temppenetration = singlebullet2.penetration;
									singlebullet2.penetration -= singlebullet.penetration;
									singlebullet.penetration -= temppenetration;
									
									if (singlebullet.penetration <= 0)
										singlebullet.owner = TB_ELIMINATED;
									if (singlebullet2.penetration <= 0)
										singlebullet2.owner = TB_ELIMINATED;
									
								}
								
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == TB_ELIMINATED) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						} else {
							allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
							allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						}
						
					}
					
				} else {
					
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.moveLR(); //takes keyboard inputs to turn the players
						tankplayer.updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayer.updateHealthDisplay(); //updates the numerical health display
						tankplayer.sightline.setVisible(false);
						tankplayer.sightlinereflect.setVisible(false);
						tankplayer.sightlinereflect2.setVisible(false);
						
						if (tankplayer.currenthealth <= 0) {
							tankplayer.setPlayerVisible(false);
							respawntimer++;
							
							if (respawntimer == 90) {
								tankplayer.setPlayerVisible(true);
								tankplayer.setClassAttributes();
								respawntimer = 0;
							}
							
						} else if (tankplayer.fireBullet()) {
							allprojectiles.add(new TankProjectile(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, new Circle()));
							allprojectiles.get(allprojectiles.size() - 1).create(tankplayer.team, tankplayer.getTrueX(), tankplayer.getTrueY(), tankplayer.team == 0 ? 3 : 73, tankplayer.getBarrelLength(), tankplayer.angle, tankplayer.bulletspeed, tankplayer.bulletsize, tankplayer.damage, tankplayer.bulletpenetration, 99999, tankplayer.maxricochet, tankplayer.ricochet, 0);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankplayer.moveToFront();
						}
						
					}
					
					for (TankProjectile singlebullet : allprojectiles) {
						singlebullet.updatePosition(); //uses bullet speed and angle to determine the next location each frame
						singlebullet.body.toBack(); //ensures that all bullets are behind everything else
						
						if (singlebullet.offStage(STAGE_WIDTH, STAGE_HEIGHT)) {
							
							if (!singlebullet.ricochet)
								singlebullet.owner = TB_ELIMINATED;
							else
								singlebullet.ricochetMovement(STAGE_WIDTH, STAGE_HEIGHT);
							
						}
						
						for (PlayerTank tankplayer : tankplayers) {
							
							if (singlebullet.intersectsNonGuard(tankplayer.base, tankplayer.team) && tankplayer.currenthealth > 0) {
								tankplayer.currenthealth -= singlebullet.damage;
								singlebullet.owner = TB_ELIMINATED;
							}
							
						}
						
						for (TankProjectile singlebullet2 : allprojectiles) {
							
							if (singlebullet2.owner != singlebullet.owner && singlebullet2.owner != TB_ELIMINATED) {
								
								if (singlebullet.body.getBoundsInLocal().intersects(singlebullet2.body.getBoundsInLocal())) {
									int temppenetration = singlebullet2.penetration;
									singlebullet2.penetration -= singlebullet.penetration;
									singlebullet.penetration -= temppenetration;
									
									if (singlebullet.penetration <= 0)
										singlebullet.owner = TB_ELIMINATED;
									if (singlebullet2.penetration <= 0)
										singlebullet2.owner = TB_ELIMINATED;
									
								}
								
							}
							
						}
						
					}
					
					for (i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == TB_ELIMINATED) {
							root.getChildren().remove(allprojectiles.get(i).body);
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