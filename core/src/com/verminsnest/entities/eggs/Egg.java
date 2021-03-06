package com.verminsnest.entities.eggs;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.verminsnest.core.management.data.RuntimeData;
import com.verminsnest.core.management.ids.Indentifiers;
import com.verminsnest.core.management.ids.Qualifier;
import com.verminsnest.entities.Entity;
import com.verminsnest.entities.enemies.Flunk;
import com.verminsnest.entities.enemies.Tinker;
import com.verminsnest.entities.particles.Gore;

public class Egg extends Entity {
	
	protected Animation<TextureRegion> idleAni;
	protected Animation<TextureRegion> hatchAni;
	protected Texture leftOver;
	
	protected int enemyID;
	protected boolean hatched;
	protected float hatchTime;
	
	public Egg(int[] pos, int enemyID) {
		super(pos, Indentifiers.ASSETMANAGER_EGG,Qualifier.RENDER_LAYER_MID);
		state = Indentifiers.STATE_IDLE;
		this.enemyID = enemyID;
		
		init();
		yShadowOffset = -10;
		this.setCurrentAni(Indentifiers.STATE_IDLE);
		this.setSize(currentAni.getKeyFrame(0).getRegionWidth(),currentAni.getKeyFrame(0).getRegionHeight());
		this.setHitbox(currentAni.getKeyFrame(0).getRegionWidth(),(int) (currentAni.getKeyFrame(0).getRegionHeight()/2.5));
		Random rand = new Random();
		hatchTime = rand.nextInt(6)+5;
	}

	@Override
	public void init() {
		hatched = false;
		
		shadow = RuntimeData.getInstance().getTexture("textures/shadows/Shadow-M.png");
		
		Texture idleSheet = RuntimeData.getInstance().getTexture("textures/enemies/eggs/Egg-Idle.png");
		TextureRegion[][] temp = TextureRegion.split(idleSheet, 50, 80);
		TextureRegion[] frames = new TextureRegion[temp[0].length];

		for (int i = 0; i < temp[0].length; i++) {
			frames[i] = temp[0][i];
		}
		idleAni = new Animation<TextureRegion>(0.8f, frames);
		
		Texture hatchedSheet = RuntimeData.getInstance().getTexture("textures/enemies/eggs/Egg-Hatch.png");
		temp = TextureRegion.split(hatchedSheet, 50, 80);
		frames = new TextureRegion[temp[0].length];

		for (int i = 0; i < temp[0].length; i++) {
			frames[i] = temp[0][i];
		}
		hatchAni = new Animation<TextureRegion>(0.05f, frames);
		
		leftOver = RuntimeData.getInstance().getTexture("textures/enemies/eggs/Egg-Leftover.png");
	}
	
	@Override
	public void setCurrentAni(int animationKey) {
		switch(animationKey){
		case Indentifiers.STATE_IDLE:
			state = Indentifiers.STATE_IDLE;
			currentAni = idleAni;
			break;
		case Indentifiers.STATE_SPAWNING:
			state = Indentifiers.STATE_SPAWNING;
			currentAni = hatchAni;
			break;
		case Indentifiers.STATE_LEFTOVER:
			isObstacle = false;
			state = Indentifiers.STATE_LEFTOVER;
			currentAni = null;
			this.setSize(leftOver.getWidth(),leftOver.getHeight());
		}
	}

	@Override
	public void update(float delta) {
		internalStateTime += delta;
		switch (state) {
		case Indentifiers.STATE_IDLE:
			if (internalStateTime > hatchTime) {
				setCurrentAni(Indentifiers.STATE_SPAWNING);
				internalStateTime = 0;
			}
			break;
		case Indentifiers.STATE_SPAWNING:
			if (!hatched) {
				switch (enemyID) {
				case Indentifiers.ASSETMANAGER_TINKER:
					new Tinker(new int[] { this.pos[0] - 8, this.pos[1] + 13 });
					break;
				case Indentifiers.ASSETMANAGER_FLUNK:
					new Flunk(new int[] { this.pos[0] - 8, this.pos[1] + 5 });
					new Flunk(new int[] { this.pos[0] + 20, this.pos[1] + 5 });
					new Flunk(new int[] { this.pos[0] + 12, this.pos[1] + 25 });
					break;
				}
				hatched = true;
				Random rand = new Random();
				for(int i = 0; i < rand.nextInt(3)+4; i++){
					new Gore(new int[]{this.pos[0], this.pos[1]});
				}
			}
			if (currentAni.isAnimationFinished(internalStateTime)) {
				setCurrentAni(Indentifiers.STATE_LEFTOVER);
				RuntimeData.getInstance().getEntityManager().sortToLeftover(this);
			}
			break;
		case Indentifiers.STATE_LEFTOVER:
			break;

		}
	}

	@Override
	public TextureRegion getCurrentFrame(float stateTime){
		if(state != Indentifiers.STATE_LEFTOVER){
			return super.getCurrentFrame(stateTime);
		}else{
			return TextureRegion.split(leftOver, leftOver.getWidth(), leftOver.getHeight())[0][0];
		}
	}
}
