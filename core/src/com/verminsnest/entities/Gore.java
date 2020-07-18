package com.verminsnest.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.verminsnest.core.management.Indentifiers;
import com.verminsnest.core.management.data.RuntimeData;

public class Gore extends Entity {
	private Texture texture;
	private int goreID;
	private int[] movement;
	private Random rand;
	
	public Gore(int[] pos) {
		super(pos, Indentifiers.ASSETMANAGER_GORE);
		isObstacle = false;
		rand = new Random();
		goreID = rand.nextInt(3)+1;
		movement = new int[2];
		this.setSize(RuntimeData.getInstance().getAsset("textures/gore/Gore-"+goreID+".png").getWidth(), RuntimeData.getInstance().getAsset("textures/gore/Gore-"+goreID+".png").getHeight());
		init();
	}

	@Override
	public void init() {
		int randomizer = rand.nextInt(4);
		switch(randomizer){
		case 0:
			movement = new int[]{rand.nextInt(3)+4,rand.nextInt(3)-4};
			break;
		case 1:
			movement = new int[]{rand.nextInt(3)-4,rand.nextInt(3)+4};
			break;
		case 2:
			movement = new int[]{rand.nextInt(3)+4,rand.nextInt(3)+4};
			break;
		case 3:
			movement = new int[]{rand.nextInt(3)-4,rand.nextInt(3)-4};
			break;
		}
		switch(goreID){
		case 1:
			texture = RuntimeData.getInstance().getAsset("textures/gore/Gore-1.png");
			break;
		case 2:
			texture = RuntimeData.getInstance().getAsset("textures/gore/Gore-2.png");
			break;
		case 3:
			texture = RuntimeData.getInstance().getAsset("textures/gore/Gore-3.png");
			break;
		}
	}

	@Override
	public void setCurrentAni(int animationKey) {
		//Not needed
	}

	@Override
	public void update(float stateTime) {
		internalStateTime += Gdx.graphics.getDeltaTime();
		if(internalStateTime < 0.2){
			rotation += rand.nextInt(8)+4;
			RuntimeData.getInstance().getMovmentSystem().move(this, movement);
		}
	}

	@Override
	public TextureRegion getCurrentFrame(float stateTime) {
		return TextureRegion.split(texture, texture.getWidth(), texture.getHeight())[0][0];
	}
}