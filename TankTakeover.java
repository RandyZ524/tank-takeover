import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Cursor;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//Importing my stuff

public class TankTakeover extends Application {
	
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }
	
	int respawntimer, globaltimer;
	int spiralindex = 0;
	int demotankselection = -1;
	double mouseposx, mouseposy;
	boolean inthelevel, inhelp, mouseonscreen, mousedragged;
	boolean isovernode, oncheckbox = false;
	boolean guardboundaryvisible = true;
	
	int autoangle = 0;
	
	DroneTank[] tankdemos = new DroneTank[Constants.NUM_OF_DRONES];
	PlayerTank[] tankplayers = new PlayerTank[Constants.NUM_OF_PLAYERS];
	DroneTank[][] tankdrones = new DroneTank[11][7];
	GameMode[] gamemodes = new GameMode[Constants.NUM_OF_MODES];
	LevelPicture[] levelpictures = new LevelPicture[Constants.NUM_OF_LEVELS];
	TankDescriptions[] alldescriptions = new TankDescriptions[Constants.NUM_OF_DRONES];
	ArrayList<TankProjectile> allprojectiles = new ArrayList<TankProjectile>();
	ArrayList<HealingPlus> greenplusses = new ArrayList<HealingPlus>();
	
	String[][] levelschematics = new String[11][7];
	
	Image[] icons = new Image[Constants.NUM_OF_DRONES];
	ImageView[] demopictures = new ImageView[Constants.NUM_OF_DRONES];
	Rectangle[] demobackgrounds = new Rectangle[Constants.NUM_OF_DRONES];
	Text[] typeofprojectile = new Text[5];
	
	ImageView startbutton = new ImageView(new Image("Buttons/Start.png"));
	ImageView helpbutton = new ImageView(new Image("Buttons/Help.png"));
	ImageView backbutton = new ImageView(new Image("Buttons/Back.png"));
	ImageView deathicon = new ImageView(new Image("Death_Icon.png"));
	
	Rectangle namebase = new Rectangle(0, 25, 0, 34);
	Rectangle descriptionbase = new Rectangle(150, 515, 630, 65);
	Rectangle statsbase = new Rectangle(750, 175, 235, 150);
	
	Text healthtext = new Text(808, 200, "Health:");
	Text damagetext = new Text(792, 250, "Damage:");
	Text firingspeedtext = new Text(760, 300, "Firing speed:");
	Text victorytext = new Text(170, 250, "Player ");
	
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
		
		namebase.setStroke(Color.BLACK);
		namebase.setFill(Constants.LIGHT_GRAY);
		namebase.setStrokeWidth(4);
		namebase.setArcWidth(Constants.CORNER_ARC);
		namebase.setArcHeight(Constants.CORNER_ARC);
		namebase.setVisible(false);
		
		descriptionbase.setStroke(Color.BLACK);
		descriptionbase.setFill(Constants.LIGHT_GRAY);
		descriptionbase.setStrokeWidth(4);
		descriptionbase.setArcWidth(Constants.CORNER_ARC);
		descriptionbase.setArcHeight(Constants.CORNER_ARC);
		descriptionbase.setVisible(false);
		
		statsbase.setStroke(Color.BLACK);
		statsbase.setFill(Constants.LIGHT_GRAY);
		statsbase.setStrokeWidth(4);
		statsbase.setArcWidth(Constants.CORNER_ARC);
		statsbase.setArcHeight(Constants.CORNER_ARC);
		statsbase.setVisible(false);
		
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
		Group root = new Group(startbutton, helpbutton, backbutton, deathicon, namebase, descriptionbase, statsbase, healthtext, damagetext, firingspeedtext, victorytext, autosplitslider);
		Scene scene = new Scene(root, Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
		scene.setCursor(Cursor.CROSSHAIR);
		
		for (int i = 0; i < Constants.NUM_OF_BULLET_TYPES; i++) {
			String tempstring = HelpType.values()[i].toString();
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
		
		byte[] inbuf = new byte[20500];
		
		try (DataInputStream dataIn = new DataInputStream(new FileInputStream("GameModeDescriptions.txt"))) {
			dataIn.read(inbuf);
			String str = new String(inbuf); //converts array representing the characters of the text file to a string
			String[] modedescriptions = str.split(System.lineSeparator() + System.lineSeparator());
			
			for (int i = 0; i < Constants.NUM_OF_MODES; i++) {
				gamemodes[i] = new GameMode();
				gamemodes[i].description = modedescriptions[i].split("\n");
				gamemodes[i].create(GameModeNames.values()[i].toString(), i);
				
				final int index = i;
				
				gamemodes[i].togglemode.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					
					public void handle(MouseEvent event) {
						
						if (!oncheckbox) {
							oncheckbox = true;
							GameMode.descriptionbase.setVisible(true);
							GameMode.descriptionbase.toFront();
							
							double maxwidth = 0;
							
							for (int i = 0; i < 6; i++) {
								GameMode.descriptiondisplay[i].setText(gamemodes[index].description[i]);
								GameMode.descriptiondisplay[i].setVisible(true);
								
								if (GameMode.descriptiondisplay[i].getLayoutBounds().getWidth() > maxwidth)
									maxwidth = GameMode.descriptiondisplay[i].getLayoutBounds().getWidth();
								
							}
							
							GameMode.descriptionbase.setWidth((int) Math.round(maxwidth) + 20);
						}
						
					}
					
				});
				
				gamemodes[i].togglemode.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
					@Override
					
					public void handle(MouseEvent event) {
						oncheckbox = false;
						GameMode.descriptionbase.setVisible(false);
						
						for (Text descline : GameMode.descriptiondisplay)
							descline.setVisible(false);
						
					}
					
				});
				
				gamemodes[i].togglemode.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
					@Override
					
					public void handle(MouseEvent event) {
						mouseposx = event.getX() + gamemodes[index].togglemode.getLayoutX();
						mouseposy = event.getY() + gamemodes[index].togglemode.getLayoutY();
						GameMode.descriptionbase.setX(mouseposx + 10);
						GameMode.descriptionbase.setY(mouseposy + 10);
						
						for (int i = 0; i < 6; i++) {
							GameMode.descriptiondisplay[i].setX(GameMode.descriptionbase.getX() + 10);
							GameMode.descriptiondisplay[i].setY(GameMode.descriptionbase.getY() + (i + 1) * 25);
							GameMode.descriptiondisplay[i].toFront();
						}
						
					}
					
				});
				
				root.getChildren().add(gamemodes[i].togglemode);
			}
			
			for (int i = 0; i < 6; i++) {
				GameMode.descriptiondisplay[i] = new Text();
				GameMode.descriptiondisplay[i].setFont(Font.font("Verdana", 16));
				root.getChildren().add(GameMode.descriptiondisplay[i]);
			}				
			
			root.getChildren().add(GameMode.descriptionbase);
		} catch (IOException exc) {
			System.out.println("Read error.");
			return;
		}
		
		for (int i = 0; i < Constants.NUM_OF_LEVELS; i++) {
			levelpictures[i] = new LevelPicture();
			levelpictures[i].create(i);
			
			final int index = i;
			
			levelpictures[i].thumbnail.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					LevelPicture.displayPreview(index, levelpictures);
				}
				
			});
			
			levelpictures[i].thumbnail.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					LevelPicture.dynamicPreview(event, levelpictures[index].thumbnail);
				}
				
			});
			
			levelpictures[i].thumbnail.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					LevelPicture.hidePreview();
				}
				
			});
			
			levelpictures[i].thumbnail.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				
				public void handle(MouseEvent event) {
					LevelPicture.changeLevelSelection(index);
				}
				
			});
			
		}
		
		for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
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
			
			final int index = i;
			
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
					
					for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
						
						if (i == index && demotankselection != -1) {
							demobackgrounds[i].setStroke(Color.GREEN);
							tankdemos[i].setDroneVisible(true);
							
							if (tankdemos[i].clazz == 4 && guardboundaryvisible) {
								tankdemos[i].rangeboundary.setRadius(tankdemos[i].bulletrange);
								tankdemos[i].rangeboundary.setVisible(true);
							}
							
						} else {
							demobackgrounds[i].setStroke(Color.BLACK);
							tankdemos[i].setDroneVisible(false);
							tankdemos[i].rangeboundary.setVisible(false);
						}
						
						alldescriptions[i].setDescriptionVisible((i == index && demotankselection != -1) ? true : false);
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
			
			root.getChildren().addAll(demopictures[i], levelpictures[i].thumbnail, demobackgrounds[i]);
		}
		
		root.getChildren().addAll(LevelPicture.selectionbox, LevelPicture.bigimagebase, LevelPicture.redcrossA, LevelPicture.redcrossB, LevelPicture.noselection, LevelPicture.displaydesc[0], LevelPicture.displaydesc[1], LevelPicture.bigimage);
		
		for (int i = 0; i < Constants.NUM_OF_PLAYERS; i++) {
			tankplayers[i] = new PlayerTank();
			tankplayers[i].create(i);
			tankplayers[i].setPlayerClass(0, Constants.NUM_OF_PLAYERS);
			tankplayers[i].setClassAttributes();
		}
		
		inbuf = new byte[20500];
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
			
			for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
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
			root.getChildren().addAll(tankplayer.base, tankplayer.barrel, tankplayer.healthdisplay, tankplayer.sightline, tankplayer.sightlinereflect, tankplayer.sightlinereflect2, tankplayer.homingbase, tankplayer.homingicon);
		
		for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
			tankdemos[i] = new DroneTank();
			tankdemos[i].create(5, 3);
			tankdemos[i].setDroneClass(i);
			tankdemos[i].setClassAttributes(icons[tankdemos[i].clazz]);
			tankdemos[i].setDroneVisible(false);
			root.getChildren().addAll(tankdemos[i].base, tankdemos[i].barrel, tankdemos[i].icon, tankdemos[i].rangeboundary);
		}
		
		startbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			
			public void handle(MouseEvent event) {
				
				if (LevelPicture.levelselection != 0) {
					globaltimer = 0;
					inthelevel = true;
					startbutton.setVisible(false);
					helpbutton.setVisible(false);
					LevelPicture.selectionbox.setVisible(false);
					
					for (GameMode mode : gamemodes) {
						mode.enabled = mode.togglemode.isSelected();
						mode.togglemode.setVisible(false);
					}
					
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.setClassAttributes();
						tankplayer.setPlayerVisible(gamemodes[1].enabled, true);
					}
					
					for (int i = allprojectiles.size() - 1; i >= 0; i--) {
						root.getChildren().remove(allprojectiles.get(i).body);
						allprojectiles.remove(i);
					}
					
					for (LevelPicture picture : levelpictures)
						picture.thumbnail.setVisible(false);
					
					byte[] inbuf = new byte[20500];
					
					try (DataInputStream dataIn = new DataInputStream(new FileInputStream("StageDronePlacement.txt"))) {
						dataIn.read(inbuf);
						String str = new String(inbuf);
						str = str.substring(str.indexOf("Level " + LevelPicture.levelselection) + 10, str.length()).trim();
						
						for (int i = 0; i < 7; i++)
							
							for (int j = 0; j < 11; j++) {
								levelschematics[j][i] = str.substring(0, 1);
								str = str.substring(1, str.length()).trim();
							}
						
					} catch (IOException exc) {
						System.out.println("Read error.");
						return;
					}
					
					for (int i = 0; i < 11; i++)
						for (int j = 0; j < 7; j++)
							
							if (levelschematics[i][j].equals("-")) {
								tankdrones[i][j] = null;
							} else {
								tankdrones[i][j] = new DroneTank();
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
					
					scene.setCursor(Cursor.NONE);
				} else {
					LevelPicture.setNoSelectionVisible(true);
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
				LevelPicture.selectionbox.setVisible(false);
				LevelPicture.setNoSelectionVisible(false);
				backbutton.setVisible(true);
				
				if (typeselected == HelpType.AUTO)
					autosplitslider.setVisible(true);
				
				for (Text typeofbullet : typeofprojectile)
					typeofbullet.setVisible(true);
				for (PlayerTank tankplayer : tankplayers)
					tankplayer.setPlayerVisible(gamemodes[1].enabled, false);
				for (GameMode mode : gamemodes)
					mode.togglemode.setVisible(false);
				
				for (int i = allprojectiles.size() - 1; i >= 0; i--) {
					root.getChildren().remove(allprojectiles.get(i).body);
					allprojectiles.remove(i);
				}
				
				for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
					levelpictures[i].thumbnail.setVisible(false);
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
					LevelPicture.selectionbox.setVisible(true);
					
					for (Text typeofbullet : typeofprojectile)
						typeofbullet.setVisible(false);
					for (PlayerTank tankplayer : tankplayers)
						tankplayer.setPlayerVisible(gamemodes[1].enabled, true);
					for (GameMode mode : gamemodes)
						mode.togglemode.setVisible(true);
					
					for (int i = 0; i < Constants.NUM_OF_DRONES; i++) {
						demopictures[i].setVisible(false);
						demobackgrounds[i].setVisible(false);
						tankdemos[i].setDroneVisible(false);
						alldescriptions[i].setDescriptionVisible(false);
						levelpictures[i].thumbnail.setVisible(true);
					}
					
					for (int i = allprojectiles.size() - 1; i >= 0; i--) {
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
					LevelPicture.selectionbox.setVisible(true);
					
					for (int i = 0; i < Constants.NUM_OF_PLAYERS; i++)
						tankplayers[i].reset(i);
					for (TankProjectile singlebullet : allprojectiles)
						root.getChildren().remove(singlebullet.body);
					for (LevelPicture picture : levelpictures)
						picture.thumbnail.setVisible(true);
					for (GameMode mode : gamemodes)
						mode.togglemode.setVisible(true);
					
					for (DroneTank[] tankdronerow : tankdrones)
						for (DroneTank tankdrone : tankdronerow)
							
							if (tankdrone != null) {
								root.getChildren().removeAll(tankdrone.base, tankdrone.ownerflag, tankdrone.rangeboundary, tankdrone.barrel, tankdrone.healthbase, tankdrone.bluehealthbar, tankdrone.redhealthbar, tankdrone.icon);
								tankdrone = null;
							}
					
					victorytext.setText("Player ");
					LevelPicture.levelselection = 0;
					LevelPicture.selectionbox.setX(Constants.STAGE_WIDTH + 10);
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
					
					for (int i = 0; i < Constants.NUM_OF_PLAYERS; i++) {
						megamindplayers[i] = tankplayers[i].stopLRandShooting(event.getCode().toString());
						
						if (megamindplayers[i])
							for (DroneTank[] tankdronerow : tankdrones)
								for (DroneTank tankdrone : tankdronerow)
									if (tankdrone != null && tankdrone.owner == i && tankdrone.clazz != 4)
										tankdrone.resetTargeting();
						
					}
					
					if (!inthelevel)
						for (PlayerTank tankplayer : tankplayers)
							tankplayer.switchClass(event.getCode().toString(), Constants.NUM_OF_PLAYER_CLASSES);
					
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
				
				if (inhelp)
					mousedragged = false;
				
			}
			
		});
		
		timer = new AnimationTimer() {
			@Override
			
			public void handle(long now) {
				
				if (inthelevel) {
					globaltimer++;
					
					if (!gamemodes[2].enabled || globaltimer <= 1800) {
					
						//Player tank logic
						for (PlayerTank tankplayer : tankplayers) {
							
							if (gamemodes[2].enabled && globaltimer == 1800) {
								tankplayer.barrel.setVisible(false);
								tankplayer.sightline.setVisible(false);
								tankplayer.sightlinereflect.setVisible(false);
								tankplayer.sightlinereflect2.setVisible(false);
								continue;
							}
							
							tankplayer.moveLR(); //takes keyboard inputs to turn the players
							
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
								allprojectiles.add(new TankProjectile());
								allprojectiles.get(allprojectiles.size() - 1).create(tankplayer, tankplayer.ricochet, tankplayer.maxricochet, tankplayer.homing);
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							}
							
						}
						
					}
					
					if (!gamemodes[2].enabled || globaltimer >= 1800) {
						
						//Drone tank logic
						for (DroneTank[] tankdronerow : tankdrones)
							for (DroneTank tankdrone : tankdronerow)
								if (tankdrone != null) {
									
									if (gamemodes[0].enabled && tankdrone.owner != -1 && globaltimer % 120 == 0) {
										tankdrone.currenthealth -= 4;
										tankdrone.lastdamaged = 0;
										
										if (tankdrone.currenthealth <= 0) {
											tankdrone.toNeutral(-1);
										}
										
									}
									
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
											
											if (!tankdrone.targeting) //finds new target if it doesn't have one
												tankdrone.findNewTarget(tankdrones, tankplayers);
											else {
												tankdrone.turnToTarget();
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
											} else {
												tankdrone.resetTargeting();
											}
											
										}
										
									} else if (tankdrone.targeting) {
										Tank temptank;
										
										if (tankdrone.activetarget == tankplayers[0].getIndex())
											temptank = tankplayers[0];
										else if (tankdrone.activetarget == tankplayers[1].getIndex())
											temptank = tankplayers[1];
										else
											temptank = tankdrones[tankdrone.activetarget / 7][tankdrone.activetarget % 7];
										
										if (temptank != null && temptank.owner == -1) {
											tankdrone.resetTargeting();
										} else if (!tankdrone.shooting) {
											tankdrone.findTargetAngle(temptank);
											tankdrone.turnToTarget();
										}
										
									}
									
									if (tankdrone.fireBullet()) {
										allprojectiles.add(new TankProjectile());
										allprojectiles.get(allprojectiles.size() - 1).create(tankdrone, false, 0, false);
										root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
										
										if (tankdrone.clazz == 4)
											tankdrone.findNewTargetGuard(allprojectiles);
										
									}
									
								}
								
					}
					
					//Collision detection logic for bullets
				bulletloop:
					for (TankProjectile singlebullet : allprojectiles) {
						
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
						
						if (singlebullet.owner == Constants.TB_ELIMINATED)
							continue bulletloop;
						
						if (singlebullet.origindrone == 3 || singlebullet.origindrone == 73) {
							
							for (PlayerTank tankplayer : tankplayers) {
								
								if (tankplayer.homingbase.isVisible() && singlebullet.body.intersects(tankplayer.homingbase.getBoundsInParent())) {
									tankplayer.homingbasehealth -= singlebullet.damage;
									
									double temppercent = (double) tankplayer.homingbasehealth / 500;
									int temprgb = (int) Math.round(temppercent * 195);
									temprgb = temprgb < 0 ? 0 : temprgb;
									
									tankplayer.homingbase.setFill(Color.rgb(temprgb, temprgb, temprgb));
									singlebullet.owner = Constants.TB_ELIMINATED;
									
									if (tankplayer.homingbasehealth <= 0) {
										tankplayer.homingbase.setVisible(false);
										tankplayer.homingicon.setVisible(false);
										tankplayer.homing = true;
										tankplayer.bulletrange = 1000;
										continue bulletloop;
									}
									
								}
								
							}
							
						}
						
						if (singlebullet.offStage() && !singlebullet.homing) {
							
							if (!singlebullet.ricochet) {
								singlebullet.owner = Constants.TB_ELIMINATED;
								continue bulletloop;
							} else {
								singlebullet.ricochetMovement();
							}
							
						}
						
						if (singlebullet.outOfRange())
							continue bulletloop;
						
						if (singlebullet.droneCollisionLogic(tankdrones, gamemodes[0].enabled)) {
							
							if (singlebullet.owner == -3) {
								int tempindex = singlebullet.origindrone;
								greenplusses.add(new HealingPlus());
								greenplusses.get(greenplusses.size() - 1).setOrigin(tankdrones[tempindex / 7][tempindex % 7]);
								root.getChildren().add(greenplusses.get(greenplusses.size() - 1).healingicon);
							}
							
							singlebullet.owner = Constants.TB_ELIMINATED;
							continue bulletloop;
						}
						
						if (singlebullet.playerCollisionLogic(tankplayers))
							continue bulletloop;
						
						singlebullet.bulletCollisionLogic(allprojectiles);
					}
					
					for (int i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == Constants.TB_ELIMINATED) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						} else {
							
							if (allprojectiles.get(i).homing)
								allprojectiles.get(i).homingMovement(tankplayers, tankdrones, false, gamemodes[0].enabled);
						
							allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
							allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						}
						
					}
					
					for (PlayerTank tankplayer : tankplayers) {
						tankplayer.updateBarrel(); //updates the graphical appearance of the barrel to follow the angle
						tankplayer.updateHealthDisplay(); //updates the numerical health display
						tankplayer.updateSightLine(); //updates the graphical appearance of the sight line to follow the angle
						tankplayer.moveToFront();
					}
					
					for (DroneTank[] tankdronerow : tankdrones)
						for (DroneTank tankdrone : tankdronerow)
						
							if (tankdrone != null) {
								tankdrone.updateHealthDisplay(); //updates the graphical health bar display
								tankdrone.displayHealthBar(); //displays the health bar if the tank was damaged 60 frames ago
								tankdrone.updateBarrel(); //updates graphical display of barrel
								tankdrone.moveToFront();
							}
					
					for (int i = greenplusses.size() - 1; i >= 0; i--) {
						
						if (greenplusses.get(i).updateAndRemove()) {
							greenplusses.get(i).healingicon.setVisible(false);
							root.getChildren().remove(greenplusses.get(i).healingicon);
							greenplusses.remove(i);
						}
						
					}
					
					for (PlayerTank tankplayer : tankplayers)
					
						if (tankplayer.currenthealth <= 0) {
							tankplayer.healthdisplay.setVisible(false);
							deathicon.setVisible(true);
							deathicon.relocate(tankplayer.getTrueX() - 11, tankplayer.getTrueY() - 11);
							deathicon.toFront();
							victorytext.setVisible(true);
							victorytext.setText(victorytext.getText() + (tankplayer.owner == 0 ? "2" : "1") + " Wins!");
							victorytext.setFill(tankplayer.owner == 0 ? Color.RED : Color.BLUE);
							victorytext.setX((Constants.STAGE_WIDTH - victorytext.getLayoutBounds().getWidth()) * 0.5);
							victorytext.toFront();
							backbutton.setVisible(true);
							backbutton.toFront();
							scene.setCursor(Cursor.CROSSHAIR);
							timer.stop();
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
							
							allprojectiles.add(new TankProjectile());
							
							if (allprojectiles.get(allprojectiles.size() - 1).generate((int) mouseposx, (int) mouseposy, temp, allprojectiles.size())) {
								root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							} else {
								allprojectiles.remove(allprojectiles.size() - 1);
							}
							
						}
						
					}
					
					for (DroneTank tankdemo : tankdemos) {
						
						if (mouseonscreen && tankdemo.clazz != 4) {
							tankdemo.targetangle = tankdemo.findMouseTargetAngle(mouseposx, mouseposy);
							tankdemo.turnToTarget();
						} else {
							tankdemo.angle = (tankdemo.angle + 1) % 360;
						}
						
						tankdemo.shooting = true;
						tankdemo.updateBarrel();
						
						if (tankdemo.clazz == 4) {
							tankdemo.findNewTargetGuard(allprojectiles); //finds new bullet target if it doesn't have one
							
							if (tankdemo.targeting) {
								
								if (tankdemo.activetarget < allprojectiles.size()) {
									TankProjectile tempbullet = allprojectiles.get(tankdemo.activetarget);
									tankdemo.turnToTargetGuard(tempbullet.centerx, tempbullet.centery, tempbullet.speed, tempbullet.angle);
									tankdemo.updateBarrel();
								} else {
									tankdemo.resetTargeting();
								}
								
							}
							
						}
						
						if (tankdemo.fireBullet() && tankdemo.clazz == demotankselection) {
							allprojectiles.add(new TankProjectile());
							allprojectiles.get(allprojectiles.size() - 1).create(tankdemo, false, 0, false);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
							tankdemo.moveToFront();
						}
						
					}
					
				bulletloop:
					for (TankProjectile singlebullet : allprojectiles) {
						
						if (singlebullet.outOfRange())
							continue bulletloop;
						
						if (singlebullet.offStage()) {
							
							if (!singlebullet.ricochet) {
								singlebullet.owner = Constants.TB_ELIMINATED;
								continue bulletloop;
							} else {
								singlebullet.ricochetMovement();
							}
							
						}
						
						singlebullet.bulletCollisionLogic(allprojectiles);
					}
					
					for (int i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == Constants.TB_ELIMINATED) {
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
						tankplayer.healthdisplay.toBack();
						tankplayer.barrel.toBack();
						tankplayer.base.toBack();
						
						if (tankplayer.currenthealth <= 0) {
							tankplayer.setPlayerVisible(gamemodes[1].enabled, false);
							respawntimer++;
							
							if (respawntimer == 90) {
								tankplayer.setPlayerVisible(gamemodes[1].enabled, true);
								tankplayer.setClassAttributes();
								respawntimer = 0;
							}
							
						} else if (tankplayer.fireBullet()) {
							allprojectiles.add(new TankProjectile());
							allprojectiles.get(allprojectiles.size() - 1).create(tankplayer, tankplayer.ricochet, tankplayer.maxricochet, tankplayer.homing);
							root.getChildren().add(allprojectiles.get(allprojectiles.size() - 1).body);
						}
						
					}
					
				bulletloop:
					for (TankProjectile singlebullet : allprojectiles) {
						
						if (singlebullet.offStage() && !singlebullet.homing) {
							
							if (!singlebullet.ricochet) {
								singlebullet.owner = Constants.TB_ELIMINATED;
								continue bulletloop;
							} else {
								singlebullet.ricochetMovement();
							}
							
						}
						
						if (singlebullet.playerCollisionLogic(tankplayers))
							continue bulletloop;
						
						singlebullet.bulletCollisionLogic(allprojectiles);
					}
					
					for (int i = allprojectiles.size() - 1; i >= 0; i--) {
						
						if (allprojectiles.get(i).owner == Constants.TB_ELIMINATED) {
							root.getChildren().remove(allprojectiles.get(i).body);
							allprojectiles.remove(i);
						} else {
							
							if (allprojectiles.get(i).homing)
								allprojectiles.get(i).homingMovement(tankplayers, tankdrones, true, false);
							
							allprojectiles.get(i).updatePosition(); //uses bullet speed and angle to determine the next location each frame
							allprojectiles.get(i).body.toBack(); //ensures that all bullets are behind everything else
						}
						
					}
					
				}
				
			}
			
		};
		timer.start();
		
		/*scene.widthProperty().addListener((dont, touch, it) -> {
			scene.setWidth(Constants.STAGE_WIDTH); //nice try
		});
		
		scene.heightProperty().addListener((dont, touch, it) -> {
			scene.setHeight(Constants.STAGE_HEIGHT); //nice try
		});*/
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}