package com.verminsnest.screens.mainmenus;

import java.net.MalformedURLException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.verminsnest.core.VerminsNest;
import com.verminsnest.core.singletons.RuntimeData;
import com.verminsnest.misc.gui.ButtonManager;
import com.verminsnest.screens.VNScreen;

public class SettingsMenu extends VNScreen {

	// Textures
	private int[] menuScrollPos;
	private int[] settingsScrollPos;

	// Buttons
	private long blockTime;
	private boolean movementBlocked;
	private long blockStartTime;

	// Main settings menu
	private final static int GRAPHICS = 0;
	private final static int SOUND = 1;
	private final static int CONTROLS = 2;
	private final static int BACK = 3;
	private ButtonManager settingsMenuManager;
	
	private ButtonManager musicMenuManager;
	private ButtonManager controlsMenuManager;
	private ButtonManager videoMenuManager;
	
	// Graphics settings menu
	private final static int MODE = 0;
	private final static int RESOLUTION = 1;
	private final static int LANGUAGE = 2;

	private int menuIndex;
	private ButtonManager currentMenuManager;

	public SettingsMenu(VerminsNest game) {
		super(game);	
	}

	@Override
	public void show() {
		// Buttons
		blockTime = 0;
		blockStartTime = System.currentTimeMillis();
		movementBlocked = true;
		menuIndex = -1;
		while(videoMenuManager.getIndex() != 0){
			videoMenuManager.prev();
		}
		while(controlsMenuManager.getIndex() != 0){
			controlsMenuManager.prev();
		}
		while(musicMenuManager.getIndex() != 0){
			musicMenuManager.prev();
		}
		while(settingsMenuManager.getIndex() != 0){
			settingsMenuManager.prev();
		}
	}

	@Override
	public void render(float delta) {
		game.setPro();
		game.getBatch().begin();
		game.getBatch().draw(RuntimeData.getInstance().getAsset("textures/general/Background.png"), Gdx.graphics.getWidth() / 2 - RuntimeData.getInstance().getAsset("textures/general/Background.png").getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - RuntimeData.getInstance().getAsset("textures/general/Background.png").getHeight() / 2);
		game.getBatch().draw(RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png"), menuScrollPos[0], menuScrollPos[1]);
		game.getBatch().draw(RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png"), settingsScrollPos[0], settingsScrollPos[1]);
		settingsMenuManager.draw(game.getBatch());
		if (currentMenuManager != null) {
			currentMenuManager.draw(game.getBatch());
		}
		game.getBatch().end();
		this.mangageControls();

	}

	@Override
	public void resize(int width, int height) {
		menuScrollPos = new int[]{
				Gdx.graphics.getWidth() / 2 - (RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getWidth() + RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth()) / 2,
				Gdx.graphics.getHeight() / 2 - RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight() / 2};
		settingsScrollPos = new int[]{Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight() / 2};
		settingsMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight()}, menuScrollPos);
		videoMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getHeight()},
				settingsScrollPos);
		musicMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getHeight()},
				settingsScrollPos);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		settingsMenuManager.dispose();
		videoMenuManager.dispose();
		musicMenuManager.dispose();
		controlsMenuManager.dispose();
		if (currentMenuManager != null)currentMenuManager.dispose();
		isDisposed = true;
	}

	// Change to preferences later
	private void mangageControls(){

		blockTime = System.currentTimeMillis() - blockStartTime;
		if (blockTime > 225) {
			movementBlocked = false;
		}

		// S Pressed
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			switch (menuIndex) {
			case -1:
				if (!movementBlocked) {
					settingsMenuManager.next();
					movementBlocked = true;
					blockStartTime = System.currentTimeMillis();
				}
				break;
			default:
				if (!movementBlocked) {
					currentMenuManager.next();
					movementBlocked = true;
					blockStartTime = System.currentTimeMillis();
				}
				break;
			}
		}

		// W Pressed
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			switch (menuIndex) {
			case -1:
				if (!movementBlocked) {
					settingsMenuManager.prev();
					movementBlocked = true;
					blockStartTime = System.currentTimeMillis();
				}
				break;
			default:
				if (!movementBlocked) {
					currentMenuManager.prev();
					movementBlocked = true;
					blockStartTime = System.currentTimeMillis();
				}
				break;
			}
		}

		// D Pressed
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			switch (menuIndex) {
			case -1:
				break;
			default:
				if (!movementBlocked) {
					switch (settingsMenuManager.getIndex()) {
					case GRAPHICS:
						if(currentMenuManager.getCurrent() != currentMenuManager.getButtons().get(currentMenuManager.getButtons().size()-1)){
							currentMenuManager.getCurrent().nextOption();
							if(currentMenuManager.getCurrent().getText().getText().equals(game.getConfig().getMessage("GraphicsMenu_Resolution"))&&currentMenuManager.getButtons().get(1).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Mode_Fullscreen"))){
								currentMenuManager.getButtons().get(1).nextOption();
							}
							movementBlocked = true;
							blockStartTime = System.currentTimeMillis();
						}
						break;
					case SOUND:
						break;
					case CONTROLS:
						break;
					}
					break;
				}
			}
		}

		// A Pressed
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			switch (menuIndex) {
			case -1:
				break;
			default:
				if (!movementBlocked) {
					switch (settingsMenuManager.getIndex()) {
					case GRAPHICS:
						if(currentMenuManager.getCurrent() != currentMenuManager.getButtons().get(currentMenuManager.getButtons().size()-1)){
							currentMenuManager.getCurrent().prevOption();
							//TODO CHeck the if?
							if(currentMenuManager.getCurrent().getText().getText().equals(game.getConfig().getMessage("GraphicsMenu_Resolution"))&&currentMenuManager.getButtons().get(1).getOption().getText().equals("Fullscreen")){
								currentMenuManager.getButtons().get(1).nextOption();
							}
							movementBlocked = true;
							blockStartTime = System.currentTimeMillis();
						}
						break;
					case SOUND:
						break;
					case CONTROLS:
						break;
					}
					break;
				}
			}
		}

		// Enter pressed
		if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			if (!movementBlocked) {
				switch (menuIndex) {
				case -1:
					switch (settingsMenuManager.getIndex()) {
					case GRAPHICS:
						while (!videoMenuManager.getButtons().get(0).getOption().getText().equals(
								game.getConfig().getResolution()[0] + "x" + (game.getConfig().getResolution()[1]+25))) {
							videoMenuManager.getButtons().get(0).nextOption();
						}
						if(!game.getConfig().isFullscreen() && videoMenuManager.getButtons().get(1).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Mode_Fullscreen"))) videoMenuManager.getButtons().get(1).nextOption();
						if(!game.getConfig().getLanguage().equals("en")&& videoMenuManager.getButtons().get(2).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Language_English")))videoMenuManager.getButtons().get(2).nextOption();
						currentMenuManager = videoMenuManager;
						menuIndex = 0;
						break;
					case SOUND:
						currentMenuManager = musicMenuManager;
						menuIndex = 1;
						break;
					case CONTROLS:
						break;
					case BACK:
						game.showScreen(VerminsNest.MAINMENU);
						break;
					}
					break;
				case GRAPHICS:
					switch (currentMenuManager.getIndex()) {
					case RESOLUTION:
						break;
					case MODE:
						break;
					case LANGUAGE:
						break;
					case BACK:
						Boolean resize = false;
						int[] newRes = new int[2];
						String currentLang = "";
						Boolean reload = false;
						
						String resolution = game.getConfig().getResolution()[0] + "x"
								+ game.getConfig().getResolution()[1];
						if (!resolution.equals(currentMenuManager.getButtons().get(0).getOption().getText()) && !game.getConfig().isFullscreen()) {
							resize = true;
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("1920x1080")) {
								newRes[0] = 1920;
								newRes[1] = 1055;
								game.getConfig().setResolution(newRes);
							}
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("1280x720")) {
								newRes[0] = 1280;
								newRes[1] = 695;
								game.getConfig().setResolution(newRes);
							}
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("852x480")) {
								newRes[0] = 852;
								newRes[1] = 455;
								game.getConfig().setResolution(newRes);
							}
						}
						if(currentMenuManager.getButtons().get(1).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Mode_Fullscreen"))&& !Gdx.graphics.isFullscreen()){
							resize = false;
							newRes[0] = 1920;
							newRes[1] = 1055;
							game.getConfig().setResolution(newRes);
							game.getConfig().setFullscreen(true);
							game.resize(0, 0);
						}else if(currentMenuManager.getButtons().get(1).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Mode_Window"))&& Gdx.graphics.isFullscreen()){
							resize = true;
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("1920x1080")) {
								newRes[0] = 1920;
								newRes[1] = 1055;
								game.getConfig().setResolution(newRes);
							}
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("1280x720")) {
								newRes[0] = 1280;
								newRes[1] = 695;
								game.getConfig().setResolution(newRes);
							}
							if (currentMenuManager.getButtons().get(0).getOption().getText().equals("852x480")) {
								newRes[0] = 852;
								newRes[1] = 455;
								game.getConfig().setResolution(newRes);
							}
						}
						
						if (resize) {
							game.getConfig().setFullscreen(false);
							game.resize(0, 0);
						}
						if(currentMenuManager.getButtons().get(2).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Language_German"))) currentLang = "de";
						else if(currentMenuManager.getButtons().get(2).getOption().getText().equals(game.getConfig().getMessage("GraphicsMenu_Language_English"))) currentLang = "en";
						if(!currentLang.equals(game.getConfig().getLanguage())){
							try {
								game.getConfig().setLanguage(currentLang);
								reload = true;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
						}
						if(reload){
							game.reload();
						}
						menuIndex = -1;
						currentMenuManager = null;
						while(videoMenuManager.getIndex() != 0){
							videoMenuManager.prev();
						}
						break;
					}
					break;
				case SOUND:
					switch (currentMenuManager.getIndex()) {
					case 0:
						break;
					case 1:
						break;
					case 2:
						menuIndex = -1;
						currentMenuManager = null;
						while(musicMenuManager.getIndex() != 0){
							musicMenuManager.prev();
						}
						break;
					}
					break;
				case CONTROLS:
					break;
				}
				movementBlocked = true;
				blockStartTime = System.currentTimeMillis();
			}
		}
	}
	
	private void initMenus(){
		//initialize video settings
		ArrayList<ArrayList<String>> buttonList = new ArrayList<ArrayList<String>>();
		ArrayList<String> reso = new ArrayList<String>();
		reso.add("GraphicsMenu_Resolution");
		reso.add("GraphicsMenu_Resolution_1920x1080");
		reso.add("GraphicsMenu_Resolution_1280x720");
		reso.add("GraphicsMenu_Resolution_852x480");
		buttonList.add(reso);
		
		ArrayList<String> mode = new ArrayList<String>();
		mode.add("GraphicsMenu_Mode");
		mode.add("GraphicsMenu_Mode_Fullscreen");
		mode.add("GraphicsMenu_Mode_Window");
		buttonList.add(mode);
		
		ArrayList<String> lang = new ArrayList<String>();
		lang.add("GraphicsMenu_Language");
		lang.add("GraphicsMenu_Language_English");
		lang.add("GraphicsMenu_Language_German");
		buttonList.add(lang);
		
		ArrayList<String> back = new ArrayList<String>();
		back.add("SettingsMenu_Back");
		buttonList.add(back);
		
		videoMenuManager = new ButtonManager(buttonList, 100,true,"<",">", true);
		videoMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getHeight()},
				settingsScrollPos);
		
		//initialize sound settings
		buttonList = new ArrayList<>();
		ArrayList<String> music = new ArrayList<String>();
		music.add("SoundMenu_Music");
		buttonList.add(music);
		
		ArrayList<String> effects = new ArrayList<String>();
		effects.add("SoundMenu_Effects");
		buttonList.add(effects);
		
		back = new ArrayList<String>();
		back.add("SettingsMenu_Back");
		buttonList.add(back);
		
		musicMenuManager = new ButtonManager(buttonList, 100,true,"<",">", true);
		musicMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getHeight()},
				settingsScrollPos);
		
		//initialize control settings
		controlsMenuManager = new ButtonManager(buttonList, 100,true,"<",">", true);	
		
		//initialize main settings
		buttonList = new ArrayList<>();
		ArrayList<String> graphics = new ArrayList<String>();
		graphics.add("SettingsMenu_Graphics");
		buttonList.add(graphics);
		ArrayList<String> sound = new ArrayList<String>();
		sound.add("SettingsMenu_Sound");
		buttonList.add(sound);
		ArrayList<String> controls = new ArrayList<String>();
		controls.add("SettingsMenu_Controls");
		buttonList.add(controls);
		back = new ArrayList<String>();
		back.add("SettingsMenu_Back");
		buttonList.add(back);
		settingsMenuManager = new ButtonManager(buttonList, 100,true , "", "", true);
		settingsMenuManager.setMidOfBounds(new int[]{RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getWidth(), RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight()}, menuScrollPos);
	}

	@Override
	public void init() {
		// Positions
		menuScrollPos = new int[]{
				Gdx.graphics.getWidth() / 2 - (RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getWidth() + RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Big.png").getWidth()) / 2,
				Gdx.graphics.getHeight() / 2 - RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight() / 2};
		settingsScrollPos = new int[]{Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - RuntimeData.getInstance().getAsset("textures/menus/scrolls/VerticalScroll_Small.png").getHeight() / 2};	
		initMenus();
		isDisposed = false;
	}

	@Override
	public void reload() {
		settingsMenuManager.reload();
		videoMenuManager.reload();
		musicMenuManager.reload();
		controlsMenuManager.reload();
	}
}
