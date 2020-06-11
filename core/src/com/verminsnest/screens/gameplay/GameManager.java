package com.verminsnest.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.verminsnest.core.VerminsNest;
import com.verminsnest.core.management.Indentifiers;
import com.verminsnest.core.management.data.RuntimeData;
import com.verminsnest.entities.playables.Mage;
import com.verminsnest.screens.VNScreen;
import com.verminsnest.screens.gameplay.menus.LevelMenu;
import com.verminsnest.screens.gameplay.menus.PauseMenu;

public class GameManager extends VNScreen {
	
	// Rendering
	private float timeSinceRender = 0;
	private float updateStep = 1 / 120f;

	//Control blocking
	private long blockTime;
	private boolean controlBlocked;
	private long blockStartTime;
	
	public static final int RUNNING = 0;
	public static final int PAUSEMENU = 1;
	public static final int LEVELMENU = 2;
	private int state;
	
	private Gameplay gameplay;
	private PauseMenu pauseMenu;
	private LevelMenu levelMenu;
	
	public GameManager() {
		super();
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
			timeSinceRender += Gdx.graphics.getDeltaTime();
			//Cap out rendering cycle to 120 frames/second
			if (timeSinceRender >= updateStep) {
				timeSinceRender -= updateStep;

				blockTime = System.currentTimeMillis() - blockStartTime;
				if (blockTime > 225) {
					controlBlocked = false;
				}
				
				if(!RuntimeData.getInstance().isGameOver()){
					RuntimeData.getInstance().getGame().getBatch().begin();
					switch(state){
					case RUNNING:
						gameplay.update(delta);
						gameplay.render(delta);
						break;
					case PAUSEMENU:
						gameplay.render(delta);
						pauseMenu.render(delta);
						pauseMenu.update(delta);
						break;
					case LEVELMENU:
						gameplay.render(delta);
						levelMenu.render(delta);
						levelMenu.update(delta);
						break;
					}
					RuntimeData.getInstance().getGame().getBatch().end();
				}else{
					RuntimeData.getInstance().getGame().showScreen(VerminsNest.MAINMENU);
				}
		}
	}

	public void setState(int state){
		switch(state){
		case RUNNING:
			this.state = RUNNING;
			break;
		case PAUSEMENU:
			pauseMenu.init();
			this.state = PAUSEMENU;
			break;
		case LEVELMENU:
			levelMenu.init();
			this.state = LEVELMENU;
			break;
		}
		this.resetBlocked();
	}
	
	public void resetBlocked(){
		controlBlocked = true;
		blockStartTime = System.currentTimeMillis();
	}
	
	public boolean isControlBlocked(){
		return controlBlocked;
	}
	
	@Override
	public void resize(int width, int height) {
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
		gameplay.dispose();
		levelMenu.dispose();
		pauseMenu.dispose();

		RuntimeData.getInstance().disposeTextures(Indentifiers.ASSETMANAGER_GAMEPLAY);
		RuntimeData.getInstance().getEntityManager().clearData();
		
		isDisposed = true;
	}

	@Override
	public void init() {
		
		if(RuntimeData.getInstance().getEntityManager().getCharacter() == null) {
			RuntimeData.getInstance().getEntityManager().setCharacter(new Mage(new int[] { 0, 0 }));
			for (int x = 0; x < RuntimeData.getInstance().getMapData().getData().length; x++) {
				for (int y = 0; y < RuntimeData.getInstance().getMapData().getData()[0].length; y++) {
					if (RuntimeData.getInstance().getMapData().getData()[x][y].isWalkable()) {
						RuntimeData.getInstance().getEntityManager().getCharacter().getPos()[0] = RuntimeData.getInstance().getMapData().getData()[x][y].getxPos();
						RuntimeData.getInstance().getEntityManager().getCharacter().getPos()[1] = RuntimeData.getInstance().getMapData().getData()[x][y].getyPos();
					}
				}
			}
			
			gameplay = new Gameplay(this);
			pauseMenu = new PauseMenu(this);
			levelMenu = new LevelMenu(this);
			pauseMenu.init();
			levelMenu.init();
		}else {
			for (int x = 0; x < RuntimeData.getInstance().getMapData().getData().length; x++) {
				for (int y = 0; y < RuntimeData.getInstance().getMapData().getData()[0].length; y++) {
					if (RuntimeData.getInstance().getMapData().getData()[x][y].isWalkable()) {
						RuntimeData.getInstance().getEntityManager().getCharacter().getPos()[0] = RuntimeData.getInstance().getMapData().getData()[x][y].getxPos();
						RuntimeData.getInstance().getEntityManager().getCharacter().getPos()[1] = RuntimeData.getInstance().getMapData().getData()[x][y].getyPos();
					}
				}
			}
		}
		
		state = RUNNING;
		

		// Controls
		blockTime = 0;
		blockStartTime = System.currentTimeMillis();
		controlBlocked = true;
		isDisposed = false;
	}

	@Override
	public void reload() {
	}
}
